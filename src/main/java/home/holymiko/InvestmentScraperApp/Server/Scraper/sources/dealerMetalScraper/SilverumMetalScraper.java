package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.MetalScraperInterface;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class SilverumMetalScraper implements MetalScraperInterface {
    private static final String SEARCH_URL_GOLD = "https://www.bessergold.cz/investicni-zlato.html?product_list_limit=all";
    private static final String SEARCH_URL_SILVER = "https://www.bessergold.cz/investicni-stribro.html?product_list_limit=all";
    private static final String SEARCH_URL_PLATINUM = "https://www.bessergold.cz/investicni-platina.html?product_list_limit=all";
    private static final String SEARCH_URL_PALLADIUM = "https://www.bessergold.cz/investicni-palladium.html?product_list_limit=all";

    private static final String X_PATH_PRODUCT_LIST = "//li[@class='item product product-item']";
    private static final String X_PATH_PRODUCT_NAME = ".//span[@class='base']";
    private static final String X_PATH_BUY_PRICE = ".//span[@class='price']";
    private static final String X_PATH_REDEMPTION_PRICE = ".//div[@class='vykupni-cena']";

    public SilverumMetalScraper(){}

    @Override
    public List<Link> scrapAllLinks(WebClient webClient) {
        List<Link> elements = new ArrayList<>();
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_GOLD)));
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_SILVER)));
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_PLATINUM)));
        elements.addAll(scrapLinks(loadPage(webClient, SEARCH_URL_PALLADIUM)));
        return elements;
    }

    /////// PRICE

    @Override
    public String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        return redemptionPriceHtml.asText().split(":")[1];          // Aktuální výkupní cena (bez DPH): xxxx,xx Kč
    }

    @Override
    public List<HtmlElement> scrapProductList(HtmlPage page) {
        return page.getByXPath(X_PATH_PRODUCT_LIST);
    }

    @Override
    public String scrapProductName(HtmlPage page) {
        return ((HtmlElement) page.getFirstByXPath(X_PATH_PRODUCT_NAME)).asText();
    }

    @Override
    public double scrapBuyPrice(HtmlPage page) {
        return scrapBuyPrice(page, X_PATH_BUY_PRICE);
    }

    @Override
    public double scrapRedemptionPrice(HtmlPage page) {
        return scrapRedemptionPrice(page, X_PATH_REDEMPTION_PRICE);
    }

    /////// LINK

    @Override
    public Link scrapLink(HtmlElement elementProduct) {
        HtmlAnchor itemAnchor = elementProduct.getFirstByXPath(".//strong[@class='product name product-item-name']/a");
        if(itemAnchor != null) {
            return new Link(Dealer.BESSERGOLD_CZ, itemAnchor.getHrefAttribute());
        }
        System.out.println("Error: "+ elementProduct.asText());
        return null;
    }
}
