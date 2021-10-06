package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MetalScraper extends Scraper {
    protected final Dealer dealer;
    protected final LinkService linkService;
    protected final PriceService priceService;
    protected final PortfolioService portfolioService;
    protected final ProductService productService;

    protected final List<String> searchUrlGold;
    protected final List<String> searchUrlSilver;
    protected final List<String> searchUrlPlatinum;
    protected final List<String> searchUrlPalladium;

    private static final long ETHICAL_DELAY = 700;
    private static final int PRINT_INTERVAL = 10;

    private final String xPathProductList;
    private final String xPathProductName;
    private final String xPathBuyPrice;
    private final String xPathRedemptionPrice;

    public MetalScraper(Dealer dealer, LinkService linkService, PriceService priceService, PortfolioService portfolioService, ProductService productService, List<String> searchUrlGold, List<String> searchUrlSilver, List<String> searchUrlPlatinum, List<String> searchUrlPalladium, String xPathProductList, String xPathProductName, String xPathBuyPrice, String xPathRedemptionPrice) {
        super();
        this.dealer = dealer;
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.searchUrlGold = searchUrlGold;
        this.searchUrlSilver = searchUrlSilver;
        this.searchUrlPlatinum = searchUrlPlatinum;
        this.searchUrlPalladium = searchUrlPalladium;
        this.xPathProductList = xPathProductList;
        this.xPathProductName = xPathProductName;
        this.xPathBuyPrice = xPathBuyPrice;
        this.xPathRedemptionPrice = xPathRedemptionPrice;
    }

    ////////////// PRODUCT

    /////// PUBLIC

    /**
     * Scraps products based on Links from DB
     */
    public void allProducts() {
        productsOrPricesScrap( linkService.findAll() );
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        System.out.println("All products scraped");
    }

    public void productByDealer() {
        productsOrPricesScrap( linkService.findByDealer(dealer) );
    }

    public void productByPortfolio(long portfolioId) throws ResponseStatusException {
        System.out.println("MetalScraper productsByPortfolio");
        Optional<PortfolioDTO_ProductDTO> optionalPortfolio = portfolioService.findById(portfolioId);
        if (optionalPortfolio.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
        }

        // Set prevents one Product to be scrapped more times
        Set<Link> linkSet = optionalPortfolio.get().getInvestments()
                .stream()
                .map(
                        investment -> linkService.findByDealerAndProductId(dealer, investment.getProductDTO().getId())
                ).filter(
                        Optional::isPresent
                ).map(
                        Optional::get
                ).collect(
                        Collectors.toSet()
                );

        productsOrPricesScrap( new ArrayList<>(linkSet) );
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }

    /////// PRIVATE

    private void productScrap(Link link) {
        loadPage(link.getLink());

        String name = "";
        try {
            name = ((HtmlElement) page.getFirstByXPath(xPathProductName)).asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String nameLowerCase = name.toLowerCase(Locale.ROOT);
        final int year = Extract.yearExtract(nameLowerCase);
        final Form form = Extract.formExtract(nameLowerCase);
        final Metal metal = Extract.metalExtractor(nameLowerCase);
        final double grams = Extract.weightExtract(nameLowerCase);
        final Producer producer = Extract.producerExtract(nameLowerCase);
        final List<Product> products = productService.findProductByProducerAndMetalAndFormAndGramsAndYear(producer, metal, form, grams, year);

        if(name.equals("") || producer == Producer.UNKNOWN || form == Form.UNKNOWN || metal == Metal.UNKNOWN ) {
            System.out.println("FATAL ERROR: "+name +" "+ producer+" "+metal+" "+form+" "+grams+ " "+link.getLink());
            return;
        }


        // New product saved
        if(products.isEmpty() || specialName(nameLowerCase)) {
            List<Link> links = new ArrayList<>();
            links.add(link);
            Product product = new Product(name, producer, form, metal, grams, year, links, new ArrayList<>(), new ArrayList<>());
            productService.save(product);
            link.setProduct(product);
            linkService.save(link);
            priceScrap(link);
            System.out.println(">> Product saved");
            return;
        }

        // Price from another dealer added to product
        if (products.size() == 1) {
            products.get(0).getLinks().add(link);
            link.setProduct(products.get(0));
            linkService.save(link);
            priceScrap(link);
            return;
        }

        System.out.println("\n???????????????????");
        System.out.println(name + " " +producer+" "+metal+" "+form+" "+grams+" "+link.getLink());
        System.out.println("???????????????????\n");

    }

    /**
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    private void productOrPriceScrap(final Link link) {
        Optional<Product> optionalProduct = this.productService.findByLink(link.getLink());

        if (optionalProduct.isEmpty()) {
            productScrap(link);
            return;
        }
        priceScrap(link);
    }

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    private void productsOrPricesScrap(final List<Link> links) {
        for (Link link : links) {
            long startTime = System.nanoTime();
            productOrPriceScrap(link);
            // Sleep time is dynamic, according to time took by scrap procedure
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, startTime, PRINT_INTERVAL, links.size());
        }
        printerCounter = 0;
    }


    ////////////// PRICE

    /////// PUBLIC

    /**
     * Scraps prices by metal from all dealers
     * @param metal Enum
     */
    public void pricesByMetal(Metal metal){
        System.out.println("MetalScraper pricesByMetal");

        scrapGivenProducts(
                productService.findByMetal(metal).stream()
                        .map(
                                ProductDTO::getId
                        )
                        .collect(Collectors.toList())
        );

        System.out.println(metal+" prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }

    public void pricesByProducts(List<Long> productIds){
        System.out.println("MetalScraper pricesByProducts");
        scrapGivenProducts(productIds);
        System.out.println(">> Prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }

    /////// PROTECTED

    /**
     * Scraps new price for already known product
     * @param link Link from which the price gonna be scrapped
     */
    protected void priceScrap(final Link link) {
        loadPage(link.getLink());

        double buyPrice = 0.0;
        double redemptionPrice = 0.0;
        try {
            buyPrice = Extract.numberExtract(((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText());
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
        }
        try {
            redemptionPrice = Extract.numberExtract(redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice)));
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
        }

        addPriceToProduct(
                link.getProduct(),
                new Price(LocalDateTime.now(), buyPrice, redemptionPrice, link.getDealer())
        );
        System.out.println("> New price saved - " + link.getLink());
    }

    private void scrapGivenProducts(final List<Long> productIds) {
        for (long productId : productIds) {
            long startTime = System.nanoTime();

            // Scraps new price for this.dealer product link
            linkService.findByDealerAndProductId(dealer, productId)
                    .ifPresent(
                            this::priceScrap
            );

            // Sleep time is dynamic, according to time took by scrap procedure
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, startTime, PRINT_INTERVAL, productIds.size());
        }
        printerCounter = 0;
    }


    ///////////////////// LINK

    /////// PUBLIC

    public void allLinksScrap() {
        linkScrap(Metal.GOLD);
        linkScrap(Metal.SILVER);
        linkScrap(Metal.PLATINUM);
        linkScrap(Metal.PALLADIUM);
    }

    public void linkScrap(Metal metal) {
        switch (metal) {
            case GOLD -> searchUrlGold.forEach(this::scrapLinks);
            case SILVER -> searchUrlSilver.forEach(this::scrapLinks);
            case PLATINUM -> searchUrlPlatinum.forEach(this::scrapLinks);
            case PALLADIUM -> searchUrlPalladium.forEach(this::scrapLinks);
        }
    }

    /////// PROTECTED

    /**
     * Finds list of elements, based on class variable xPathProductList
     * For each calls scrapLink abstract method.
     * @param searchUrl URL of page, where the search will be done
     */
    protected void scrapLinks(String searchUrl) {
        loadPage(searchUrl);

        List<HtmlElement> elements = page.getByXPath(xPathProductList);

        System.out.println(elements.size()+" HTMLElements to scrap");

        // Scraps new link for each element
        elements.forEach(
                element -> scrapLink(element, searchUrl)
        );

        System.out.println("Total number of links: " + linkService.findAll().size());
    }

    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }


    ////////////// FILTER

    /////// PROTECTED

    /**
     * Filtration based on text of the link
     * @param link Link about to be filtered
     * @return False for link being filtered
     */
    protected static boolean linkFilter(String link) {
        if (link.contains("etuje")) {
            System.out.println("Link vyřazen: etuje - " + link);
            return false;
        }
        if (link.contains("obalka")) {
            System.out.println("Link vyřazen: obalka - " + link);
            return false;
        }
        if (link.contains("kapsle")) {
            System.out.println("Link vyřazen: kapsle - " + link);
            return false;
        }
        if (link.contains("slit") || link.contains("minc") || link.contains("lunarni-serie-rok") || link.contains("tolar")) {
            return true;
        }
        if (link.contains("goldbarren") || link.contains("krugerrand") || link.contains("sliek")) {
            return true;
        }
        System.out.println("Link vyřazen: " + link);
        return false;
    }

    protected void linkFilterWrapper(final Link link) {
        if ( !linkFilter( link.getLink() ) ) {
            return;
        }
        List<Link> linkList = linkService.findByLink(link.getLink());

        if (linkList.isEmpty()) {
            linkService.save(link);
            System.out.println("Link saved");
            return;
        }
        if (linkList.size() == 1) {
            System.out.println("Link already in DB");
            return;
        }
        System.out.println("WARNING - Duplicates in DB table LINK");
    }

    protected String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText();
    }

    /////// PRIVATE

    /**
     * Price is added to Product. LatestPrice is updated.
     * @param product Product where the Price gonna be added
     * @param price Price to be added to Product
     */
    private void addPriceToProduct(Product product, Price price) {
        List<Price> priceList = product.getPrices();
        this.priceService.save(price);
        priceList.add(price);
        product.setLatestPrice(price);
        product.setPrices(priceList);
        this.productService.save(product);
    }

    private static boolean specialName(String name) {
        name = name.toLowerCase(Locale.ROOT);
        return name.contains("lunární") || name.contains("výročí") || name.contains("rush") || name.contains("horečka");
        // TODO Add attribute type to Product
    }

}
