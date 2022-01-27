package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Mapper.LinkMapper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Extract;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class MetalScraper extends Scraper {
    protected final Dealer dealer;
    protected final LinkService linkService;
    protected final PriceService priceService;
    protected final PortfolioService portfolioService;
    protected final ProductService productService;
    protected final LinkMapper linkMapper;

    // Used for Polymorphic calling
    protected final Map< Metal, List<String>> searchUrlMap = new HashMap<>();

    private static final long ETHICAL_DELAY = 700;
    private static final int PRINT_INTERVAL = 10;

    private final String xPathProductList;
    private final String xPathProductName;
    private final String xPathBuyPrice;
    private final String xPathRedemptionPrice;

    public MetalScraper(
            Dealer dealer,
            LinkService linkService,
            PriceService priceService,
            PortfolioService portfolioService,
            ProductService productService,
            LinkMapper linkMapper,
            List<String> searchUrlGold, List<String> searchUrlSilver, List<String> searchUrlPlatinum, List<String> searchUrlPalladium,
            String xPathProductList, String xPathProductName, String xPathBuyPrice, String xPathRedemptionPrice
    ) {
        super();
        this.dealer = dealer;
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.linkMapper = linkMapper;
        this.xPathProductList = xPathProductList;
        this.xPathProductName = xPathProductName;
        this.xPathBuyPrice = xPathBuyPrice;
        this.xPathRedemptionPrice = xPathRedemptionPrice;

        searchUrlMap.put(Metal.GOLD, searchUrlGold);
        searchUrlMap.put(Metal.SILVER, searchUrlSilver);
        searchUrlMap.put(Metal.PLATINUM, searchUrlPlatinum);
        searchUrlMap.put(Metal.PALLADIUM, searchUrlPalladium);
    }

    ////////////// PRODUCT

    /////// PUBLIC

    /**
     * Scraps products based on Links from DB
     */
    public void allProducts() {
        productsOrPricesScrap( linkMapper.toDTO(linkService.findAll()) );
        ConsolePrinter.printTimeStamp();
        System.out.println("All products scraped");
    }

    public void productByDealer() {
        productsOrPricesScrap( linkMapper.toDTO(linkService.findByDealer(dealer)) );
    }

    public void productByPortfolio(long portfolioId) throws ResponseStatusException {
        System.out.println("MetalScraper productsByPortfolio");
        Optional<PortfolioDTO_ProductDTO> optionalPortfolio = portfolioService.findById(portfolioId);
        if (optionalPortfolio.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
        }

        // Set prevents one Product to be scrapped more times
        Set<LinkDTO> linkSet = optionalPortfolio.get().getInvestmentsMetal()
                .stream()
                .map(
                        investment -> linkService.findByDealerAndProductId(dealer, investment.getProductDTO().getId())
                ).filter(
                        Optional::isPresent
                ).map(
                        optionalLink -> linkMapper.toDTO(optionalLink.get())
                ).collect(
                        Collectors.toSet()
                );

        productsOrPricesScrap( new ArrayList<>(linkSet) );
        ConsolePrinter.printTimeStamp();
    }

    /////// PRIVATE

    private void productScrap(LinkDTO link) {
        String name = "";
        final int year;
        final double grams;
        final Form form;
        final Metal metal;
        final Producer producer;
        final List<Product> products;

        loadPage(link.getUrl());

        // Sends request for name of the Product. Prepares name for extraction (Extract methods)
        try {
            name = ((HtmlElement) page.getFirstByXPath(xPathProductName)).asText();
            name = name.toLowerCase(Locale.ROOT);
            if(name.equals("") ) {
                System.out.println("FATAL ERROR: Empty name - "+link.getUrl());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Extraction of parameters for saving new Product/Price to DB
        year = Extract.yearExtract(name);
        grams = Extract.weightExtract(name);
        try {
            form = Extract.formExtract(name);
            metal = Extract.metalExtractor(name);
            producer = Extract.producerExtract(name);
        } catch (IllegalArgumentException e) {
            System.out.println("FATAL ERROR: "+name +" "+link.getUrl()+" - "+e.getMessage());
            return;
        }

        products = productService.findProductByProducerAndMetalAndFormAndGramsAndYear(producer, metal, form, grams, year);

        // New product saved
        if(products.isEmpty() || isNameSpecial(name)) {
            final Product product = new Product(name, producer, form, metal, grams, year);
            productService.save(product);
            link = linkMapper.toDTO(
                    linkService.updateProduct(link.getId(), product)
            );
            priceScrap(link);
            System.out.println(">> Product saved");
            return;
        }

        // Product mapped by 2. Link
        // Price from another dealer added to product
        // TODO Number in condition should somehow correspond with number of active dealers
        if (products.size() == 1) {
            final Product product = products.get(0);
            link = linkMapper.toDTO(
                    linkService.updateProduct(link.getId(), product)
            );
            priceScrap(link);
            return;
        }

        throw new DataIntegrityViolationException(
                "ERROR: Multiple Products for following params present in DB\n" +
                name + " " +producer+" "+metal+" "+form+" "+grams+" "+link.getUrl()
        );
    }

    /**
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    private void productOrPriceScrap(final LinkDTO link) {
        Optional<Product> optionalProduct = this.productService.findByLink(link.getUrl());

        if (optionalProduct.isEmpty()) {
            try {
                productScrap(link);
            } catch (DataIntegrityViolationException e) {
                System.out.println( e.getMessage() );
            }
            return;
        }
        priceScrap(link);
    }

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    private void productsOrPricesScrap(final List<LinkDTO> links) {
        for (LinkDTO link : links) {
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
    public void pricesByMetal(Metal metal) {
        scrapProductByIdList(
                productService.findByMetal(metal).stream()
                        .map(
                                ProductDTO::getId
                        )
                        .collect(Collectors.toList())
        );
    }

    /////// PROTECTED

    /**
     * Scraps new price for already known product
     * @param link Link from which the price gonna be scrapped
     */
    protected void priceScrap(final LinkDTO link) {
        double buyPrice = 0.0;
        double redemptionPrice = 0.0;
        final Price price;

        loadPage(link.getUrl());

        try {
            buyPrice = Convert.currencyToNumberConvert(
                    ((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText()
            );
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
        }
        try {
            redemptionPrice = Convert.currencyToNumberConvert(
                    redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice))
            );
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
        }

        price = new Price(LocalDateTime.now(), buyPrice, redemptionPrice, link.getDealer());
        this.priceService.save(price);
        this.productService.updatePrice(link.getProductId(), price);

        System.out.println("> New price saved - " + link.getUrl());
    }

    public void scrapProductByIdList(final List<Long> productIds) {
        for (long productId : productIds) {
            long startTime = System.nanoTime();

            // Scraps new price for this.dealer product link
            linkService.findByDealerAndProductId(dealer, productId)
                    .ifPresent( link -> priceScrap(linkMapper.toDTO(link)) );

            // Sleep time is dynamic, according to time took by scrap procedure
            dynamicSleepAndStatusPrint(ETHICAL_DELAY, startTime, PRINT_INTERVAL, productIds.size());
        }
        printerCounter = 0;
    }


    ///////////////////// LINK

    /////// PUBLIC

    public void allLinksScrap() {
        // Polymorphic call
        searchUrlMap.values().stream()
                .flatMap(Collection::stream)
                .forEach(this::scrapLinks);
    }

    public void linkScrap(Metal metal) {
        searchUrlMap.get(metal)
                .forEach(this::scrapLinks);
    }

    /////// PROTECTED

    /**
     * Finds list of elements, based on class variable xPathProductList
     * For each calls scrapLink abstract method.
     * @param searchUrl URL of page, where the search will be done
     */
    protected void scrapLinks(String searchUrl) {
        if( !loadPage(searchUrl) ) {
            return;
        }

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
        if ( !linkFilter( link.getUrl() ) ) {
            return;
        }

        try {
            linkService.save(link);
            System.out.println("Link saved");
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
    }

    protected String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText();
    }

    /////// PRIVATE

    private static boolean isNameSpecial(String name) {
        name = name.toLowerCase(Locale.ROOT);
        return name.contains("lunární") || name.contains("výročí") || name.contains("rush") || name.contains("horečka");
        // TODO Add attribute type to Product
    }

}
