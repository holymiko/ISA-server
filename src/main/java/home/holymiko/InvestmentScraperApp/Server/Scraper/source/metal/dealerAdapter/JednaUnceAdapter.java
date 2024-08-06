package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;

import java.util.Arrays;
import java.util.List;

public class JednaUnceAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.JEDNA_UNCE;
    private static final String BASE_URL = "https://www.jednaunce.cz";
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.jednaunce.cz/zlato/",
            "https://www.jednaunce.cz/zlato/strana-2/",
            "https://www.jednaunce.cz/zlato/strana-3/",
            "https://www.jednaunce.cz/stribro/",
            "https://www.jednaunce.cz/stribro/strana-2/",
            "https://www.jednaunce.cz/stribro/strana-3/",
            "https://www.jednaunce.cz/stribro/strana-4/",
            "https://www.jednaunce.cz/stribro/strana-5/",
            "https://www.jednaunce.cz/stribro/strana-6/"
    );


    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"products\"]/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = "./div/a";
    private static final String X_PATH_PRODUCT_NAME = "//*[@id=\"content\"]/div/div[1]/div/h1";
    private static final String X_PATH_BUY_PRICE = "//*[@id=\"product-detail-form\"]/div/div[3]/div[1]/strong/span";
    private static final String X_PATH_SELL_PRICE = "//*[@id=\"product-detail-form\"]/div/div[3]/div[8]/div[3]/strong";
    private static final String X_PATH_AVAILABILITY = "//*[@id=\"product-detail-form\"]/div/div[3]/div[2]/span/span";


    public JednaUnceAdapter() {
        super("JednaUnceAdapter");
    }

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }

    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        return scrapAllLinksFromProductListUtil(PRODUCT_LIST_URL);
    }

    /////// PRICE

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) {
        return Convert.currencyToDouble(
                ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText()
                        .replace("ks", "")
                        .replace("/", "")
        );
    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        // TODO Scrap from list
        return 0;
    }

    /////// LINK
    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLinkFromAnchor(elementProduct, X_PATH_PRODUCT_LIST_ANCHOR, DEALER, BASE_URL);
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
    }

}
