package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Component
public class ZlatakyScraper extends MetalScraper {

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

    @Autowired
    public ZlatakyScraper(LinkService linkService,
                          PriceService priceService,
                          ProductService productService,
                          PortfolioService portfolioService) {
        super(
                Dealer.ZLATAKY,
                linkService,
                priceService,
                portfolioService,
                productService,
                new ArrayList<>(
                        Arrays.asList(
                                SEARCH_URL_GOLD_BAR,
                                SEARCH_URL_GOLD_COIN
                        )
                ),
                new ArrayList<>(
                        Arrays.asList(
                                SEARCH_URL_SILVER_BAR,
                                SEARCH_URL_SILVER_COIN
                        )
                ),
                new ArrayList<>(
                        Collections.singletonList(
                                SEARCH_URL_PLATINUM
                        )
                ),
                new ArrayList<>(
                        Collections.singletonList(
                                SEARCH_URL_PALLADIUM
                        )
                ),
                X_PATH_PRODUCT_LIST,
                X_PATH_PRODUCT_NAME,
                X_PATH_BUY_PRICE,
                X_PATH_REDEMPTION_PRICE
        );
    }

    /////// PRICE

    @Override
    protected String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];          // Výkupní cena (osvobozeno od DPH): xxxx,xx Kč
    }

    /////// LINK

    @Override
    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//div/h3/a");
        if(itemAnchor != null) {
            Link link = new Link(Dealer.ZLATAKY, BASE_URL + itemAnchor.getHrefAttribute());
            linkFilterWrapper(link);
            return;
        }
        System.out.println("Error: "+htmlItem.asText());
    }

}
