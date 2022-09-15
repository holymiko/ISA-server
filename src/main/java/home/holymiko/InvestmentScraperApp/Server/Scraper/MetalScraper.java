package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapFailedException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Extract;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.BessergoldDeMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.BessergoldMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.SilverumMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.ZlatakyMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MetalScraper extends Client {
    private final CNBScraper cnbScraper;
    private final LinkService linkService;
    private final PriceService priceService;
    private final PortfolioService portfolioService;
    private final ProductService productService;
    private final ExchangeRateService exchangeRateService;

    // Used for Polymorphic calling
    private final Map<Dealer, MetalScraperInterface> dealerInterfaces = new HashMap<>();

    private static final long ETHICAL_DELAY = 700;
    private static final int PRINT_INTERVAL = 10;

    @Autowired
    public MetalScraper(
            CNBScraper cnbScraper,
            LinkService linkService,
            PriceService priceService,
            PortfolioService portfolioService,
            ProductService productService,
            ExchangeRateService exchangeRateService
    ) {
        super();
        this.cnbScraper = cnbScraper;
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Automatically called method. Initialize instances dealer interfaces which are locally used in MetalScraper.java
     * Instances can be initialized in constructor, because exchange rate has to be scraped and injected at first.
     * (For foreign website scrapers)
     */
    @EventListener(ApplicationStartedEvent.class)
    public void initializeDealerInterfaces() {
        System.out.println(">> Metal scraper - Initialize dealer interfaces");
        dealerInterfaces.put(Dealer.BESSERGOLD_CZ, new BessergoldMetalScraper());
        dealerInterfaces.put(Dealer.SILVERUM, new SilverumMetalScraper());
        dealerInterfaces.put(Dealer.ZLATAKY, new ZlatakyMetalScraper());
//        TODO Try to move this method to Run.java
        try {
            cnbScraper.scrapExchangeRate();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        dealerInterfaces.put(
                Dealer.BESSERGOLD_DE,
                new BessergoldDeMetalScraper(
                        // Insert currency exchange rate for conversion to CZK
                        exchangeRateService.findFirstByCodeOrderByDateDesc("EUR").getExchangeRate()
                )
        );
    }

    ////////////// PRODUCT
    /////// PUBLIC

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    public void generalScrapAndSleep(final List<LinkDTO> links) {
        int counter = 0;
        for (LinkDTO link : links) {
            long startTime = System.nanoTime();

            // Main method for scraping a document
            generalScrap(link);

            // TODO Logging
            ConsolePrinter.statusPrint(PRINT_INTERVAL, links.size(), counter++);
            // Sleep time is dynamic, according to time took by scrap procedure
            Client.dynamicSleep(ETHICAL_DELAY, startTime);
        }
    }

    /**
     * TODO Needs to be checked and tested before putting into use.
     * @param portfolioId
     * @throws ResponseStatusException
     */
    @Deprecated
    public void productByPortfolio(long portfolioId) throws ResponseStatusException {
        System.out.println("MetalScraper productsByPortfolio");
        Optional<PortfolioDTO_ProductDTO> optionalPortfolio = portfolioService.findById(portfolioId);
        if (optionalPortfolio.isEmpty()) {
            throw new IllegalArgumentException("No portfolio with such ID");
        }

        // Get Links for scraping
        // Set prevents one Product to be scrapped more times
        Set<LinkDTO> linkSet = optionalPortfolio.get().getInvestmentsMetal()
                .stream()
                .map(
                        investment -> linkService.findByProductId( investment.getProductDTO().getId() )
                ).flatMap(
                        List::stream
                ).collect(
                        Collectors.toSet()
                );

        generalScrapAndSleep( new ArrayList<>(linkSet) );
        ConsolePrinter.printTimeStamp();
    }

    /////// PRIVATE

    /**
     * 1) Loads HTML page based on link
     * 2) Takes Product name from HTML
     * 3) Performs Product attributes extraction from Product name
     * 4) Calls saving method of Product
     * @param link without any related Product saved in DB
     * @throws ResourceNotFoundException loading page by link failed
     * @throws ScrapFailedException Link has saved product / Extraction from HTML failed
     * @throws DataIntegrityViolationException
     */
    private void productScrap(LinkDTO link) throws ResourceNotFoundException, ScrapFailedException, DataIntegrityViolationException{
        String name = "";
        final HtmlPage page;
        final ProductCreateDTO productExtracted;

        if(link.getProductId() != null) {
            throw new ScrapFailedException("ProductScrap - Product already scraped");
        }

        // Throws ResourceNotFoundException
        page = loadPage(link.getUrl());

        // Scraps name of the Product
        try {
            name = dealerInterfaces.get(link.getDealer()).scrapProductName(page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(name == null || name.equals("") ) {
            throw new ScrapFailedException("Invalid name - "+link.getUrl());
        }

        // Extraction of parameters for saving new Product to DB
        try {
            productExtracted = Extract.productAggregateExtract(name);
        } catch (IllegalArgumentException e) {
            throw new ScrapFailedException("Extraction ERROR: "+name +" "+link.getUrl()+" - "+e.getMessage());
        }

        productSaveSwitch(link, productExtracted);
    }

    /**
     * Method used for saving of Product. Decided how the newly scraped Product will be saved.
     * Options:
     *  a) Product is special
     *  b) Product has unique attributes. No Product in DB has corresponding attributes.
     *  c) Product doesn't have unique attributes. Product with corresponding attributes is already in DB.
     * Solutions:
     *  a,b) Save new Product
     *  c) Add new Link and PricePair to already existing Product
     * @param link
     * @param productExtracted
     */
    private void productSaveSwitch(LinkDTO link, ProductCreateDTO productExtracted) {
        final List<Product> nonSpecialProducts;

        // Special products are saved separately
        if(productExtracted.isSpecial()) {
            saveValidNewProductAndScrapPrice(link, productExtracted);
            return;
        }

        // Number of product for given extracted params should be within 0-1
        // Products from different Dealers with same params are supposed to be merged into one
        nonSpecialProducts = productService.findByParams(null, productExtracted);

        // New product saved
        if(nonSpecialProducts.isEmpty()) {
            saveValidNewProductAndScrapPrice(link, productExtracted);
            return;
        }

        // Product merging
        // PricePair & Link from another dealer will be added to existing Product
        if (nonSpecialProducts.size() == 1) {
            final Dealer linkDealer = link.getDealer();
            final Product productFound = nonSpecialProducts.get(0);

            // Max one Link per Dealer
            if(productFound.getLinks().stream().anyMatch(link1 -> link1.getDealer() == linkDealer)) {
                throw new DataIntegrityViolationException(
                        "ProductScrap - Only one Link per Dealer related to single Product allowed \n" +
                        "   Link:      " + link + "\n" +
                        "   Found:     " + productFound.toString() + "\n"
                );
            }

            link = linkService.updateProductLinks(link.getId(), productFound);
            priceScrap(link);
            return;
        }

        throw new DataIntegrityViolationException(
                "ERROR: Multiple Products for following params present in DB\n" + productExtracted.toString()
        );
    }

    /** Method meant to be used ONLY in productSaveSwitch
     * @param link
     * @param productExtracted
     */
    private void saveValidNewProductAndScrapPrice(LinkDTO link, ProductCreateDTO productExtracted) {
        final Product productToSave = productService.save(productExtracted);
        link = linkService.updateProductLinks(link.getId(), productToSave);
        priceScrap(link);
        System.out.println(">> Product saved");
    }

    /**
     * Main method for scraping a document
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    private void generalScrap(final LinkDTO link) {
        // Product is already assigned to Link -> update PricePair
        if (link.getProductId() != null) {
            priceScrap(link);
            return;
        }

        try {
            productScrap(link);
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
    }

    ////////////// PRICE
    /////// PRIVATE

    /**
     * Main Price scraping method
     * Scraps new price for link with assigned product
     * @param link Link from which the price will be scrapped
     */
    private void priceScrap(final LinkDTO link) {
        long startTime = System.nanoTime();
        Double sellingPrice = null;
        Double redemptionPrice = null;
        final PricePair pricePair;
        HtmlPage productDetailPage;

        try {
            productDetailPage = loadPage(link.getUrl());
        } catch (ResourceNotFoundException e) {
            // TODO Handle this & Log this
            return;
        }

        try {
            // Choose MetalScraperInterface & scrap buy price
            sellingPrice = dealerInterfaces.get(link.getDealer()).scrapBuyPrice(productDetailPage);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        redemptionPrice = dealerInterfaces.get(link.getDealer()).scrapRedemptionPrice(productDetailPage);

        pricePair = new PricePair(
                link.getDealer(),
                priceService.save(new Price(LocalDateTime.now(), sellingPrice, false)),
                priceService.save(new Price(LocalDateTime.now(), redemptionPrice, true)),
                productService.findById(link.getProductId()).get()
        );

        // Save PricePair
        this.priceService.save(pricePair);
        this.productService.updatePrices(link.getProductId(), pricePair);

        System.out.println("> New pricePair saved - " + link.getUrl());

        // Sleep time is dynamic, according to time took by scrap procedure
        Client.dynamicSleep(ETHICAL_DELAY, startTime);
    }

    /**
     * TODO Method has to be tested
     */
    @Deprecated
    private void redemptionScrap(final Metal metal, final Dealer dealer) {
        System.out.println(">>> Redemption Scrap");
        if( dealerInterfaces.get(dealer) instanceof ScrapRedemptionFromListInterface ) {
            System.out.println(">>>>> Scraping Redemption List available");
            List<Pair<String, Double>> nameRedemptionMap = ((ScrapRedemptionFromListInterface) dealerInterfaces.get(dealer)).scrapRedemptionFromList();

            nameRedemptionMap.forEach(
                x -> {
                    try {
                        priceService.updatePricePair(x.getFirst(), dealer, x.getSecond(), true);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            );
        } else {
            System.out.println(">>>>> Scraping Redemption List is NOT available");
            // TODO Throw and catch in upper layer
        }

        System.out.println("Redemption "+dealer+" "+metal+" update");
    }


    /**
     * TODO Method has to be tested
     */
    @Deprecated
    public void scrapProductByIdList(final List<Long> productIds) {
        for (long productId : productIds) {
            // Scraps new price for this.dealer product link
            linkService.findByProductId(productId)
                    .forEach(
                            this::priceScrap
                    );
        }
    }


    ///////////////////// LINK
    /////// PUBLIC

    /**
     * Polymorphic call on all instances of dealerInterfaces a.k.a. dealerMetalScrapers
     */
    public void allLinksScrap() {
        // Polymorphic call
        dealerInterfaces.values().forEach(
                scraperInterface -> scraperInterface.scrapAllLinks()
                        .forEach(
                                link -> {
                                        try {
                                            linkService.save(link);
                                            System.out.println("Link saved");
                                        } catch (Exception e) {
                                            System.out.println(e.getMessage());
                                        }
                                }
                        )
        );
    }

    public void linksByDealerScrap(Dealer dealer) {
        dealerInterfaces.get(dealer).scrapAllLinks()
                .forEach(
                        link -> {
                            try {
                                linkService.save(link);
                                System.out.println("Link saved");
                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                );
    }

}
