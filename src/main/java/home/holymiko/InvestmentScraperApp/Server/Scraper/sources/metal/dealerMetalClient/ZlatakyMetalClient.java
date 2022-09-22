package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.parser.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ZlatakyMetalClient extends Client implements MetalClientInterface {

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
    private static final String X_PATH_REDEMPTION_PRICE = ".//*[@id=\"snippet--page\"]/div[2]/div[1]/div[2]/div[5]/div[1]/span[2]/strong";



    public ZlatakyMetalClient() {
        super();
    }

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }
    @Override
    public List<Link> scrapAllLinks() {
        List<Link> elements = new ArrayList<>();
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_GOLD_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_GOLD_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_SILVER_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_SILVER_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_PLATINUM)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return elements;
    }

    /////// PRICE

    /**
     * @param redemptionPriceHtml
     * @return
     */
    @Override
    public String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText();
    }

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public double scrapBuyPrice(HtmlPage productDetailPage) {
        String x = Convert.currencyClean(scrapBuyPrice(productDetailPage, X_PATH_BUY_PRICE));

        if(Pattern.compile("původnícena:").matcher(x).find()) {
            x = x.split("původnícena:\\d+")[1];
        }

        return Double.parseDouble(x);
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
        return scrapLink(elementProduct, X_PATH_PRODUCT_LIST_PRODUCT_LINK, Dealer.ZLATAKY, BASE_URL);
    }

}
