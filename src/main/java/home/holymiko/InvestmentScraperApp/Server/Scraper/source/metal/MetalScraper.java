package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapFailedException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.*;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Extract;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import home.holymiko.InvestmentScraperApp.Server.Core.LogBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MetalScraper {
    private static final long ETHICAL_DELAY = 700;
    private static final int PRINT_INTERVAL = 10;
    private static final Logger LOGGER = LoggerFactory.getLogger(MetalScraper.class);

    private final LinkService linkService;
    private final PriceService priceService;
    private final PortfolioService portfolioService;
    private final ProductService productService;

    // Used for Polymorphic calling
    private final Map<Dealer, MetalAdapterInterface> dealerToMetalAdapter = new HashMap<>();


    @Autowired
    public MetalScraper(
            LinkService linkService,
            PriceService priceService,
            PortfolioService portfolioService,
            ProductService productService
    ) {
        super();
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
    }

    public void addAdapter(Dealer dealer, MetalAdapterInterface adapter) {
        dealerToMetalAdapter.put(dealer, adapter);
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
            LogBuilder.statusPrint(PRINT_INTERVAL, links.size(), counter++);
            // Sleep time is dynamic, according to time took by scrap procedure
            Client.dynamicSleep(ETHICAL_DELAY, startTime);
        }
    }

    /**
     * Scraps Price by Products. Each dealer has max one Link related to each Product.
     * That's why Product Links can be scraped at the same time, because workload is
     * distributed between different web servers (different Dealers)
     * Performs ethical delay.
     * @param linksGroupByProduct Lists of Links, grouped in List, by Product
     */
    public void generalInSyncScrapAndSleep(final List<List<LinkDTO>> linksGroupByProduct) {
        int counter = 0;
        for (List<LinkDTO> productLinks : linksGroupByProduct) {
            long startTime = System.nanoTime();

            // TODO Make multithreading here
            // Main method for scraping a document
            productLinks.forEach(this::generalScrap);

            // TODO Logging
            LogBuilder.statusPrint(PRINT_INTERVAL, linksGroupByProduct.size(), counter++);
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
        LOGGER.info("MetalScraper productsByPortfolio");
        PortfolioDTO_ProductDTO optionalPortfolio = portfolioService.findByIdAsDTO(portfolioId);

        // Get Links for scraping
        // Set prevents one Product to be scrapped more times
        Set<LinkDTO> linkSet = optionalPortfolio.getInvestmentsMetal()
                .stream()
                .map(
                        investment -> linkService.findByProductId( investment.getProductDTO().getId() )
                ).flatMap(
                        List::stream
                ).collect(
                        Collectors.toSet()
                );

        generalScrapAndSleep( new ArrayList<>(linkSet) );
        LogBuilder.printTimeStamp();
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
    private void productScrap(LinkDTO link) throws ResourceNotFoundException, ScrapFailedException, DataIntegrityViolationException {
        String name = "";
        final HtmlPage page;
        final ProductCreateDTO productExtracted;
        final MetalAdapterInterface adapter = dealerToMetalAdapter.get(link.getDealer());

        if(link.getProductId() != null) {
            throw new ScrapFailedException("ProductScrap - Product already scraped");
        }
        if(adapter == null) {
            throw new ScrapFailedException("ProductScrap - Scraper for dealer "+link.getDealer()+" not found");
        }

        // Throws ResourceNotFoundException
        page = adapter.getPage(link.getUri());

        // Scraps name of the Product
        try {
            name = adapter.scrapNameFromProductPage(page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Scraps name of the Product
        try {
            name = adapter.scrapNameFromProductPage(page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(name == null || name.equals("") ) {
            throw new ScrapFailedException("Invalid name - "+link.getUri());
        }

        // Extraction of parameters for saving new Product to DB
        try {
            productExtracted = Extract.productAggregateExtract(name);
        } catch (IllegalArgumentException e) {
            throw new ScrapFailedException("Extraction ERROR: "+name +" "+link.getUri()+" - "+e.getMessage());
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

            link = linkService.updateLinkProductId(link.getId(), productFound.getId());
            priceScrap(link);
            return;
        }

        throw new DataIntegrityViolationException(
                "ERROR: Multiple Products for following params present in DB\n" + productExtracted.toString()
        );
    }

    /** Method meant to be used ONLY in productSaveSwitch
     * @param linkDTO
     * @param productExtracted
     */
    private void saveValidNewProductAndScrapPrice(LinkDTO linkDTO, ProductCreateDTO productExtracted) {
        Product p = productService.save(productExtracted);
        linkDTO = linkService.updateLinkProductId(linkDTO.getId(), p.getId());
        priceScrap(linkDTO);
        LOGGER.info("Product saved");
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
            LOGGER.warn( e.getMessage() );
        }
    }

    ////////////// PRICE
    /////// PRIVATE

    /**
     * Main Price scraping method
     * Scraps new price for linkDTO with assigned product
     */
    private void priceScrap(final LinkDTO linkDTO) {
        Double sellingPrice = null;
        Double butOutPrice = null;
        final MetalAdapterInterface adapter = dealerToMetalAdapter.get(linkDTO.getDealer());
        final PricePair pricePair;
        HtmlPage productDetailPage;

        if(adapter == null) {
            throw new ScrapFailedException("ProductScrap - Scraper for dealer "+linkDTO.getDealer()+" not found");
        }
        try {
            productDetailPage = adapter.getPage(linkDTO.getUri());
        } catch (ResourceNotFoundException e) {
            // TODO Handle this & Log this
            return;
        }

        try {
            // Choose MetalScraperInterface & scrap buy price
            sellingPrice = adapter.scrapPriceFromProductPage(productDetailPage);
        } catch (NumberFormatException e) {
            LOGGER.warn(e.getMessage());
        }
        if(sellingPrice == null || sellingPrice.intValue() == 0) {
            LOGGER.warn("Kupni cena = 0");
        }
        butOutPrice = adapter.scrapBuyOutPrice(productDetailPage);
        if(butOutPrice.intValue() == 0) {
            LOGGER.warn("Vykupni cena = 0");
        }

        pricePair = new PricePair(
                priceService.save(new Price(LocalDateTime.now(), sellingPrice, false)),
                priceService.save(new Price(LocalDateTime.now(), butOutPrice, true)),
                linkDTO.getId()
        );

        // Save PricePair
        this.priceService.save(pricePair);
        this.productService.updatePrices(linkDTO.getId(), pricePair);

        LOGGER.info("New pricePair saved - " + linkDTO.getUri());
    }

    /**
     * TODO Method has to be tested
     */
    @Deprecated
    private void redemptionScrap(final Metal metal, final Dealer dealer) {
//        LOGGER.info("Redemption Scrap");
//        if( dealerToMetalAdapter.get(dealer) instanceof BuyOutInterface) {
//            LOGGER.info("Scraping Redemption List available");
//            List<Pair<String, Double>> nameRedemptionMap = ((BuyOutInterface) dealerToMetalAdapter.get(dealer)).scrapBuyOutFromList();
//
//            nameRedemptionMap.forEach(
//                x -> {
//                    try {
//                        priceService.updatePricePair(x.getFirst(), dealer, x.getSecond(), true);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            );
//        } else {
//            LOGGER.warn("Scraping Redemption List is NOT available");
//            // TODO Throw and catch in upper layer
//        }
//
//        LOGGER.info("Redemption "+dealer+" "+metal+" update");
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

    private void saveAndCountLinks(List<Link> linkList, AtomicInteger linkSaveCounter, AtomicInteger linkInDBCounter) {
        for (Link link : linkList) {
            try {
                linkService.saveOrUpdate(link);
                linkSaveCounter.getAndIncrement();
            } catch (DataIntegrityViolationException e) {
                linkInDBCounter.getAndIncrement();
            } catch (IllegalArgumentException e) {
                LOGGER.info(e.getMessage());
            } catch (NullPointerException e) {
                LOGGER.error(e.getMessage());
            }
        }
    }

    /**
     * Polymorphic call on all instances of dealerInterfaces a.k.a. dealerMetalScrapers
     */
    public void allLinksScrap() {
        AtomicInteger linkSaveCounter = new AtomicInteger(0);
        AtomicInteger linkInDBCounter = new AtomicInteger(0);

        // Polymorphic call
        for (MetalAdapterInterface scraperInterface : dealerToMetalAdapter.values()) {
            saveAndCountLinks(scraperInterface.scrapAllLinksFromProductLists(), linkSaveCounter, linkInDBCounter);
        }
        LOGGER.info("Links saved: " + linkSaveCounter + ", Links already in DB: " + linkInDBCounter);
    }

    public void linksByDealerScrap(Dealer dealer) {
        AtomicInteger linkSaveCounter = new AtomicInteger(0);
        AtomicInteger linkInDBCounter = new AtomicInteger(0);

        saveAndCountLinks(dealerToMetalAdapter.get(dealer).scrapAllLinksFromProductLists(), linkSaveCounter, linkInDBCounter);
        LOGGER.info("Links saved: " + linkSaveCounter + ", Links already in DB: " + linkInDBCounter);
    }

}
