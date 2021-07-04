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

    private static final long DELAY = 700;
    private static final double OUNCE = 28.349523125;
    private static final double TROY_OUNCE = 31.1034768;
    private final String xPathProductList;

    public ScrapMetal(Dealer dealer, LinkService linkService, PriceService priceService, PortfolioService portfolioService, ProductService productService, InvestmentService investmentService, String xPathProductList) {
        super();
        this.dealer = dealer;
        this.linkService = linkService;
        this.priceService = priceService;
        this.portfolioService = portfolioService;
        this.productService = productService;
        this.investmentService = investmentService;
        this.xPathProductList = xPathProductList;
    }

    /////// PRODUCT

    protected void productByLink(Link link) {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
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
            priceByProduct(productList.get(0));
        }
    }

    /**
     * Performs ethical delay.
     * Prints to console.
     * @param links Optional links of Products
     */
    public void productsByLinks(List<Link> links) {
        int i = 0;
        for (Link link : links) {
            productByOptionalLink(link);
            i++;
            if ((i % 10) == 0)
                System.out.println(i + "/" + links.size());
            sleep(DELAY);
        }
    }

    public void allProducts() {
        productsByDealer(Dealer.BESSERGOLD);

        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        System.out.println("All products scraped");
    }        // Scraps products based on Links from DB

    public void productsByDealer(Dealer dealer) {
        List<Link> dealerLinks = linkService.findByDealer(dealer);
        //// TODO Make new dealer or find existing
        productsByLinks(dealerLinks);
    }

    public void productsByPortfolio(long portfolioId) throws ResponseStatusException {
        System.out.println("ScrapMetal Portfolio-Products");
        Optional<Portfolio> optionalPortfolio = portfolioService.findById(portfolioId);
        if (optionalPortfolio.isPresent()) {
            // Set prevents one Product to be scrapped more times
            Set<Link> linkSet = optionalPortfolio.get().getInvestments()
                    .stream()
                    .map(investment -> investment.getProduct().getLink())
                    .collect(Collectors.toSet());

            productsByLinks(new ArrayList<>(linkSet));
            portfolioService.refresh(portfolioId); 
            System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
        }
    }


    /////// PRICE

    protected Price priceByProduct(Product product) {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    public void pricesByMetal(Metal metal){
        System.out.println("ScrapMetal pricesByMetal");
        List<Product> productList = this.productService.findByMetal(metal);
        int i = 0;
        for (Product product : productList) {
            priceByProduct(product);
            i++;
            if ((i % 10) == 0)
                System.out.println(i + "/" + productList.size());
            sleep(DELAY);
        }
        System.out.println(metal+" prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }

    public void pricesByProductIds(List<Long> productIds){
        System.out.println("ScrapMetal pricesByProductIds");
        int i = 0;
        for (Long productId : productIds) {
            Optional<Product> optionalProduct = this.productService.findById(productId);
            if(optionalProduct.isEmpty()){
                i++;
                continue;
            }
            priceByProduct(optionalProduct.get());

            i++;
            if ((i % 10) == 0) {
                System.out.println(i + "/" + productIds.size());
            }
            sleep(DELAY);
        }
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

        List<HtmlElement> item = page.getByXPath(xPathProductList);
        if (item.isEmpty()) {
            System.out.println("No products here");
            return;
        } else {
            System.out.println(item.size()+" HTMLElements to scrap");
        }

        for (HtmlElement htmlItem : item) {
            scrapLink(htmlItem, searchUrl);
        }

        System.out.println("Number of links: " + linkService.findAll().size());
    }

    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    protected void sAllLinks() {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    protected void sGoldLinks() {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    protected void sSilverLinks() {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    protected void sPlatinumLinks() {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
    }

    protected void sPalladiumLinks() {
        throw new AbstractMethodError("Abstract method is supposed to be overwritten");
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
        } else {
            if (text.contains("kanada")){
                return Producer.ROYAL_CANADIAN_MINT;
            } else if (text.contains("austrálie")) {
                return Producer.PERTH_MINT;
            } else if (text.contains("usa") || text.contains("american")) {
                return Producer.UNITED_STATES_MINT;
            }
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
//            Pattern pattern = Pattern.compile("\\d*\\.?x?,?\\d+?g");
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

}
