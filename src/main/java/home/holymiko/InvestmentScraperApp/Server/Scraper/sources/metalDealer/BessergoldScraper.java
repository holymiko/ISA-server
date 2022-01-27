package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer;

import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Mapper.LinkMapper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;

@Component
public class BessergoldScraper extends MetalScraper {
    private static final String SEARCH_URL_GOLD = "https://www.bessergold.cz/investicni-zlato.html?product_list_limit=all";
    private static final String SEARCH_URL_SILVER = "https://www.bessergold.cz/investicni-stribro.html?product_list_limit=all";
    private static final String SEARCH_URL_PLATINUM = "https://www.bessergold.cz/investicni-platina.html?product_list_limit=all";
    private static final String SEARCH_URL_PALLADIUM = "https://www.bessergold.cz/investicni-palladium.html?product_list_limit=all";

    private static final String X_PATH_PRODUCT_LIST = "//li[@class='item product product-item']";
    private static final String X_PATH_PRODUCT_NAME = ".//span[@class='base']";
    private static final String X_PATH_BUY_PRICE = ".//span[@class='price']";
    private static final String X_PATH_REDEMPTION_PRICE = ".//div[@class='vykupni-cena']";

    @Autowired
    public BessergoldScraper(LinkService linkService,
                             PriceService priceService,
                             ProductService productService,
                             PortfolioService portfolioService,
                             LinkMapper linkMapper) {
        super(
                Dealer.BESSERGOLD,
                linkService,
                priceService,
                portfolioService,
                productService,
                linkMapper,
                new ArrayList<>(
                        Collections.singletonList(
                                SEARCH_URL_GOLD
                        )
                ),
                new ArrayList<>(
                        Collections.singletonList(
                                SEARCH_URL_SILVER
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

    /**
     * Takes following pattern:
     * - Aktuální výkupní cena (bez DPH): xxxx,xx Kč
     * @param redemptionPriceHtml
     * @return
     */
    @Override
    protected String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];
    }


    /////// LINK

    @Override
    protected void scrapLink(HtmlElement htmlItem, String searchUrl) {
        HtmlAnchor itemAnchor = htmlItem.getFirstByXPath(".//strong[@class='product name product-item-name']/a");
        if(itemAnchor == null) {
            System.out.println("Error: "+htmlItem.asText());
            return;
        }
        Link link = new Link(Dealer.BESSERGOLD, itemAnchor.getHrefAttribute());
        linkFilterWrapper(link);
    }
}
