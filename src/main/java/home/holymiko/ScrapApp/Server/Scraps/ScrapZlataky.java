package home.holymiko.ScrapApp.Server.Scraps;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Link;
import home.holymiko.ScrapApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ScrapZlataky extends ScrapMetal {

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
    public ScrapZlataky(LinkService linkService,
                        PriceService priceService,
                        ProductService productService,
                        PortfolioService portfolioService,
                        InvestmentService investmentService) {
        super(
                Dealer.ZLATAKY,
                linkService,
                priceService,
                portfolioService,
                productService,
                investmentService,
                SEARCH_URL_GOLD_BAR,       // Wont be used, method overwritten
                SEARCH_URL_SILVER_BAR,     // Wont be used, method overwritten
                SEARCH_URL_PLATINUM,
                SEARCH_URL_PALLADIUM,
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


    @Override
    public void goldLinksScrap() {
        scrapLinks(SEARCH_URL_GOLD_COIN);
        scrapLinks(SEARCH_URL_GOLD_BAR);
    }

    @Override
    public void silverLinksScrap() {
        scrapLinks(SEARCH_URL_SILVER_COIN);
        scrapLinks(SEARCH_URL_SILVER_BAR);
    }

}
