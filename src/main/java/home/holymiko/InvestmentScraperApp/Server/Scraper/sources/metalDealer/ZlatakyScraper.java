package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.ScraperInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

public class ZlatakyScraper implements ScraperInterface {

    private static final String BASE_URL = "https://zlataky.cz";
    private static final String SEARCH_URL_GOLD_COIN = "https://zlataky.cz/investicni-zlate-mince?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";
    private static final String SEARCH_URL_GOLD_BAR = "https://zlataky.cz/investicni-zlate-slitky?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";

    private static final String SEARCH_URL_SILVER_COIN = "https://zlataky.cz/investicni-stribrne-mince?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";
    private static final String SEARCH_URL_SILVER_BAR = "https://zlataky.cz/investicni-stribrne-slitky?ext=0&filter_weight=on&sort=3a&filter_in_stock=1&page=all";

    private static final String SEARCH_URL_PLATINUM = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=1&filter_weight=on&filter_id=on&sort=3a&filter_in_stock=1";
    private static final String SEARCH_URL_PALLADIUM = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=2&filter_weight=on&filter_id=on&sort=3a&filter_in_stock=1";
    private static final String SEARCH_URL_RHODIUM = "https://zlataky.cz/ostatni-investicni-kovy?ext=0&filter_subcat_value=3&filter_weight=on&filter_id=on&sort=1a&filter_in_stock=1";

    private static final String X_PATH_PRODUCT_LIST = "//*[@id=\"productListing\"]/div";
    private static final String X_PATH_PRODUCT_NAME = ".//*[@id=\"productName\"]";
    private static final String X_PATH_BUY_PRICE = ".//*[@id=\"product_price\"]";
    private static final String X_PATH_REDEMPTION_PRICE = ".//*[@id=\"product_price_purchase\"]";

    public ZlatakyScraper() {}

    @Override
    public List<Link> scrapAllLinks(WebClient webClient) {
        List<Link> elements = new ArrayList<>();
        elements.addAll(scrapLinks(webClient, SEARCH_URL_GOLD_BAR));
        elements.addAll(scrapLinks(webClient, SEARCH_URL_GOLD_COIN));
        elements.addAll(scrapLinks(webClient, SEARCH_URL_SILVER_BAR));
        elements.addAll(scrapLinks(webClient, SEARCH_URL_SILVER_COIN));
        elements.addAll(scrapLinks(webClient, SEARCH_URL_PLATINUM));
        elements.addAll(scrapLinks(webClient, SEARCH_URL_PALLADIUM));
        return elements;

    }

    /////// PRICE

    /**
     * Takes following pattern:
     * - Výkupní cena (osvobozeno od DPH): xxxx,xx Kč
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
    public double scrapBuyPrice(HtmlPage page) {
        return scrapBuyPrice(page, X_PATH_BUY_PRICE);
    }
    @Override
    public String scrapProductName(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapRedemptionPrice(HtmlPage page) {
        return scrapRedemptionTime(page, X_PATH_REDEMPTION_PRICE);
    }

    /////// LINK

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        HtmlAnchor itemAnchor = elementProduct.getFirstByXPath(".//div/h3/a");
        if(itemAnchor != null) {
            return new Link(Dealer.ZLATAKY, BASE_URL + itemAnchor.getHrefAttribute());
        }
        System.out.println("Error: "+ elementProduct.asText());
        return null;
    }

}
