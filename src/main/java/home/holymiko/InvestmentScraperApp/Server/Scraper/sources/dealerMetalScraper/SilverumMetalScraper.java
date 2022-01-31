package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.MetalScraperInterface;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SilverumMetalScraper implements MetalScraperInterface {
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
    }

/////// PRICE

    @Override
    public List<Link> scrapAllLinks(WebClient webClient) {
        List<Link> elements = new ArrayList<>();
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_GOLD_COIN)));
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_GOLD_BAR)));
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_GOLD_BRICK)));


//        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_SILVER)));
        return elements;
    }

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     * @param redemptionPriceHtml
     * @return
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
        return Convert.currencyToNumberConvert(
                scrapBuyPrice(productDetailPage, X_PATH_BUY_PRICE)
        );
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
