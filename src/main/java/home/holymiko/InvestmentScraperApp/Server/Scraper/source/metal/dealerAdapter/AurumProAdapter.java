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

public class AurumProAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.AURUM_PRO;
    private static final String BASE_URL = "https://www.aurumpro.cz";
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.aurumpro.cz/zlate-mince",
            "https://www.aurumpro.cz/zlate-mince/pg-2",
            "https://www.aurumpro.cz/zlate-mince/pg-3",
            "https://www.aurumpro.cz/zlate-mince/pg-4",
            "https://www.aurumpro.cz/zlate-slitky",
            "https://www.aurumpro.cz/zlate-slitky/pg-2",
            "https://www.aurumpro.cz/stribrne-mince",
            "https://www.aurumpro.cz/stribrne-mince/pg-2",
            "https://www.aurumpro.cz/stribrne-slitky"
            // TODO Add Mince ČNB URLs
    );

    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"snippet-products-productList\"]/div/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = "./a";
    private static final String X_PATH_PRODUCT_NAME = "/html/body/main/article/div[2]/div/div/div[2]/div[1]/header/h2";
    private static final String X_PATH_BUY_PRICE = "//*[@id=\"snippet--codeAjax\"]/section/div/strong";
    private static final String X_PATH_SELL_PRICE = "//*[@id=\"snippet--codeAjax\"]/section/a/strong";
    private static final String X_PATH_AVAILABILITY = "//*[@id=\"snippet--codeAjax\"]/div/div[1]/strong";


    public AurumProAdapter() {
        super("AurumProAdapter");
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
        );
    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        return Convert.currencyToDouble(
                ((HtmlElement) page.getFirstByXPath(X_PATH_SELL_PRICE)).asText()
        );
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
