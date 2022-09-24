package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapFailedException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.parser.Extract;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.parser.Convert;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SilverumMetalClient extends Client implements MetalClientInterface {
    private static final String BASE = "https://www.silverum.cz/";
    private static final String SEARCH_URL_GOLD_COIN = "https://www.silverum.cz/investicni-mince.html";
    private static final String SEARCH_URL_GOLD_BAR = "https://www.silverum.cz/investicni-slitky.html";
    private static final String SEARCH_URL_GOLD_BRICK = "https://www.silverum.cz/investicni-cihly.html";

    // THIS won't work from list, because of missing weight
    private static final String SEARCH_URL_SILVER_COIN_CNB = "https://www.silverum.cz/mince-cnb.html";
    private static final String SEARCH_URL_SILVER_COIN_21 = "https://www.silverum.cz/numismatika-do-2021.html";
    private static final String SEARCH_URL_SILVER_COIN_22 = "https://www.silverum.cz/numismatika-2022.html";
    private static final String SEARCH_URL_SILVER_BAR = "https://www.silverum.cz/investicni-slitky-cs.html";
    private static final String SEARCH_URL_SILVER_BRICK = "https://www.silverum.cz/investicni-cihly-cs.html";

    private static final String X_PATH_PRODUCT_LIST = ".//div[@class='productItem']";
    private static final String X_PATH_PRODUCT_NAME = ".//h1[@class='title f600']";
    private static final String X_PATH_PRODUCT_LIST_NAME = ".//h3[@class='title f600']";

    private static final String X_PATH_BUY_PRICE = ".//strong[@class='pricePerItem']";
    private static final String X_PATH_BUY_PRICE_LIST = ".//div[@class='price f600']";
    private static final String X_PATH_REDEMPTION_PRICE = "";

    private static final String X_PATH_BID_SPOT_GOLD = "/html/body/section[1]/div/div/div[1]/div/div/div[2]/div";
    private static final String X_PATH_BID_SPOT_SILVER  = "/html/body/section[1]/div/div/div[2]/div/div/div[2]/div";
    private static final String X_PATH_BID_SPOT_DOLLAR = "/html/body/section[1]/div/div/div[4]/div/div/div[2]/div";

    public SilverumMetalClient() {
        super();
    }

    /////// LINK

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }
    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        List<Link> elements = new ArrayList<>();
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_GOLD_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_GOLD_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_GOLD_BRICK)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_SILVER_COIN_21)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_SILVER_COIN_22)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_SILVER_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_SILVER_BRICK)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        return elements;
    }

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLink(elementProduct, "./a", Dealer.SILVERUM, BASE);
    }


    /////// PRODUCT

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }


    /////// PRICE

    @Override
    public double scrapPriceFromProductPage(HtmlPage productDetailPage) {
        String x = scrapPriceFromProductPage(productDetailPage, X_PATH_BUY_PRICE);
        if(Pattern.compile("\\d+,\\d+ Kč bez DPH").matcher(x).find()) {
            x = x.split("\\d*[ ]?\\d+,\\d+ Kč bez DPH")[0];
        }

        return Convert.currencyToNumberConvert(x);
    }

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     */
    @Override
    public String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];
    }

    /**
     * Computed according to https://www.silverum.cz/zpetny-vykup.html
     * TODO Call to Silverum and double-check the buyout computation is correct
     * @param page
     * @return
     */
    @Override
    public double scrapRedemptionPrice(HtmlPage page) {
        final String productName = scrapNameFromProductPage(page).toLowerCase();
        final Metal metal = Extract.metalExtract(productName);
        final double weight = Extract.weightExtract(productName) / Extract.TROY_OUNCE;
        final Double dollar = Convert.currencyToNumberConvert(
                ((HtmlElement) page.getFirstByXPath(X_PATH_BID_SPOT_DOLLAR)).asText()
        );
        String metalDollarSpotTxt;
        double q = 1;

        if(metal == Metal.GOLD) {
            metalDollarSpotTxt = ((HtmlElement) page.getFirstByXPath(X_PATH_BID_SPOT_GOLD)).asText();
            if(weight > Extract.TROY_OUNCE) {
                q = 0.97;
            }
        } else if(metal == Metal.SILVER) {
            metalDollarSpotTxt = ((HtmlElement) page.getFirstByXPath(X_PATH_BID_SPOT_SILVER)).asText();
            // American Silver Eagle is +2,5%
            if(productName.contains("american") && productName.contains("eagle")) {
                q = 1.025;
            }
            if(weight > 100 * Extract.TROY_OUNCE) {
                q = 0.93;
            }
        } else {
            throw new ScrapFailedException("ERROR: Silverum unexpected product");
        }

        return Convert.currencyToNumberConvert( metalDollarSpotTxt ) * dollar * weight * q;
    }
}
