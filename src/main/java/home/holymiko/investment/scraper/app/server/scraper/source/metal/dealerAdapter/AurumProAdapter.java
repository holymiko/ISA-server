package home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Convert;
import home.holymiko.investment.scraper.app.server.scraper.source.Client;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;

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
            "https://www.aurumpro.cz/stribrne-slitky",
            "https://www.aurumpro.cz/mince-cnb",
            "https://www.aurumpro.cz/mince-cnb/pg-2",
            "https://www.aurumpro.cz/mince-cnb/pg-3",
            "https://www.aurumpro.cz/mince-cnb/pg-4",
            "https://www.aurumpro.cz/mince-cnb/pg-5",
            "https://www.aurumpro.cz/mince-cnb/pg-6",
            "https://www.aurumpro.cz/mince-cnb/pg-7",
            "https://www.aurumpro.cz/mince-cnb/pg-8",
            "https://www.aurumpro.cz/historicke-novorazby",
            "https://www.aurumpro.cz/historicke-novorazby/pg-2",
            "https://www.aurumpro.cz/historicke-novorazby/pg-3",
            "https://www.aurumpro.cz/historicke-novorazby/pg-4",
            "https://www.aurumpro.cz/historicke-novorazby/pg-5",
            "https://www.aurumpro.cz/historicke-novorazby/pg-6",
            "https://www.aurumpro.cz/historicke-novorazby/pg-7",
            "https://www.aurumpro.cz/historicke-novorazby/pg-8",
            "https://www.aurumpro.cz/historicke-novorazby/pg-9",
            "https://www.aurumpro.cz/historicke-novorazby/pg-10",
            "https://www.aurumpro.cz/historicke-novorazby/pg-11",
            "https://www.aurumpro.cz/historicke-novorazby/pg-12",
            "https://www.aurumpro.cz/historicke-novorazby/pg-13",
            "https://www.aurumpro.cz/historicke-novorazby/pg-14",
            "https://www.aurumpro.cz/historicke-novorazby/pg-15",
            "https://www.aurumpro.cz/historicke-novorazby/pg-16"
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
