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
import java.util.regex.Matcher;
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

    private static final long DELAY = 700;
    private static final double TROY_OUNCE = 31.1034768;
    private static final double OUNCE = 28.349523125;

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

    protected void productByLink(Link link) {
        Product product;

        loadPage(link.getLink());
        HtmlElement htmlName = page.getFirstByXPath(xPathProductName);
        String name = htmlName.asText();
        double weight = weightExtractor(name);
        Form form = formExtractor(name);
        Producer producer = producerExtractor(name);

        if (name.contains("Zlat")) {
            product = new Product(producer, form, Metal.GOLD, name, weight, link, null, new ArrayList<>());
        } else if (name.contains("Stříbr")) {
            product = new Product(producer, form, Metal.SILVER, name, weight, link, null, new ArrayList<>());
        } else if (name.contains("Platin")) {
            product = new Product(producer, form, Metal.PLATINUM, name, weight, link, null, new ArrayList<>());
        } else if (name.contains("Pallad")) {
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
    protected void productByOptionalLink(Link link) {
        List<Product> productList = this.productService.findByLink(link.getLink());

        if (productList.isEmpty()) {
            productByLink(link);
        } else {
            if (productList.size() > 1) {
                System.out.println("WARNING - More products with same link");
            }
            priceByProductScrap(productList.get(0));
        }
    }

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    public void productsByOptionalLinks(List<Link> links) {
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

        Double buyPrice = 0.0;
        Double redemptionPrice = 0.0;
        try {
            buyPrice = formatPrice(((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText());
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
//            e.printStackTrace();
        }
        try {
            redemptionPrice = formatPrice(hmtlRedemptionPriceToText(page.getFirstByXPath(xPathRedemptionPrice)));
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
     * @param metal
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
        if (elements.isEmpty()) {
            System.out.println("No products here");
            return;
        }
        System.out.println(elements.size()+" HTMLElements to scrap");

        elements.forEach( element -> scrapLink(element, searchUrl) );

        System.out.println("Number of links: " + linkService.findAll().size());
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


    /////// EXTRACTOR

    /**
     * Extracts Producer from text
     * @param text including producer's name
     * @return Enum class Producer
     */
    protected Producer producerExtractor(String text) {
        text = text.toLowerCase();
        if (text.contains("perth") || text.contains("rok") || text.contains("kangaroo") || text.contains("kookaburra") || text.contains("koala")) {
            return Producer.PERTH_MINT;
        } else if(text.contains("argor")) {
            return Producer.ARGOR_HERAEUS;
        } else if (text.contains("heraeus")) {
            return Producer.HERAEUS;
        } else if (text.contains("münze") || text.contains("wiener philharmoniker")) {
            return Producer.MUNZE_OSTERREICH;
        } else if (text.contains("rand") || text.contains("krugerrand")) {
            return Producer.SOUTH_AFRICAN_MINT;
        } else if (text.contains("pamp")) {
            return Producer.PAMP;
        } else if (text.contains("valcambi")) {
            return Producer.VALCAMBI;
        } else if (text.contains("royal canadian mint") || text.contains("maple leaf") || text.contains("moose") || text.contains("golden eagle")) {
            return Producer.ROYAL_CANADIAN_MINT;
        } else if (text.contains("panda čína")) {
            return Producer.CHINA_MINT;
        } else if (text.contains("american eagle")) {
            return Producer.UNITED_STATES_MINT;
        } else if (text.contains("britannia") || text.contains("sovereign elizabeth")) {
            return Producer.ROYAL_MINT_UK;
        } else if (text.contains("libertad") || text.contains("mexico") || text.contains("mexiko")) {
            return Producer.MEXICO_MINT;
        } else if (text.contains("slon") ) {
            return Producer.BAVARIAN_STATE_MINT;
        } else if (text.contains("noble isle of man") ) {
            return Producer.POBJOY_MINT;
        } else if (text.contains("kanada")){
            return Producer.ROYAL_CANADIAN_MINT;
        } else if (text.contains("austrálie")) {
            return Producer.PERTH_MINT;
        } else if (text.contains("usa") || text.contains("american")) {
            return Producer.UNITED_STATES_MINT;
        }

        return Producer.UNKNOWN;
    }

    /**
     * Extracts Form from text
     * @param text including name of form
     * @return Enum class Form
     */
    protected Form formExtractor(String text) {
        text = text.toLowerCase();
        if(text.contains("mince") || text.contains("coin")){
            return Form.COIN;
        }
        if(text.contains("bar") || text.contains("slitek")){
            return Form.BAR;
        }
        return Form.UNKNOWN;
    }

    /**
     * Extracts weight from various patterns
     * @param text including number with unit
     * @return Grams
     */
    protected double weightExtractor(String text) {
//        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.toLowerCase(Locale.ROOT);

        Pattern pattern = Pattern.compile("\\d+x\\d+g");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Integer.parseInt(s.split("x")[0]) * Integer.parseInt(s.split("x")[1]);
        }

        pattern = Pattern.compile("\\d+\\.\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+,\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            s = s.replace(",", ".");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+ g");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" g", "");
            return Double.parseDouble(s);
        }

        pattern = Pattern.compile("\\d+\\/\\d+ oz");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" oz", "");
            return Double.parseDouble(s.split("/")[0]) / Double.parseDouble(s.split("/")[1]) * TROY_OUNCE;
        }

        pattern = Pattern.compile("\\d+ oz");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" oz", "");
            return Double.parseDouble(s) * TROY_OUNCE;
        }

        pattern = Pattern.compile("\\d+ kg");
        matcher = pattern.matcher(text);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" kg", "");
            return Double.parseDouble(s) * 1000;
        }

        return -1;
    }


    /////// FILTER

    protected boolean linkFilter(String link) {
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

    protected void linkFilterWrapper(Link link) {
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
     * Extracts price from text format.
     * @param text text including only number and currency
     * @return price from text
     */
    protected Double formatPrice(String text) {
        text = text.replace("\u00a0", "");         // &nbsp;
        text = text.replace(",", ".");             // -> Double
        text = text.replace("Kč", "");
        return Double.parseDouble( text.replace(" ", "") );
    }

    protected void addPriceToProduct(Product product, Price price) {
        List<Price> priceList = product.getPrices();
        this.priceService.save(price);
        priceList.add(price);
        product.setLatestPrice(price);
        product.setPrices(priceList);
        this.productService.save(product);
    }

    protected String hmtlRedemptionPriceToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText();
    }

}
