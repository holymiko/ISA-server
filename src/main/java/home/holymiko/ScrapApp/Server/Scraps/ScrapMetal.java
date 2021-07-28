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

    private static final long DELAY = 700;


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

    protected void productByLink(final Link link) {
        loadPage(link.getLink());

        Product product;
        final String name = ((HtmlElement) page.getFirstByXPath(xPathProductName)).asText();
        final String nameLowerCase = name.toLowerCase(Locale.ROOT);
        final double weight = Extractor.weightExtractor(name);
        final Form form = Extractor.formExtractor(name);
        final Producer producer = Extractor.producerExtractor(name);

        if (nameLowerCase.contains("zlat")) {
            product = new Product(producer, form, Metal.GOLD, name, weight, link, null, new ArrayList<>());
        } else if (nameLowerCase.contains("stříbr")) {
            product = new Product(producer, form, Metal.SILVER, name, weight, link, null, new ArrayList<>());
        } else if (nameLowerCase.contains("platin")) {
            product = new Product(producer, form, Metal.PLATINUM, name, weight, link, null, new ArrayList<>());
        } else if (nameLowerCase.contains("pallad")) {
            product = new Product(producer, form, Metal.PALLADIUM, name, weight, link, null, new ArrayList<>());
        } else {
            product = new Product(producer, form, Metal.UNKNOWN, name, weight, link, null, new ArrayList<>());
        }

        priceByProductScrap(product);
        System.out.println("Product saved");
    }

    /**
     * Switch: Save new product / Update price of existing product
     * @param link of Product
     */
    protected void productByOptionalLink(final Link link) {
        Optional<Product> optionalProduct = this.productService.findByLink(link.getLink());

        if (optionalProduct.isEmpty()) {
            productByLink(link);
            return;
        }
        priceByProductScrap(optionalProduct.get());
    }

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    public void productsByOptionalLinks(final List<Link> links) {
        for (Link link : links) {
            productByOptionalLink(link);
            printAndSleep(DELAY, links.size());
        }
        printerCounter = 0;
    }

    /**
     * Scraps products based on Links from DB
     */
    public void allProducts() {
        productsByDealer(Dealer.BESSERGOLD);
        productsByDealer(Dealer.ZLATAKY);
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        System.out.println("All products scraped");
    }

    public void productsByDealer(Dealer dealer) {
        productsByOptionalLinks( linkService.findByDealer(dealer) );
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
                .map(investment -> investment.getProduct().getLink())
                .collect(Collectors.toSet());

        productsByOptionalLinks( new ArrayList<>(linkSet) );
        portfolioService.refresh(portfolioId);
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }


    /////// PRICE

    /**
     * Scraps new price for already known product
     * @param product already saved in DB
     * @return new price
     */
    protected Price priceByProductScrap(Product product) {
        loadPage(product.getLink().getLink());

        double buyPrice = 0.0;
        double redemptionPrice = 0.0;
        try {
            buyPrice = Extractor.priceExtractor(((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText());
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
//            e.printStackTrace();
        }
        try {
            redemptionPrice = Extractor.priceExtractor(redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice)));
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
//            e.printStackTrace();
        }
        Price newPrice = new Price(LocalDateTime.now(), buyPrice, redemptionPrice, product.getGrams());
        addPriceToProduct(product, newPrice);
        System.out.println("> New price saved");
        return newPrice;
    }

    /**
     * Scraps prices by metal from all dealers
     * @param metal Enum
     */
    public void pricesByMetal(Metal metal){
        System.out.println("ScrapMetal pricesByMetal");
        List<Product> productList = this.productService.findByMetal(metal);
        for (Product product : productList) {
            priceByProductScrap(product);
            printAndSleep(DELAY, productList.size());
        }
        printerCounter = 0;
        System.out.println(metal+" prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }

    public void pricesByProductIds(List<Long> productIds){
        System.out.println("ScrapMetal pricesByProductIds");
        for (Long productId : productIds) {
            Optional<Product> optionalProduct = this.productService.findById(productId);
            if(optionalProduct.isEmpty()){
                printerCounter++;
                continue;
            }
            priceByProductScrap(optionalProduct.get());
            printAndSleep(DELAY, productIds.size());
        }
        printerCounter = 0;
        System.out.println("Prices scraped");
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

        elements.forEach( element -> scrapLink(element, searchUrl) );

        System.out.println("Total number of links: " + linkService.findAll().size());
    }

    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    public void sAllLinks() {
        sGoldLinks();
        sSilverLinks();
        sPlatinumLinks();
        sPalladiumLinks();
    }

    public void sGoldLinks() {
        scrapLinks(searchUrlGold);
    }

    public void sSilverLinks() {
        scrapLinks(searchUrlSilver);
    }

    public void sPlatinumLinks() {
        scrapLinks(searchUrlPlatinum);
    }

    public void sPalladiumLinks() {
        scrapLinks(searchUrlPalladium);
    }



    /////// FILTER

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
        if (link.contains("slit") || link.contains("minc") || link.contains("lunarni-serie-rok") || link.contains("tolar"))
            return true;
        if (link.contains("goldbarren") || link.contains("krugerrand") || link.contains("sliek"))
            return true;
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

    protected void addPriceToProduct(Product product, Price price) {
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
