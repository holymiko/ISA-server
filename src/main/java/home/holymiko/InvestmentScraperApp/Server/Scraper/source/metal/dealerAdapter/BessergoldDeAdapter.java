package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.Client;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Scraper.extractor.Convert;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BessergoldDeAdapter extends Client implements ProductDetailInterface {

    private static final Dealer DEALER = Dealer.BESSERGOLD_DE;
    private static final List<String> PRODUCT_LIST_URL = Arrays.asList(
            "https://www.bessergold.de/de/gold.html?product_list_limit=all",
            "https://www.bessergold.de/de/silber.html?product_list_limit=all",
            "https://www.bessergold.de/de/platin.html?product_list_limit=all",
            "https://www.bessergold.de/de/palladium.html?product_list_limit=all"
    );

    private static final String X_PATH_PRODUCT_LIST = "//li[@class='item product product-item']";
    private static final String X_PATH_PRODUCT_NAME = ".//span[@class='base']";

    //    private static final String X_PATH_BUY_PRICE = ".//span[@class='price-wrapper price-including-tax']/span";
    private static final String X_PATH_BUY_PRICE = ".//span[@class='price']";
    private static final String X_PATH_SELL_PRICE = ".//div[@class='vykupni-cena']";
    private static final String X_PATH_AVAILABILITY = "//*[@id=\"maincontent\"]/div[2]/div/div[1]/div[2]/div[2]/div";
    private final double euroExchangeRate;

    public BessergoldDeAdapter(double euroExchangeRate) {
        super("BessergoldDeAdapter");
        this.euroExchangeRate = euroExchangeRate;
    }

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }

    @Override
    public List<Link> scrapAllLinksFromProductLists() {
        return scrapAllLinksFromProductListUtil(PRODUCT_LIST_URL);
    }

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public String scrapNameFromProductPage(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

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
    public double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) {
        return Convert.currency(
                ((HtmlElement) productDetailPage.getFirstByXPath(X_PATH_BUY_PRICE)).asText().replace(".", ""),
                euroExchangeRate,
                "€"
        );
    }

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     * @param redemptionPriceHtml
     * @return
     */
    public String buyOutHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1].replace(".", "");
    }

    @Override
    public double scrapSellPriceFromProductPage(HtmlPage page) {
        try {
            return Convert.currency(
                    buyOutHtmlToText(page.getFirstByXPath(X_PATH_SELL_PRICE)),
                    euroExchangeRate,
                    "€"
            );
        } catch (Exception e) {
            return 0.0;
        }
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
