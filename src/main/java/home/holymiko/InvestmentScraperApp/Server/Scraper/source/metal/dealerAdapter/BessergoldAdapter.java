package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BessergoldAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.BESSERGOLD_CZ;
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.bessergold.cz/investicni-zlato.html?product_list_limit=all",
            "https://www.bessergold.cz/investicni-stribro.html?product_list_limit=all",
            "https://www.bessergold.cz/investicni-platina.html?product_list_limit=all",
            "https://www.bessergold.cz/investicni-palladium.html?product_list_limit=all"
    );

    private static final String X_PATH_PRODUCT_LIST = "//li[@class='item product product-item']";
    private static final String X_PATH_PRODUCT_NAME = ".//span[@class='base']";
    private static final String X_PATH_BUY_PRICE = ".//span[@class='price']";
    private static final String X_PATH_SELL_PRICE = ".//div[@class='vykupni-cena']";
    private static final String X_PATH_AVAILABILITY = "//*[@id=\"maincontent\"]/div[2]/div/div[1]/div[2]/div[2]/div/span";

    public BessergoldAdapter() {
        super("BessergoldAdapter");
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
    public String scrapNameFromProductPage(HtmlPage page) {
        String name = ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText().trim();
        // Web mistake, verified by call to Bessergold. Product vintage is random.
        if(name.equals("Stříbrná mince 1 oz (trojská unce) WIENER PHILHARMONIKER Rakousko 2011")
        || name.equals("Stříbrná mince 1 oz (trojská unce) MAPLE LEAF Kanada 2011")) {
            name = name.replace( " 2011", "");
        }
        return name;
    }

    @Override
    public double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) {
        return Convert.currencyToDouble(
                ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText()
        );
    }

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     */
    public String buyOutHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        try {
            return Convert.currencyToDouble(
                buyOutHtmlToText(page.getFirstByXPath(X_PATH_SELL_PRICE))
            );
        } catch (Exception e) {
            return 0.0;
        }
    }


    /////// LINK

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLinkFromAnchor(
                elementProduct,
                ".//strong[@class='product name product-item-name']/a",
                DEALER,
                ""
        );
    }

    @Override
    public List<Pair<String, Double>> scrapSellPriceFromList() {
        // TODO Implement scrapRedemptionFromList
        return new ArrayList<>();
    }

    ////// AVAILABILITY

    @Override
    public String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        return ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_AVAILABILITY)).asText();
    }

}
