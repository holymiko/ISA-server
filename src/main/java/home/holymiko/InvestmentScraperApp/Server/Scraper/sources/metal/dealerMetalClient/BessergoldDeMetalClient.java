package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Client;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Scraper.parser.Convert;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class BessergoldDeMetalClient extends Client implements MetalClientInterface, RedemptionListInterface {

    private static final String SEARCH_URL_GOLD = "https://www.bessergold.de/de/gold.html?product_list_limit=all";
    private static final String SEARCH_URL_SILVER = "https://www.bessergold.de/de/silber.html?product_list_limit=all";
    private static final String SEARCH_URL_PLATINUM = "https://www.bessergold.de/de/platin.html?product_list_limit=all";
    private static final String SEARCH_URL_PALLADIUM = "https://www.bessergold.de/de/palladium.html?product_list_limit=all";

    private static final String X_PATH_PRODUCT_LIST = "//li[@class='item product product-item']";
    private static final String X_PATH_PRODUCT_NAME = ".//span[@class='base']";

    //    private static final String X_PATH_BUY_PRICE = ".//span[@class='price-wrapper price-including-tax']/span";
    private static final String X_PATH_BUY_PRICE = ".//span[@class='price']";
    private static final String X_PATH_REDEMPTION_PRICE = ".//div[@class='vykupni-cena']";

    private final double euroExchangeRate;

    public BessergoldDeMetalClient(double euroExchangeRate) {
        super();
        this.euroExchangeRate = euroExchangeRate;
    }

/////// PRICE

    @Override
    public HtmlPage getPage(String link) throws ResourceNotFoundException {
        return this.loadPage(link);
    }

    @Override
    public List<Link> scrapAllLinks() {
        List<Link> elements = new ArrayList<>();
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_GOLD)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_SILVER)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_PLATINUM)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        try {
            elements.addAll(scrapLinks(getPage(SEARCH_URL_PALLADIUM)));
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        return elements;
    }

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     * @param redemptionPriceHtml
     * @return
     */
    @Override
    public String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];
    }

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public String scrapProductName(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }



    /////// LINK

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        return scrapLink(
                elementProduct,
                ".//strong[@class='product name product-item-name']/a",
                Dealer.BESSERGOLD_DE,
                ""
        );
    }

    @Override
    public double scrapBuyPrice(HtmlPage productDetailPage) {
        return Convert.currencyConvert(
                scrapBuyPrice(productDetailPage, X_PATH_BUY_PRICE).replace(".", ""),
                euroExchangeRate,
                "€"
        );
    }

    @Override
    public double scrapRedemptionPrice(HtmlPage page) {
        return Convert.currencyConvert(
                scrapRedemptionPrice(page, X_PATH_REDEMPTION_PRICE).replace(".", ""),
                euroExchangeRate,
                "€"
        );
    }

    @Override
    public List<Pair<String, Double>> scrapRedemptionFromList() {
        // TODO Implement scrapRedemptionFromList
        return new ArrayList<>();
    }
}
