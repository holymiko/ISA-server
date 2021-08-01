package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScrapMetal extends Scrap {
    protected final Dealer dealer;
    protected final LinkService linkService;
    protected final PriceService priceService;
    protected final PortfolioService portfolioService;
    protected final ProductService productService;
    protected final InvestmentService investmentService;

    protected final String searchUrlGold;
    protected final String searchUrlSilver;
    protected final String searchUrlPlatinum;
    protected final String searchUrlPalladium;

    private static final long ETHICAL_DELAY = 700;
    private static final int INTERVAL_PRINT = 10;

    private final String xPathProductList;
    private final String xPathProductName;
    private final String xPathBuyPrice;
    private final String xPathRedemptionPrice;

    public ScrapMetal(Dealer dealer, LinkService linkService, PriceService priceService, PortfolioService portfolioService, ProductService productService, InvestmentService investmentService, String searchUrlGold, String searchUrlSilver, String searchUrlPlatinum, String searchUrlPalladium, String xPathProductList, String xPathProductName, String xPathBuyPrice, String xPathRedemptionPrice) {
        super();
        this.dealer = dealer;
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.investmentService = investmentService;
        this.searchUrlGold = searchUrlGold;
        this.searchUrlSilver = searchUrlSilver;
        this.searchUrlPlatinum = searchUrlPlatinum;
        this.searchUrlPalladium = searchUrlPalladium;
        this.xPathProductList = xPathProductList;
        this.xPathProductName = xPathProductName;
        this.xPathBuyPrice = xPathBuyPrice;
        this.xPathRedemptionPrice = xPathRedemptionPrice;
    }

    /////// PRODUCT

    private void productByLink(final Link link) {
        loadPage(link.getLink());

        String name = "";
        try {
            name = ((HtmlElement) page.getFirstByXPath(xPathProductName)).asText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String nameLowerCase = name.toLowerCase(Locale.ROOT);
        final Form form = Extractor.formExtractor(nameLowerCase);
        final Metal metal = Extractor.metalExtractor(nameLowerCase);
        final double grams = Extractor.weightExtractor(nameLowerCase);
        final Producer producer = Extractor.producerExtractor(nameLowerCase);
        final List<Product> products = productService.findProductByProducerAndMetalAndFormAndGrams(producer, metal, form, grams);

        if(name.equals("") || producer == Producer.UNKNOWN || form == Form.UNKNOWN || metal == Metal.UNKNOWN ) {
            System.out.println("FATAL ERROR: "+name +" "+ producer+" "+metal+" "+form+" "+grams+ " "+link.getLink());
            return;
        }

        // Year specials are being saved separately
        Pattern pattern = Pattern.compile("20[12]\\d");

        if(products.isEmpty() || nameLowerCase.contains("rok")
        || nameLowerCase.contains("lunarni") || nameLowerCase.contains("výročí")
        || pattern.matcher(nameLowerCase).find()) {
            List<Link> links = new ArrayList<>();
            links.add(link);
            priceByProductScrap( new Product(name, producer, form, metal, grams, links, null, new ArrayList<>()), link );
            System.out.println(">> Product saved");
            return;
        }

        // Price from another dealer added to product
        if (products.size() == 1) {
            products.get(0).getLinks().add(link);
            priceByProductScrap(products.get(0), link);
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
            productByLink(link);
            return;
        }
        priceByProductScrap(optionalProduct.get(), link);
    }

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    private void productsOrPricesScrap(final List<Link> links) {
        for (Link link : links) {
            long start = System.nanoTime();
            productOrPriceScrap(link);
            // Sleep time is dynamic, according to time took by scrap procedure
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, start, INTERVAL_PRINT, links.size());
        }
        printerCounter = 0;
    }

    /**
     * Scraps products based on Links from DB
     */
    public void allProducts() {
        productsOrPricesScrap( linkService.findAll() );
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        System.out.println("All products scraped");
    }

    public void byDealerScrap() {
        productsOrPricesScrap( linkService.findByDealer(dealer) );
    }

    public void productsByPortfolio(long portfolioId) throws ResponseStatusException {
        System.out.println("ScrapMetal Portfolio-Products");
        Optional<Portfolio> optionalPortfolio = portfolioService.findById(portfolioId);
        if (optionalPortfolio.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
        }

        // Set prevents one Product to be scrapped more times
        Set<Link> linkSet = optionalPortfolio.get().getInvestments()
                .stream()
                .map(investment -> investment.getProduct().getLinks())
                .flatMap(List::stream)
                .collect(Collectors.toSet());

        productsOrPricesScrap( new ArrayList<>(linkSet) );
        portfolioService.refresh(portfolioId);
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }


    /////// PRICE

    /**
     * Scraps new price for already known product
     * @param product already saved in DB
     */
    protected void priceByProductScrap(Product product, Link link) {
        loadPage(link.getLink());

        double buyPrice = 0.0;
        double redemptionPrice = 0.0;
        try {
            buyPrice = Extractor.priceExtractor(((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText());
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
        }
        try {
            redemptionPrice = Extractor.priceExtractor(redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice)));
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
        }

        addPriceToProduct(
                product,
                new Price(LocalDateTime.now(), buyPrice, redemptionPrice, dealer)
        );
        System.out.println("> New price saved - "+link.getLink());
    }

    /**
     * Scraps prices by metal from all dealers
     * @param metal Enum
     */
    public void pricesByMetal(Metal metal){
        System.out.println("ScrapMetal pricesByMetal");
        List<Product> productList = this.productService.findByMetal(metal);
        for (Product product : productList) {
            long start = System.nanoTime();

            // Scraps new price for each product's link
            product.getLinks().forEach(
                    link -> priceByProductScrap(product, link)
            );

            // Sleep time is dynamic, according to time took by scrap procedure
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, start, INTERVAL_PRINT, productList.size());
        }
        printerCounter = 0;
        System.out.println(metal+" prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }

    public void pricesByProductIds(List<Long> productIds){
        System.out.println("ScrapMetal pricesByProductIds");
        for (Long productId : productIds) {
            long start = System.nanoTime();
            Optional<Product> optionalProduct = this.productService.findById(productId);

            if(optionalProduct.isEmpty()){
                printerCounter++;
                continue;
            }

            // Scraps new price for each product's link
            optionalProduct.get().getLinks().forEach(
                    link -> priceByProductScrap(optionalProduct.get(), link)
            );

            // Sleep time is dynamic, according to time took by scrap procedure
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, start, INTERVAL_PRINT, productIds.size());
        }
        printerCounter = 0;
        System.out.println(">> Prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }


    /////// LINK

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

    public void allLinksScrap() {
        goldLinksScrap();
        silverLinksScrap();
        platinumLinksScrap();
        palladiumLinksScrap();
    }

    public void goldLinksScrap() {
        scrapLinks(searchUrlGold);
    }

    public void silverLinksScrap() {
        scrapLinks(searchUrlSilver);
    }

    public void platinumLinksScrap() {
        scrapLinks(searchUrlPlatinum);
    }

    public void palladiumLinksScrap() {
        scrapLinks(searchUrlPalladium);
    }



    /////// FILTER

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

    protected String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText();
    }

}
