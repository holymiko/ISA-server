package home.holymiko.InvestmentScraperApp.Server.Scraper.sources;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Mapper.LinkMapper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Extract;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.BessergoldMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.ZlatakyMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class MetalScraper extends Client {
    private final LinkService linkService;
    private final PriceService priceService;
    private final PortfolioService portfolioService;
    private final ProductService productService;
    private final LinkMapper linkMapper;

    // Used for Polymorphic calling
    private final Map< Dealer, MetalScraperInterface> searchInter = new HashMap<>();

    private static final long ETHICAL_DELAY = 700;
    private static final int PRINT_INTERVAL = 10;

    @Autowired
    public MetalScraper(
            LinkService linkService,
            PriceService priceService,
            PortfolioService portfolioService,
            ProductService productService,
            LinkMapper linkMapper
    ) {
        super();
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.linkMapper = linkMapper;

        searchInter.put(Dealer.BESSERGOLD_CZ, new BessergoldMetalScraper());
//        searchInter.put(Dealer.BESSERGOLD_DE, new BessergoldDeMetalScraper());
//        searchInter.put(Dealer.SILVERUM, new SilverumMetalScraper());
        searchInter.put(Dealer.ZLATAKY, new ZlatakyMetalScraper());
    }

    ////////////// PRODUCT
    /////// PUBLIC

    /**
     * Scraps products based on Links from DB
     */
    public void allProducts() {
        generalScrap( linkMapper.toDTO(linkService.findAll()) );
        ConsolePrinter.printTimeStamp();
        System.out.println("All products scraped");
    }

    public void scrapByParam(Dealer dealer, Metal metal, Form form, String url) {
        generalScrap( linkMapper.toDTO(linkService.findByParams(dealer, metal, form, url)) );
    }

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
                        investment -> linkService.findByProductId( investment.getProductDTO().getId())
                ).flatMap(
                        List::stream
                ).map(
                        linkMapper::toDTO
                ).collect(
                        Collectors.toSet()
                );

        generalScrap( new ArrayList<>(linkSet) );
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

        HtmlPage page = loadPage(link.getUrl());

        // Sends request for name of the Product. Prepares name for extraction (Extract methods)
        try {
            name = searchInter.get(link.getDealer()).scrapProductName(page);
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
     * Main method for scraping a document
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    private void generalScrap(final LinkDTO link) {
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
    private void generalScrap(final List<LinkDTO> links) {
        int counter = 0;
        for (LinkDTO link : links) {
            long startTime = System.nanoTime();

            // Main method for scraping a document
            generalScrap(link);

            // Sleep time is dynamic, according to time took by scrap procedure
            ConsolePrinter.statusPrint(PRINT_INTERVAL, links.size(), counter++);
            dynamicSleep(ETHICAL_DELAY, startTime);
        }
    }


    ////////////// PRICE
    /////// PRIVATE

    /**
     * Scraps new price for already known product
     * @param link Link from which the price gonna be scrapped
     */
    private void priceScrap(final LinkDTO link) {
        long startTime = System.nanoTime();
        double buyPrice;
        double redemptionPrice;
        final Price price;

        HtmlPage page = loadPage(link.getUrl());

        buyPrice = searchInter.get(link.getDealer()).scrapBuyPrice(page);
        redemptionPrice = searchInter.get(link.getDealer()).scrapRedemptionPrice(page);

        price = new Price(LocalDateTime.now(), buyPrice, redemptionPrice, link.getDealer());
        this.priceService.save(price);
        this.productService.updatePrice(link.getProductId(), price);

        System.out.println("> New price saved - " + link.getUrl());

        // Sleep time is dynamic, according to time took by scrap procedure
        dynamicSleep(ETHICAL_DELAY, startTime);
    }

    public void scrapProductByIdList(final List<Long> productIds) {
        for (long productId : productIds) {
            // Scraps new price for this.dealer product link
            linkService.findByProductId(productId)
                    .forEach(
                            link -> priceScrap(linkMapper.toDTO(link))
                    );
        }
    }


    ///////////////////// LINK
    /////// PUBLIC

    public void allLinksScrap() {
        // Polymorphic call
        searchInter.values().forEach(
                scraperInterface -> scraperInterface.scrapAllLinks(client)
                        .forEach(
                                this::linkFilterWrapper
                        )
        );
    }


    ////////////// FILTER

    /**
     * Filtration based on text of the link
     * @param link Link about to be filtered
     * @return False for link being filtered
     */
    private static boolean linkFilter(String link) {
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

    private void linkFilterWrapper(final Link link) {
        if ( link == null || !linkFilter( link.getUrl() ) ) {
            return;
        }

        try {
            linkService.save(link);
            System.out.println("Link saved");
        } catch (Exception e) {
            System.out.println( e.getMessage() );
        }
    }

    /////// PRIVATE

    private static boolean isNameSpecial(String name) {
        name = name.toLowerCase(Locale.ROOT);
        return name.contains("lunární") || name.contains("výročí") || name.contains("rush") || name.contains("horečka");
        // TODO Add attribute type to Product
    }

}
