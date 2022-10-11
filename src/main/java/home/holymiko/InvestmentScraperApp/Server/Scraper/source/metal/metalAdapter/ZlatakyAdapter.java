package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class ZlatakyAdapter extends Client implements MetalAdapterInterface {

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



    public ZlatakyAdapter() {
        super();
    }

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }
    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        List<Link> elements = new ArrayList<>();
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_GOLD_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_GOLD_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_SILVER_BAR)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_SILVER_COIN)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinksFromProductList(getPage(SEARCH_URL_PLATINUM)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return elements;
    }

    /////// PRICE

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public double scrapPriceFromProductPage(HtmlPage productDetailPage) {
        try {
            String x = Convert.currencyClean(
                    ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText()
            );
            if(Pattern.compile("původnícena:").matcher(x).find()) {
                x = x.split("původnícena:\\d+")[1];
            }
            return Double.parseDouble(x);
        } catch (Exception e) {
            return Double.parseDouble("0.0");
        }

    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }
    @Override
    public double scrapBuyOutPrice(HtmlPage page) {
        try {
            return Convert.currencyToNumberConvert(
                    ((HtmlElement) page.getFirstByXPath(X_PATH_REDEMPTION_PRICE)).asText()
            );
        } catch (Exception e) {
            return Double.parseDouble("0.0");
        }
    }

    /////// LINK
    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLink(elementProduct, X_PATH_PRODUCT_LIST_PRODUCT_LINK, Dealer.ZLATAKY, BASE_URL);
    }

}
