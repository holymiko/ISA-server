package home.holymiko.InvestmentScraperApp.Server.Scraper;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapFailedException;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.LinkDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Mapper.LinkMapper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Extract;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.BessergoldDeMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.BessergoldMetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper.SilverumMetalScraper;
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
            LinkMapper linkMapper,
            ExchangeRateService exchangeRateService
    ) {
        super();
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.linkMapper = linkMapper;

        searchInter.put(Dealer.BESSERGOLD_CZ, new BessergoldMetalScraper());
        searchInter.put(
                Dealer.BESSERGOLD_DE,
                new BessergoldDeMetalScraper(
                        // Insert currency exchange rate for conversion to CZK
                        exchangeRateService.findFirstByCodeOrderByDateDesc("EUR").getExchangeRate()
                )
        );
        searchInter.put(Dealer.SILVERUM, new SilverumMetalScraper());
        searchInter.put(Dealer.ZLATAKY, new ZlatakyMetalScraper());
    }

    ////////////// PRODUCT
    /////// PUBLIC

    /**
     * Scraps products based on Links from DB
     */
    public void allProducts() {
        List<LinkDTO> linkDTOList = linkMapper.toDTO(linkService.findAll());
//        linkDTOList = linkDTOList.stream().filter(
//                linkDTO -> linkDTO.getProductId() == null
//        ).collect(Collectors.toList());
//        for (LinkDTO link:linkDTOList) {
//            productScrap(link);
////            System.out.println(link.toString());
//        }
        generalScrap( linkDTOList );
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
            name = searchInter.get(link.getDealer()).scrapProductName(page);
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

    private void productSaveSwitch(LinkDTO link, ProductCreateDTO productExtracted) {
        final List<Product> nonSpecialProducts;

        // Special products are saved separately
        if(productExtracted.isSpecial()) {
            newProduct(link, productExtracted);
            return;
        }

        // Number of product for given extracted params should be within 0-1
        // Products from different Dealers with same params are supposed to be merged into one
        nonSpecialProducts = productService.findByParams(null, productExtracted);

        // New product save
        if(nonSpecialProducts.isEmpty()) {
            newProduct(link, productExtracted);
            return;
        }

        // Product merging
        // Price & Link from another dealer will be added to product
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

            link = linkMapper.toDTO(
                    linkService.updateProduct(link.getId(), productFound)
            );
            priceScrap(link);
            return;
        }

        throw new DataIntegrityViolationException(
                "ERROR: Multiple Products for following params present in DB\n" + productExtracted.toString()
        );
    }

    private void newProduct(LinkDTO link, ProductCreateDTO productExtracted) {
        final Product productToSave = new Product(
                productExtracted.getName(),
                productExtracted.getProducer(),
                productExtracted.getForm(),
                productExtracted.getMetal(),
                productExtracted.getGrams(),
                productExtracted.getYear(),
                productExtracted.isSpecial()
        );
        productService.save(productToSave);
        link = linkMapper.toDTO(
                linkService.updateProduct(link.getId(), productToSave)
        );
        priceScrap(link);
        System.out.println(">> Product saved");
    }

    /**
     * Main method for scraping a document
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    private void generalScrap(final LinkDTO link) {
        // Product is already assigned to Link -> update Price
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
            Client.dynamicSleep(ETHICAL_DELAY, startTime);
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
        Double buyPrice = null;
        Double redemptionPrice = null;
        final Price price;

        HtmlPage productDetailPage = loadPage(link.getUrl());

        // TODO Double think about buy/redemption price data types + What to do if both are zero?
        try {
            buyPrice = searchInter.get(link.getDealer()).scrapBuyPrice(productDetailPage);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
        }
        redemptionPrice = searchInter.get(link.getDealer()).scrapRedemptionPrice(productDetailPage);

        price = new Price(LocalDateTime.now(), buyPrice, redemptionPrice, link.getDealer());
        this.priceService.save(price);
        this.productService.updatePrice(link.getProductId(), price);

        System.out.println("> New price saved - " + link.getUrl());

        // Sleep time is dynamic, according to time took by scrap procedure
        Client.dynamicSleep(ETHICAL_DELAY, startTime);
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

}
