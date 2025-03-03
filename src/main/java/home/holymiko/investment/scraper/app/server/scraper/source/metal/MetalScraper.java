package home.holymiko.investment.scraper.app.server.scraper.source.metal;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.core.exception.ScrapFailedException;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Convert;
import home.holymiko.investment.scraper.app.server.scraper.source.Client;
import home.holymiko.investment.scraper.app.server.service.LinkService;
import home.holymiko.investment.scraper.app.server.service.ProductService;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.LinkDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.ProductCreateDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Product;
import home.holymiko.investment.scraper.app.server.type.enums.Availability;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Extract;
import home.holymiko.investment.scraper.app.server.core.LogBuilder;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.ProductDetailInterface;
import home.holymiko.investment.scraper.app.server.service.AppPropsService;
import home.holymiko.investment.scraper.app.server.service.PortfolioService;
import home.holymiko.investment.scraper.app.server.service.PriceService;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MetalScraper {
    private static final Logger LOGGER = LoggerFactory.getLogger(MetalScraper.class);

    private final int ETHICAL_DELAY;
    private final int LOG_INTERVAL;
    private final boolean SAVE_NEW_PRODUCTS_SEPARATELY;
    private final boolean SAVE_INCOMPLETE_PRODUCTS_AS_HIDDEN;

    private final LinkService linkService;
    private final PriceService priceService;
    private final PortfolioService portfolioService;
    private final ProductService productService;

    // Used for Polymorphic calling
    private final Map<Dealer, ProductDetailInterface> dealerToMetalAdapter = new HashMap<>();


    @Autowired
    public MetalScraper(
            AppPropsService appPropsService,
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
        ETHICAL_DELAY = Integer.parseInt(appPropsService.getAppProperty("scraper.ethicaldelay"));
        LOG_INTERVAL = Integer.parseInt(appPropsService.getAppProperty("scraper.log.interval"));
        SAVE_NEW_PRODUCTS_SEPARATELY = Boolean.parseBoolean(appPropsService.getAppProperty("scraper.newproducts.saveseparately"));
        SAVE_INCOMPLETE_PRODUCTS_AS_HIDDEN = Boolean.parseBoolean(appPropsService.getAppProperty("scraper.newproducts.saveincomplete.hidden"));
    }

    public void addAdapter(Dealer dealer, ProductDetailInterface adapter) {
        dealerToMetalAdapter.put(dealer, adapter);
    }

    ////////////// PRODUCT
    /////// PUBLIC

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    public void generalScrapAndSleep(List<LinkDTO> links, boolean saveHistory) {
        final int before = links.size();
        int counter = 0;

        // Filter products without Adapter ON
        links = links.stream().filter(x -> dealerToMetalAdapter.containsKey(x.getDealer())).collect(Collectors.toList());
        LOGGER.info(".generalScrapAndSleep() - Filter products with OFF Adapter - {} -> {}", before, links.size());

        for (LinkDTO link : links) {
            long startTime = System.nanoTime();

            // Main method for scraping a document
            generalScrap(link, saveHistory);

            // TODO Logging
            LogBuilder.statusPrint(LOG_INTERVAL, links.size(), counter++);
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
    public void generalInSyncScrapAndSleep(List<List<LinkDTO>> linksGroupByProduct, boolean saveHistory) {
        final int before = linksGroupByProduct.size();
        int counter = 0;

        // Filter products without Adapter ON
        linksGroupByProduct = linksGroupByProduct.stream().map(productLinks ->
                productLinks.stream().filter(x -> dealerToMetalAdapter.containsKey(x.getDealer())).collect(Collectors.toList())
        ).filter(x -> !x.isEmpty()).collect(Collectors.toList());
        LOGGER.info(".generalInSyncScrapAndSleep() - Filter products with OFF Adapter - {} -> {}", before, linksGroupByProduct.size());

        for (List<LinkDTO> productLinks : linksGroupByProduct) {
            long startTime = System.nanoTime();

            // TODO Make multithreading here
            // Main method for scraping a document
            productLinks.forEach(linkDTO -> generalScrap(linkDTO, saveHistory));

            // TODO Logging
            LogBuilder.statusPrint(LOG_INTERVAL, linksGroupByProduct.size(), counter++);
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

        generalScrapAndSleep( new ArrayList<>(linkSet), true );
        LogBuilder.logTimeStamp();
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
    private void productScrap(LinkDTO link, boolean saveHistory) throws ResourceNotFoundException, ScrapFailedException, DataIntegrityViolationException {
        String name = "";
        final HtmlPage page;
        final ProductCreateDTO productExtracted;
        final ProductDetailInterface adapter = dealerToMetalAdapter.get(link.getDealer());

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
        productExtracted = Extract.productAggregateExtract(name);
        if(productExtracted.isHidden()) {
            if(SAVE_INCOMPLETE_PRODUCTS_AS_HIDDEN) {
                LOGGER.warn("Extraction: "+name+" will be saved as hidden, due to missing parameters");
            } else {
                throw new ScrapFailedException("Extraction ERROR: "+name +" "+link.getUri());
            }
        }

        productSaveSwitch(link, productExtracted, saveHistory);
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
    private void productSaveSwitch(LinkDTO link, ProductCreateDTO productExtracted, boolean saveHistory) {
        final List<Product> nonSpecialProducts;

        // Special products are saved separately
        if(SAVE_NEW_PRODUCTS_SEPARATELY) {
            saveValidNewProductAndScrapPrice(link, productExtracted, saveHistory);
            return;
        }

        // Number of product for given extracted params should be within 0-1
        // Products from different Dealers with same params are supposed to be merged into one
        nonSpecialProducts = productService.findByParams(null, productExtracted);

        // New product saved
        if(nonSpecialProducts.isEmpty()) {
            saveValidNewProductAndScrapPrice(link, productExtracted, saveHistory);
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
                        "   Found:     " + productFound + "\n"
                );
            }

            link = linkService.updateLinkProductId(link.getId(), productFound.getId(), productExtracted.getName());
            priceScrap(link, saveHistory);
            return;
        }

        throw new DataIntegrityViolationException(
                "ERROR: Multiple Products for following params present in DB\n" + productExtracted
        );
    }

    /** Method meant to be used ONLY in productSaveSwitch
     * @param linkDTO
     * @param productExtracted
     */
    private void saveValidNewProductAndScrapPrice(LinkDTO linkDTO, ProductCreateDTO productExtracted, boolean saveHistory) {
        Product p = productService.save(productExtracted);
        linkDTO = linkService.updateLinkProductId(linkDTO.getId(), p.getId(), p.getName());
        priceScrap(linkDTO, saveHistory);
        LOGGER.info("New Product saved");
    }

    /**
     * Main method for scraping a document
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    private void generalScrap(final LinkDTO link, boolean saveHistory) {
        // Product is already assigned to Link -> update PricePair
        if (link.getProductId() != null) {
            priceScrap(link, saveHistory);
            return;
        }

        try {
            productScrap(link, saveHistory);
        } catch (Exception e) {
            LOGGER.warn( e.getMessage() );
        }
    }

    ////////////// PRICE
    /////// PRIVATE

    /**
     * Main Price scraping method
     * Scraps new price. Saves entities Price, PricePair and PricePairHistory. Updates relations.
     */
    private void priceScrap(final LinkDTO linkDTO, boolean saveHistory) {
        Double buy = null;
        Double sell = null;
        final ProductDetailInterface adapter = dealerToMetalAdapter.get(linkDTO.getDealer());
        final HtmlPage productDetailPage;
        String availabilityMsg = null;
        Availability availability = null;

        if(adapter == null) {
            throw new ScrapFailedException("ProductScrap - Scraper for dealer "+linkDTO.getDealer()+" not found");
        }
        try {
            productDetailPage = adapter.getPage(linkDTO.getUri());
        } catch (ResourceNotFoundException e) {
            // TODO Handle this & Log this
            return;
        }

        try{
            availabilityMsg = adapter.scrapAvailabilityFromProductPage(productDetailPage);
            try {
                availability = Convert.availability(availabilityMsg);
            } catch (NullPointerException | IllegalArgumentException e) {
                LOGGER.error("{} - {}", linkDTO.getDealer(), e.getMessage());
            }
        } catch (NotImplementedException | NullPointerException e) {
            LOGGER.error("{} - {}", linkDTO.getDealer(), e.getMessage());
        }

        try {
            // Choose MetalScraperInterface & scrap buy price
            buy = adapter.scrapBuyPriceFromProductPage(productDetailPage);
        } catch (Exception e) {
            LOGGER.error("{} - {}", linkDTO.getDealer(), e.getMessage());
            buy = 0.0;
        }
        if(buy.intValue() == 0) {
            LOGGER.warn("Kupni cena = 0");
        }

        try {
            sell = adapter.scrapSellPriceFromProductPage(productDetailPage);
        } catch (Exception e) {
            LOGGER.error("{} - {}", linkDTO.getDealer(), e.getMessage());
            sell = 0.0;
        }
        if(sell.intValue() == 0) {
            LOGGER.warn("Vykupni cena = 0");
        }

        this.priceService.savePriceAndUpdateLink(linkDTO.getId(), buy, sell, availability, availabilityMsg, saveHistory);
        LOGGER.info("New pricePair saved - " + linkDTO.getUri());
    }

    /**
     * TODO Method has to be tested
     */
    @Deprecated
    private void redemptionScrap(final Metal metal, final Dealer dealer) {
//        LOGGER.info("Redemption Scrap");
//        if( dealerToMetalAdapter.get(dealer) instanceof SellPriceListInterface) {
//            LOGGER.info("Scraping Redemption List available");
//            List<Pair<String, Double>> nameRedemptionMap = ((SellPriceListInterface) dealerToMetalAdapter.get(dealer)).scrapBuyOutFromList();
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
    public void scrapProductByIdList(final List<Long> productIds, boolean saveHistory) {
        for (long productId : productIds) {
            // Scraps new price for this.dealer product link
            linkService.findByProductId(productId)
                    .forEach((linkDTO) -> priceScrap(linkDTO, saveHistory));
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
        for (ProductDetailInterface scraperInterface : dealerToMetalAdapter.values()) {
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
