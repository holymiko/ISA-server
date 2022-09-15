package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraperInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Class is deprecated, because buyOut (redemption) scraping is missing.
 * Only golden products are scraped. Silver products are not activated.
 * Apart from that, class works correctly a can be used
 */
@Deprecated
public class SilverumMetalScraper extends Client implements MetalScraperInterface {
    private static final String BASE = "https://www.silverum.cz/";
    private static final String SEARCH_URL_GOLD_COIN = "https://www.silverum.cz/investicni-mince.html";
    private static final String SEARCH_URL_GOLD_BAR = "https://www.silverum.cz/investicni-slitky.html";
    private static final String SEARCH_URL_GOLD_BRICK = "https://www.silverum.cz/investicni-cihly.html";

    // THIS won't work from list, because of missing weight
    private static final String SEARCH_URL_SILVER_COIN_CNB = "https://www.silverum.cz/mince-cnb.html";

    private static final String SEARCH_URL_SILVER_BAR = "https://www.silverum.cz/investicni-slitky-cs.html";
    private static final String SEARCH_URL_SILVER_BRICK = "https://www.silverum.cz/investicni-cihly-cs.html";

    private static final String X_PATH_PRODUCT_LIST = ".//div[@class='productItem']";
    private static final String X_PATH_PRODUCT_NAME = ".//h1[@class='title f600']";
    private static final String X_PATH_PRODUCT_LIST_NAME = ".//h3[@class='title f600']";

    private static final String X_PATH_BUY_PRICE = ".//strong[@class='pricePerItem']";
    private static final String X_PATH_BUY_PRICE_LIST = ".//div[@class='price f600']";
    private static final String X_PATH_REDEMPTION_PRICE = "";

    public SilverumMetalScraper() {
        super();
    }

/////// PRICE

    @Override
    public List<Link> scrapAllLinks() {
        List<Link> elements = new ArrayList<>();
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_GOLD_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_GOLD_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_GOLD_BRICK)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_SILVER_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_SILVER_BRICK)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }

        return elements;
    }

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     */
    @Override
    public String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];
    }

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public String scrapProductName(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }



    /////// LINK

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLink(elementProduct, "./a", Dealer.SILVERUM, BASE);
    }

    @Override
    public double scrapBuyPrice(HtmlPage productDetailPage) {
        String x = scrapBuyPrice(productDetailPage, X_PATH_BUY_PRICE);
        if(Pattern.compile("\\d+,\\d+ Kč bez DPH").matcher(x).find()) {
            x = x.split("\\d*[ ]?\\d+,\\d+ Kč bez DPH")[0];
        }

        return Convert.currencyToNumberConvert(x);
    }

    @Override
    public double scrapRedemptionPrice(HtmlPage page) {
        // TODO Compute redemption
        return 0.0;
//        return Convert.currencyConvert(
//                scrapRedemptionPrice(page, X_PATH_REDEMPTION_PRICE).replace(".", ""),
//                euroExchangeRate,
//                "€"
//        );
    }
}
