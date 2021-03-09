package home.holymiko.ScrapApp.Server;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class Scrap {
    private final LinkService linkService;
    private final PriceService priceService;
    private final PortfolioService portfolioService;
    private final ProductService productService;
    private final InvestmentService investmentService;

    private HtmlPage page;
    private final WebClient client;

    private static final long DELAY = 700;
    private static final double OUNCE = 28.349523125;
    private static final double TROY_OUNCE = 31.1034768;

    private static final String searchUrlGold = "https://www.bessergold.cz/investicni-zlato.html?product_list_limit=all";
    private static final String searchUrlSilver = "https://www.bessergold.cz/investicni-stribro.html?product_list_limit=all";
    private static final String searchUrlPlatinum = "https://www.bessergold.cz/investicni-platina.html?product_list_limit=all";
    private static final String searchUrlPalladium = "https://www.bessergold.cz/investicni-palladium.html?product_list_limit=all";

    @Autowired
    public Scrap(LinkService linkService,
                 PriceService priceService,
                 ProductService productService,
                 PortfolioService portfolioService,
                 InvestmentService investmentService) {
        this.linkService = linkService;
        this.priceService = priceService;
        this.productService = productService;
        this.portfolioService = portfolioService;
        this.investmentService = investmentService;
        client = new WebClient();
        client.getOptions().setJavaScriptEnabled(false);
        client.getOptions().setCssEnabled(false);
    }


    /////// PRODUCT

    private void scrapProduct(Link link) {
        double weight;
        Product product;

        loadPage(link.getLink());
        HtmlElement name = page.getFirstByXPath(".//span[@class='base']");
        weight = weightExtractor(name.asText());
        Form form = formExtractor(name.asText());
        Producer producer = producerExtractor(name.asText());

        if (name.asText().contains("Zlat"))
            product = new Product( producer, form, Metal.GOLD, name.asText(), weight, link, null, new ArrayList<>() );
        else if (name.asText().contains("Stříbr"))
            product = new Product( producer, form, Metal.SILVER, name.asText(), weight, link, null, new ArrayList<>());
        else if (name.asText().contains("Platin"))
            product = new Product( producer, form, Metal.PLATINUM, name.asText(), weight, link, null, new ArrayList<>());
        else if (name.asText().contains("Pallad"))
            product = new Product( producer, form, Metal.PALLADIUM, name.asText(), weight, link, null, new ArrayList<>());
        else
            product = new Product( producer, form, Metal.UNKNOWN, name.asText(), weight, link, null, new ArrayList<>());

        addPriceToProduct(product, scrapPrice(product));
        System.out.println("Product saved");
    }

    public void sOptionalProduct(Link link) {
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
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
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
        System.out.println("Scrap Portfolio-Products");
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
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            portfolioService.update(portfolioId);
            System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
//            System.out.println("No portfolio with such ID");

    }


    /////// PRICE

    private Price scrapPrice(Product product) {
        loadPage(product.getLink().getLink());

        double weight = product.getGrams();
        HtmlElement htmlBuyPrice = page.getFirstByXPath(".//span[@class='price']");
        HtmlElement htmlRedemptionPrice = page.getFirstByXPath(".//div[@class='vykupni-cena']");

        String buyPrice = formatPrice(htmlBuyPrice.asText());
        String redPrice = htmlRedemptionPrice.asText();
        try {
            redPrice = redPrice.split(":")[1];          // Aktuální výkupní cena (bez DPH): xxxx,xx Kč
        } catch (Exception e) {
            e.printStackTrace();
        }
        redPrice = formatPrice(redPrice);
        if (Double.parseDouble(redPrice) <= 0.0)
            System.out.println("WARNING - Vykupni cena = 0");
        Price newPrice = new Price(LocalDateTime.now(), Double.parseDouble(buyPrice), Double.parseDouble(redPrice), weight);
        addPriceToProduct(product, newPrice);
        System.out.println("> New price saved");
        return newPrice;
    }

    public void sMetalPrices(Metal metal){
        System.out.println("Scrap Prices-Metal");
        List<Product> productList = this.productService.findByMetal(metal);
        int i = 0;
        for (Product product : productList) {
            scrapPrice(product);
            i++;
            if ((i % 10) == 0)
                System.out.println(i + "/" + productList.size());
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(metal+" prices scraped");
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
    }


    /////// LINK

    private void scrapLink(HtmlElement htmlItem, String searchUrl){
        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//strong[@class='product name product-item-name']/a");
        Link link;
        if(  searchUrl.equals(searchUrlGold) || searchUrl.equals(searchUrlSilver)
        ||   searchUrl.equals(searchUrlPlatinum) || searchUrl.equals(searchUrlPalladium) ) {
            link = new Link(Dealer.BESSERGOLD, itemAnchor.getHrefAttribute());
        } else {
            link = new Link(Dealer.UNKNOWN, itemAnchor.getHrefAttribute());
        }

        if (linkFilter(link.getLink())) {
            List<Link> linkList = linkService.findByLink(link.getLink());
            if (linkList.isEmpty()) {
                linkService.save(link);
                System.out.println("Link saved");
            } else {
                if (linkList.size() == 1)
                    System.out.println("Link already in DB");
                else
                    System.out.println("WARNING - Duplicates in DB table LINK");
            }
        }
    }

    public void scrapLinks(String searchUrl) {
        page = null;
        try {
            page = client.getPage(searchUrl);                                       // Product links from main page
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<HtmlElement> item = page.getByXPath("//li[@class='item product product-item']");
        if (item.isEmpty()) {
            System.out.println("No products here");
            return;
        }

        for (HtmlElement htmlItem : item) {
            scrapLink(htmlItem, searchUrl);
        }

        List<Link> allLinks = linkService.findAll();
        System.out.println("Number of links: " + allLinks.size());
    }

    public void sAllLinks() {
        scrapLinks(searchUrlGold);
        scrapLinks(searchUrlSilver);
        scrapLinks(searchUrlPlatinum);
        scrapLinks(searchUrlPalladium);
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


    /////// UTILS

    private Producer producerExtractor(String name) {
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

    private Form formExtractor(String name) {
        name = name.toLowerCase();
        if(name.contains("mince") || name.contains("coin")){
            return Form.COIN;
        }
        if(name.contains("bar") || name.contains("slitek")){
            return Form.BAR;
        }
        return Form.UNKNOWN;
    }

    private double weightExtractor(String name) {
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

    private boolean linkFilter(String link) {
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
        if (link.contains("slit") || link.contains("minc") || link.contains("lunarni-serie-rok"))
            return true;
        if (link.contains("goldbarren") || link.contains("krugerrand") || link.contains("sliek"))
            return true;
        System.out.println("Link vyřazen: " + link);
        return false;
    }

    private String formatPrice(String price) {
        price = price.replace("\u00a0", "");         // &nbsp;
        price = price.replace(",", ".");             // -> Double
        price = price.replace("Kč", "");
        return price.replace(" ", "");
    }

    public void addPriceToProduct(Product product, Price price) {
        List<Price> priceList = product.getPrices();
        this.priceService.save(price);
        priceList.add(price);
        product.setLatestPrice(price);
        product.setPrices(priceList);
        this.productService.save(product);
    }

    private void loadPage(String link){
        page = null;
        try {
            page = client.getPage(link);                // Product page
        } catch (Exception e) {
            e.printStackTrace();
        }
        assert page != null;
    }
}
