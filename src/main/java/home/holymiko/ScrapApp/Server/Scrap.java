package home.holymiko.ScrapApp.Server;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.ScrapApp.Server.Entity.*;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private static final long DELAY = 800;
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

    public void scrapProduct(Link link) {
        List<Product> productList = this.productService.findByLink(link.getLink());

        if (productList.isEmpty()) {
            double weight;
            Product product;

            loadPage(link.getLink());
            HtmlElement name = page.getFirstByXPath(".//span[@class='base']");
            weight = weightExtracter(name.asText());

            if (name.asText().contains("Zlat"))
                product = new Product(Metal.GOLD, name.asText(), weight, link, null, new ArrayList<>());
            else if (name.asText().contains("Stříbr"))
                product = new Product(Metal.SILVER, name.asText(), weight, link, null, new ArrayList<>());
            else if (name.asText().contains("Platin"))
                product = new Product(Metal.PLATINUM, name.asText(), weight, link, null, new ArrayList<>());
            else if (name.asText().contains("Pallad"))
                product = new Product(Metal.PALLADIUM, name.asText(), weight, link, null, new ArrayList<>());
            else
                product = new Product(null, name.asText(), weight, link, null, new ArrayList<>());

            addPriceToProduct(product, getPriceFromPage(weight));
            System.out.println("Product saved");
        } else {
            if (productList.size() > 1)
                System.out.println("WARNING - More products with same link");
            scrapPrice(productList.get(0));
        }
    }   // Saves new product or Updates price of existing

    public void scrapAllProducts() {
        List<Link> allLinks = linkService.findAll();
        int i = 0;
        for (Link link : allLinks) {
            scrapProduct(link);
            i++;
            if ((i % 10) == 0)
                System.out.println(i + "/" + allLinks.size());
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
        System.out.println("All products scraped");
    }        // Scraps products based on Links from DB

    public void scrapPortfolioProducts(long portfolioId) throws ResponseStatusException {
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
                scrapProduct(link);
            }
            try {
                Thread.sleep(DELAY);
            } catch (Exception e) {
                e.printStackTrace();
            }
            portfolioService.update(portfolioId);
            System.out.println(">> " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(new Date()) + " <<");
            throw new ResponseStatusException(HttpStatus.OK);
        } else
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No portfolio with such ID");
//            System.out.println("No portfolio with such ID");

    }

    public void scrapPrice(Product product) {
        loadPage(product.getLink().getLink());
        double weight = product.getGrams();
        addPriceToProduct(product, getPriceFromPage(weight));
        System.out.println("> New price saved");
    }

    public void scrapPricesByMetal(Metal metal){
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

    ///////////////////// Link scraping //////////////////////////

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
            HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//strong[@class='product name product-item-name']/a");
            Link link = new Link(itemAnchor.getHrefAttribute());

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
        List<Link> allLinks = linkService.findAll();
        System.out.println("Number of links: " + allLinks.size());
    }

    public void scrapAllLinks() {
        scrapLinks(searchUrlGold);
        scrapLinks(searchUrlSilver);
        scrapLinks(searchUrlPlatinum);
        scrapLinks(searchUrlPalladium);
    }

    public void scrapGoldLinks() {
        scrapLinks(searchUrlGold);
    }

    public void scrapSilverLinks() {
        scrapLinks(searchUrlSilver);
    }

    public void scrapPlatinumLinks() {
        scrapLinks(searchUrlPlatinum);
    }

    public void scrapPalladiumLinks() {
        scrapLinks(searchUrlPalladium);
    }

    //////////////////// UTILS ////////////////////////////

    private double weightExtracter(String name) {
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

    private Price getPriceFromPage(double weight) {
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
        return new Price(LocalDateTime.now(), Double.parseDouble(buyPrice), Double.parseDouble(redPrice), weight);
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
