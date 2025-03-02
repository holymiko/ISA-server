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

public class EkkaGoldAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.EKKA_GOLD;
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.ekka-gold.cz/zlate-investicni-mince",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=2",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=3",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=4",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=5",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=6",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=7",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=8",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=9",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=10",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=11",
            "https://www.ekka-gold.cz/zlate-investicni-mince?page=12",
            "https://www.ekka-gold.cz/zlate-investicni-slitky",
            "https://www.ekka-gold.cz/zlate-investicni-slitky?page=2",
            "https://www.ekka-gold.cz/zlate-investicni-slitky?page=3",
            "https://www.ekka-gold.cz/zlate-investicni-slitky?page=4",
            "https://www.ekka-gold.cz/stribrne-investicni-mince",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=2",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=3",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=4",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=5",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=6",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=7",
            "https://www.ekka-gold.cz/stribrne-investicni-mince?page=8",
            "https://www.ekka-gold.cz/stribrne-investicni-slitky",
            "https://www.ekka-gold.cz/stribrne-investicni-slitky?page=2",
            "https://www.ekka-gold.cz/stribrne-investicni-slitky?page=3",
            "https://www.ekka-gold.cz/platinove-investicni-mince",
            "https://www.ekka-gold.cz/platinove-investicni-slitky",
            "https://www.ekka-gold.cz/paladiove-investicni-mince",
            "https://www.ekka-gold.cz/paladiove-investicni-slitky",
            "https://www.ekka-gold.cz/australska-lunarni-serie",
            "https://www.ekka-gold.cz/australska-lunarni-serie?page=2",
            "https://www.ekka-gold.cz/australska-lunarni-serie?page=3",
            "https://www.ekka-gold.cz/australska-lunarni-serie?page=4",
            "https://www.ekka-gold.cz/obezne-mince",
            "https://www.ekka-gold.cz/moderni-numismatika",
            "https://www.ekka-gold.cz/moderni-numismatika?page=2",
            "https://www.ekka-gold.cz/moderni-numismatika?page=3"
    );

    private static final String X_PATH_PRODUCT_LIST = "//*[@class=\"rocketoo-products\"]/div[1]/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = "./div/div[2]/a";
    private static final String X_PATH_PRODUCT_NAME = "//*[@id=\"rocketoo-product-detail\"]/div[1]/div[2]/div[1]/div/h1";
    private static final String X_PATH_BUY_PRICE = "//*[@class=\"product-price-primary\"]";
    private static final String X_PATH_SELL_PRICE = "//*[@class=\"product-purchase-price\"]/a";
    private static final String X_PATH_AVAILABILITY = "//*[@class=\"product-detail-info-line product-detail-availability\"]/div/div[2]";


    public EkkaGoldAdapter() {
        super("EkkaGoldAdapter");
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
                ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText().split("/")[0]
        );
    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        return Convert.currencyToDouble(
                ((HtmlElement) page.getFirstByXPath(X_PATH_SELL_PRICE)).asText().split("/")[0]
        );
    }

    /////// LINK
    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLinkFromAnchor(elementProduct, X_PATH_PRODUCT_LIST_ANCHOR, DEALER, "");
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
    }

}
