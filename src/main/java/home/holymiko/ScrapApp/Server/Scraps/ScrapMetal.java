package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
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

    protected void scrapProduct(Link link) {
        System.out.println("Error: This method should be overwritten");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    protected void sOptionalProduct(Link link) {
        List<Product> productList = this.productService.findByLink(link.getLink());

        if (productList.isEmpty()) {
            // TODO Add product to Avatar
            scrapProduct(link);
        } else {
            if (productList.size() > 1)
                System.out.println("WARNING - More products with same link");
            scrapPrice(productList.get(0));
        }
    }   // Saves new product or Updates price of existing

    public void sProducts(List<Link> links) {
        int i = 0;
        for (Link link : links) {
            sOptionalProduct(link);
            i++;
            if ((i % 10) == 0)
                System.out.println(i + "/" + links.size());
            sleep(DELAY);
        }
    }

    public void sAllProducts() {
        sDealerProducts(Dealer.BESSERGOLD);

        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        System.out.println("All products scraped");
    }        // Scraps products based on Links from DB

    public void sDealerProducts(Dealer dealer) {
        List<Link> dealerLinks = linkService.findByDealer(dealer);
        //// TODO Make new dealer or find existing
        sProducts(dealerLinks);
    }

    public void sPortfolioProducts(long portfolioId) throws ResponseStatusException {
        System.out.println("ScrapMetal Portfolio-Products");
        Optional<Portfolio> optionalPortfolio = portfolioService.findById(portfolioId);
        if (optionalPortfolio.isPresent()) {
            Portfolio portfolio = optionalPortfolio.get();
            List<Investment> investmentList = portfolio.getInvestments();
            Set<Link> linkSet = new HashSet<>();                        // Donts need to scrap same product more times
            for (Investment investment : investmentList) {
                linkSet.add(investment.getProduct().getLink());
            }
            for (Link link : linkSet) {
                sOptionalProduct(link);
            }
            sleep(DELAY);
            portfolioService.update(portfolioId);
            System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
//            System.out.println("No portfolio with such ID");

    }


    /////// PRICE

    protected Price scrapPrice(Product product) {
        System.out.println("Error: This method should be overwritten");
        throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public void sMetalPrices(Metal metal){
        System.out.println("ScrapMetal Prices-Metal");
        List<Product> productList = this.productService.findByMetal(metal);
        int i = 0;
        for (Product product : productList) {
            scrapPrice(product);
            i++;
            if ((i % 10) == 0)
                System.out.println(i + "/" + productList.size());
            sleep(DELAY);
        }
        System.out.println(metal+" prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }


    /////// LINK

    protected void scrapLink(HtmlElement htmlItem, String searchUrl){
        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//strong[@class='product name product-item-name']/a");
        Link link = new Link(dealer, itemAnchor.getHrefAttribute());
        linkFilterAction(link);
    }

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

    protected void sAllLinks() {
    }

    protected void sGoldLinks() {

    }

    protected void sSilverLinks() {

    }

    protected void sPlatinumLinks() {

    }

    protected void sPalladiumLinks() {

    }


    /////// EXTRACTOR

    protected Producer producerExtractor(String name) {
        name = name.toLowerCase();
        if (name.contains("perth") || name.contains("rok") || name.contains("kangaroo") || name.contains("kookaburra") || name.contains("koala")) {
            return Producer.PERTH_MINT;
        } else if(name.contains("argor")) {
            return Producer.ARGOR_HERAEUS;
        } else if (name.contains("heraeus")) {
            return Producer.HERAEUS;
        } else if (name.contains("münze") || name.contains("wiener philharmoniker")) {
            return Producer.MUNZE_OSTERREICH;
        } else if (name.contains("rand") || name.contains("krugerrand")) {
            return Producer.SOUTH_AFRICAN_MINT;
        } else if (name.contains("pamp")) {
            return Producer.PAMP;
        } else if (name.contains("valcambi")) {
            return Producer.VALCAMBI;
        } else if (name.contains("royal canadian mint") || name.contains("maple leaf") || name.contains("moose") || name.contains("golden eagle")) {
            return Producer.ROYAL_CANADIAN_MINT;
        } else if (name.contains("panda čína")) {
            return Producer.CHINA_MINT;
        } else if (name.contains("american eagle")) {
            return Producer.UNITED_STATES_MINT;
        } else if (name.contains("britannia") || name.contains("sovereign elizabeth")) {
            return Producer.ROYAL_MINT_UK;
        } else if (name.contains("libertad") || name.contains("mexico") || name.contains("mexiko")) {
            return Producer.MEXICO_MINT;
        } else if (name.contains("slon") ) {
            return Producer.BAVARIAN_STATE_MINT;
        } else if (name.contains("noble isle of man") ) {
            return Producer.POBJOY_MINT;
        } else {
            if (name.contains("kanada")){
                return Producer.ROYAL_CANADIAN_MINT;
            } else if (name.contains("austrálie")) {
                return Producer.PERTH_MINT;
            } else if (name.contains("usa") || name.contains("american")) {
                return Producer.UNITED_STATES_MINT;
            }
        }

        return Producer.UNKNOWN;
    }

    protected Form formExtractor(String name) {
        name = name.toLowerCase();
        if(name.contains("mince") || name.contains("coin")){
            return Form.COIN;
        }
        if(name.contains("bar") || name.contains("slitek")){
            return Form.BAR;
        }
        return Form.UNKNOWN;
    }

    protected double weightExtractor(String name) {
//            Pattern pattern = Pattern.compile("\\d*\\.?x?,?\\d+?g");
        Pattern pattern = Pattern.compile("\\d+x\\d+g");
        Matcher matcher = pattern.matcher(name);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Integer.parseInt(s.split("x")[0]) * Integer.parseInt(s.split("x")[1]);
        }
        pattern = Pattern.compile("\\d+\\.\\d+g");
        matcher = pattern.matcher(name);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }
        pattern = Pattern.compile("\\d+,\\d+g");
        matcher = pattern.matcher(name);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            s = s.replace(",", ".");
            return Double.parseDouble(s);
        }
        pattern = Pattern.compile("\\d+g");
        matcher = pattern.matcher(name);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }
        pattern = Pattern.compile("\\d+ g");
        matcher = pattern.matcher(name);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace("g", "");
            return Double.parseDouble(s);
        }
        pattern = Pattern.compile("\\d+\\/\\d+ oz");
        matcher = pattern.matcher(name);
        if (matcher.find()) {
            String s = matcher.group();
            s = s.replace(" oz", "");
            return Double.parseDouble(s.split("/")[0]) / Double.parseDouble(s.split("/")[1]) * TROY_OUNCE;
        }
        pattern = Pattern.compile("\\d+ oz");
        matcher = pattern.matcher(name);
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

    protected void linkFilterAction(Link link) {
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

    protected String formatPrice(String price) {
        price = price.replace("\u00a0", "");         // &nbsp;
        price = price.replace(",", ".");             // -> Double
        price = price.replace("Kč", "");
        return price.replace(" ", "");
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
