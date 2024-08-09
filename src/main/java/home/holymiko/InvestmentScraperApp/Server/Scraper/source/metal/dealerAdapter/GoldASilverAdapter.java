package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;

import java.util.Arrays;
import java.util.List;

public class GoldASilverAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.GOLD_A_SILVER;
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            // GOLD
            "https://www.goldasilver.com/3-zlato?page=1",
            "https://www.goldasilver.com/3-zlato?page=2",
            "https://www.goldasilver.com/3-zlato?page=3",
            "https://www.goldasilver.com/3-zlato?page=4",
            "https://www.goldasilver.com/3-zlato?page=5",
            "https://www.goldasilver.com/3-zlato?page=6",
            "https://www.goldasilver.com/3-zlato?page=7",
            "https://www.goldasilver.com/3-zlato?page=8",
            "https://www.goldasilver.com/3-zlato?page=9",
            "https://www.goldasilver.com/3-zlato?page=10",
            "https://www.goldasilver.com/3-zlato?page=11",
            "https://www.goldasilver.com/3-zlato?page=12",
            "https://www.goldasilver.com/3-zlato?page=13",
            "https://www.goldasilver.com/3-zlato?page=14",
            "https://www.goldasilver.com/3-zlato?page=15",
            "https://www.goldasilver.com/3-zlato?page=16",
            // SILVER
            "https://www.goldasilver.com/6-stribro?page=1",
            "https://www.goldasilver.com/6-stribro?page=2",
            "https://www.goldasilver.com/6-stribro?page=3",
            "https://www.goldasilver.com/6-stribro?page=4",
            "https://www.goldasilver.com/6-stribro?page=5",
            "https://www.goldasilver.com/6-stribro?page=6",
            "https://www.goldasilver.com/6-stribro?page=7",
            "https://www.goldasilver.com/6-stribro?page=8",
            "https://www.goldasilver.com/6-stribro?page=9",
            "https://www.goldasilver.com/6-stribro?page=10",
            "https://www.goldasilver.com/6-stribro?page=11",
            "https://www.goldasilver.com/6-stribro?page=12",
            "https://www.goldasilver.com/6-stribro?page=13",
            "https://www.goldasilver.com/6-stribro?page=14",
            "https://www.goldasilver.com/6-stribro?page=15",
            "https://www.goldasilver.com/6-stribro?page=16",
            "https://www.goldasilver.com/6-stribro?page=17",
            "https://www.goldasilver.com/6-stribro?page=18",
            "https://www.goldasilver.com/6-stribro?page=19",
            "https://www.goldasilver.com/6-stribro?page=20",
            "https://www.goldasilver.com/6-stribro?page=21",
            "https://www.goldasilver.com/6-stribro?page=22",
            "https://www.goldasilver.com/6-stribro?page=23",
            "https://www.goldasilver.com/6-stribro?page=24",
            "https://www.goldasilver.com/6-stribro?page=25",
            "https://www.goldasilver.com/6-stribro?page=26",
            "https://www.goldasilver.com/6-stribro?page=27",
            "https://www.goldasilver.com/6-stribro?page=28",
            "https://www.goldasilver.com/6-stribro?page=29",
            // PLATINUM
            "https://www.goldasilver.com/304-platina",
            // OTHER
            "https://www.goldasilver.com/267-darkove-mince-a-slitky",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=2",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=3",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=4",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=5",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=6",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=7",
            "https://www.goldasilver.com/267-darkove-mince-a-slitky?page=8"
    );

    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"js-product-list\"]/div[1]/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = ".//article/div/div[2]/h2/a";
    private static final String X_PATH_PRODUCT_NAME = "//*[@id=\"main\"]/div[1]/div[2]/h1";
    private static final String X_PATH_BUY_PRICE = "//*[@id=\"main\"]/div[1]/div[2]/div[1]/div[1]/div/span";
    private static final String X_PATH_BUY_PRICE_1 = "//*[@id=\"main\"]/div[1]/div[2]/div[1]/div[2]/div/span";
    private static final String X_PATH_SELL_PRICE = "//*[@id=\"add-to-cart-or-refresh\"]/div[3]/div/span";
    private static final String X_PATH_AVAILABILITY = "//*[@id=\"product-availability\"]";


    public GoldASilverAdapter() {
        super("GoldASilverAdapter");
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
        HtmlSpan span = productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE);
        if(span == null) {
            span = productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE_1);
        }
        return Convert.currencyToDouble(span.asText());
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
        return scrapLinkFromAnchor(elementProduct, X_PATH_PRODUCT_LIST_ANCHOR, DEALER, "");
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
    }

}
