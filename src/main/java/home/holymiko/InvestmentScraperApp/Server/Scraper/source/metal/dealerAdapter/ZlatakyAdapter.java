package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class ZlatakyAdapter extends Client implements ProductDetailInterface {

    private static final String BASE_URL = "https://zlataky.cz";
    private static final String SEARCH_URL_GOLD_COIN = "https://zlataky.cz/investicni-zlate-mince?page=1&page_all=1";
    private static final String SEARCH_URL_GOLD_BAR = "https://zlataky.cz/investicni-zlate-slitky?page=1&page_all=1";
    private static final String SEARCH_URL_SILVER_COIN = "https://zlataky.cz/investicni-stribrne-mince?page=1&page_all=1";
    private static final String SEARCH_URL_SILVER_BAR = "https://zlataky.cz/investicni-stribrne-slitky?page=1&page_all=1";
    private static final String SEARCH_URL_PLATINUM = "https://zlataky.cz/ostatni-investicni-kovy";

    private static final String X_PATH_PRODUCT_LIST = ".//*[@id=\"kategorie-obsah\"]/div[3]/child::*";
    private static final String X_PATH_PRODUCT_LIST_PRODUCT_LINK = ".//div/div[4]/a";
    private static final String X_PATH_PRODUCT_NAME = "//*[@id=\"snippet--page\"]/div[2]/div[1]/div[2]/h1";
    private static final String X_PATH_BUY_PRICE = "//*[@id=\"hlavni_cena\"]";
    private static final String X_PATH_SELL_PRICE = ".//*[@id=\"box_vip_vykup\"]/div[3]/span[2]/strong";
    private static final String X_PATH_AVAILABILITY_1 = "//*[@id=\"sklad_info\"]/span[1]";
    private static final String X_PATH_AVAILABILITY_2 = "//*[@id=\"sklad_info\"]/span[2]";


    public ZlatakyAdapter() {
        super("ZlatakyAdapter");
    }

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }

    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        return scrapAllLinksFromProductListUtil(
                Arrays.asList(SEARCH_URL_GOLD_BAR, SEARCH_URL_GOLD_COIN, SEARCH_URL_SILVER_BAR, SEARCH_URL_SILVER_COIN, SEARCH_URL_PLATINUM)
        );
    }

    /////// PRICE

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) {
        try {
            String x = Convert.currencyClean(
                    ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText()
            );
            if(Pattern.compile("původnícena:").matcher(x).find()) {
                x = x.split("původnícena:\\d+")[1];
            }
            return Double.parseDouble(x);
        } catch (Exception e) {
            return 0.0;
        }

    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        try {
            return Convert.currencyToDouble(
                    ((HtmlElement) page.getFirstByXPath(X_PATH_SELL_PRICE)).asText()
            );
        } catch (Exception e) {
            return 0.0;
        }
    }

    /////// LINK
    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLink(elementProduct, X_PATH_PRODUCT_LIST_PRODUCT_LINK, Dealer.ZLATAKY, BASE_URL);
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        // There are 2 spans in HTML code, but only 1 is visible
        HtmlElement span = productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY_1);
        if(span.getAttribute("class").contains("zobrazit")) {
            return span.asText();
        }
        return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY_2)).asText();
    }

}
