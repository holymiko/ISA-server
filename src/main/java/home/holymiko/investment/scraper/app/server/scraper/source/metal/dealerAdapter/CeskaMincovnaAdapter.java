package home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.scraper.extractor.Convert;
import home.holymiko.investment.scraper.app.server.scraper.source.Client;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CeskaMincovnaAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.CESKA_MINCOVNA;
    private static final String BASE_URL = "https://ceskamincovna.cz";
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka1/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka2/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka3/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka4/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka5/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka6/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka7/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka8/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka9/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka10/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka11/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka12/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka13/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka14/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka15/",
            "https://ceskamincovna.cz/pro-investory-1437-p/stranka16/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka1/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka2/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka3/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka4/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka5/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka6/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka7/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka8/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka9/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka10/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka11/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka12/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka13/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka14/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka15/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka16/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka17/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka18/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka19/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka20/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka21/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka22/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka23/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka24/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka25/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka26/",
            "https://ceskamincovna.cz/pro-sberatele-1436-p/stranka27/"
    );

    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"productList\"]/div[4]/div[1]/div/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = ".//div/div/div[1]/div[2]/a";
    private static final String X_PATH_PRODUCT_NAME = "/html/body/div[3]/div/div[1]/div[2]/div/h1";
    private static final String X_PATH_BUY_PRICE = "/html/body/div[3]/div/div[1]/div[2]/div/div[2]/div[2]";
    private static final String X_PATH_SELL_PRICE = "/html/body/div[3]/div/div[1]/div[2]/div/div[5]/div/div[2]/div[2]/span";
    private static final String X_PATH_SELL_PRICE_2 = "/html/body/div[3]/div/div[1]/div[2]/div/div[5]/div/div[3]/div[2]/span";
    private static final String X_PATH_SELL_PRICE_3 = "/html/body/div[3]/div/div[1]/div[2]/div/div[5]/div/div[4]/div[2]/span";
    private static final String X_PATH_AVAILABILITY = "/html/body/div[3]/div/div[1]/div[2]/div/div[1]/div/div[2]";


    public CeskaMincovnaAdapter() {
        super("CeskaMincovnaAdapter");
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
        HtmlSpan span = page.getFirstByXPath(X_PATH_SELL_PRICE);
        if(span == null) {
            span = page.getFirstByXPath(X_PATH_SELL_PRICE_2);
        }
        if(span == null) {
            span = page.getFirstByXPath(X_PATH_SELL_PRICE_3);
        }
        String x = span.asText();
        if(Pattern.compile("Orientační výkupní cena produktu je ").matcher(x).find()) {
            x = x.split("Orientační výkupní cena produktu je ")[1];
            x = x.split(" Kč. Výkup probíhá dle pravidel výkupu.")[0];
        }
        return Convert.currencyToDouble(x);
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
