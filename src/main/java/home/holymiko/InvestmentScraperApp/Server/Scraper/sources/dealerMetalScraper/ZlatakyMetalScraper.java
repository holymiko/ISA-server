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

public class ZlatakyMetalScraper extends Client implements MetalScraperInterface {

    private static final String BASE_URL = "https://zlataky.cz";
    private static final String SEARCH_URL_GOLD_COIN = "https://zlataky.cz/investicni-zlate-mince?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";
    private static final String SEARCH_URL_GOLD_BAR = "https://zlataky.cz/investicni-zlate-slitky?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";

    private static final String SEARCH_URL_SILVER_COIN = "https://zlataky.cz/investicni-stribrne-mince?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";
    private static final String SEARCH_URL_SILVER_BAR = "https://zlataky.cz/investicni-stribrne-slitky?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";

    private static final String SEARCH_URL_PLATINUM = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=1&filter_weight=on&filter_id=on&sort=3a&filter_in_stock=1";
    private static final String SEARCH_URL_PALLADIUM = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=2&filter_weight=on&filter_id=on&sort=3a&filter_in_stock=1";
    private static final String SEARCH_URL_RHODIUM = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=3&filter_weight=on&filter_id=on&sort=1a&filter_in_stock=1";

    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"productListing\"]/div";
    private static final String X_PATH_PRODUCT_NAME = ".//*[@id=\"productName\"]";
    private static final String X_PATH_BUY_PRICE = ".//*[@id=\"product_price\"]";
    private static final String X_PATH_REDEMPTION_PRICE = ".//*[@id=\"product_price_purchase\"]";

    public ZlatakyMetalScraper() {
        super();
    }

    @Override
    public List<Link> scrapAllLinks() {
        List<Link> elements = new ArrayList<>();
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_GOLD_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_GOLD_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_SILVER_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_SILVER_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_PLATINUM)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(loadPage(SEARCH_URL_PALLADIUM)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return elements;
    }

    /////// PRICE

    /**
     * Takes following pattern:
     * - Výkupní cena (osvobozeno od DPH): xxxx,xx Kč
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
    public double scrapBuyPrice(HtmlPage productDetailPage) {
        return Convert.currencyToNumberConvert(
                scrapBuyPrice(productDetailPage, X_PATH_BUY_PRICE)
        );
    }
    @Override
    public String scrapProductName(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapRedemptionPrice(HtmlPage page) {
        return Convert.currencyToNumberConvert(
                scrapRedemptionPrice(page, X_PATH_REDEMPTION_PRICE)
        );
    }

    /////// LINK

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLink(elementProduct, ".//div/h3/a", Dealer.ZLATAKY, BASE_URL);
    }

}
