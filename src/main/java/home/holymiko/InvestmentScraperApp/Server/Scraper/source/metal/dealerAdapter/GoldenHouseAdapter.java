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

public class GoldenHouseAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.GOLDEN_HOUSE;
    private static final String BASE_URL = "https://goldenhouse.cz";
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://goldenhouse.cz/c/zlato",
            "https://goldenhouse.cz/c/zlato?page=2",
            "https://goldenhouse.cz/c/zlato?page=3",
            "https://goldenhouse.cz/c/zlato?page=4",
            "https://goldenhouse.cz/c/zlato?page=5",
            "https://goldenhouse.cz/c/zlato?page=6",
            "https://goldenhouse.cz/c/zlato?page=7",
            "https://goldenhouse.cz/c/zlato?page=8",
            "https://goldenhouse.cz/c/zlato?page=9",
            "https://goldenhouse.cz/c/stribro",
            "https://goldenhouse.cz/c/stribro?page=2",
            "https://goldenhouse.cz/c/stribro?page=3",
            "https://goldenhouse.cz/c/stribro?page=4",
            "https://goldenhouse.cz/c/platina-a-palladium",
            "https://goldenhouse.cz/c/platina-a-palladium?page=2"
            // TODO Add Numismatika, ČNB, Pro sběratele URLs
    );


    private static final String X_PATH_PRODUCT_LIST = "//*[@class=\"productList\"]/child::*";
    private static final String X_PATH_PRODUCT_LIST_ANCHOR = "./a";

    private static final String X_PATH_PRODUCT_NAME = "//*[@class=\"productDetailMain\"]/h1";
    private static final String X_PATH_PRODUCT_NAME_2 = "//*[@class=\"productDetailMain double\"]/h1";
    private static final String X_PATH_BUY_PRICE = "//*[@class=\"fullPrice\"]";
    private static final String X_PATH_SELL_PRICE = "//*[@class=\"mainContent\"]/div[5]/div";
    private static final String X_PATH_SELL_PRICE_2 = "//*[@class=\"mainContent\"]/div[4]/div";
    private static final String X_PATH_AVAILABILITY = "//*[@class=\"mainContent\"]/div[3]";


    public GoldenHouseAdapter() {
        super("GoldenHouseAdapter");
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
                        .replace("s DPH", "")
        );
    }
    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        HtmlElement name = page.getFirstByXPath(X_PATH_PRODUCT_NAME);
        if(name == null) {
            name = page.getFirstByXPath(X_PATH_PRODUCT_NAME_2);
        }
        return name.asText();
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        HtmlElement sellHtmlElement = page.getFirstByXPath(X_PATH_SELL_PRICE);
        if(sellHtmlElement == null) {
            sellHtmlElement = page.getFirstByXPath(X_PATH_SELL_PRICE_2);
        }
        return Convert.currencyToDouble(
                sellHtmlElement.asText()
                        .split(":")[1]
                        .split("\\(")[0]
                        .replace("s DPH", "")
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
