package home.holymiko.InvestmentScraperApp.Server.Scraper.sources;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Scraper.ClientInterface;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;

import java.util.List;
import java.util.stream.Collectors;

public interface MetalScraperInterface extends ClientInterface {

    List<Link> scrapAllLinks(WebClient client);

    String redemptionHtmlToText(HtmlElement redemptionPriceHtml);

    String scrapProductName(HtmlPage page);

    List<HtmlElement> scrapProductList(HtmlPage page);

    double scrapBuyPrice(HtmlPage productDetailPage);

    double scrapRedemptionPrice(HtmlPage page);

    Link scrapLink(HtmlElement elementProduct);

    default Link scrapLink(HtmlElement elementProduct, String xPathToLink, Dealer dealer, String baseUrl) {
        HtmlAnchor itemAnchor = elementProduct.getFirstByXPath(xPathToLink);
        if(itemAnchor == null) {
            System.out.println("Error: "+ elementProduct.asText());
            return null;
        }
        return new Link(dealer, baseUrl + itemAnchor.getHrefAttribute());
    }

    /**
     * Finds list of elements, based on class variable xPathProductList
     * For each calls scrapLink abstract method.
     * @param page
     * @return
     */
    default List<Link> scrapLinks(HtmlPage page) {
        // Scraps new link for each element
        return scrapProductList(page).stream()
                .map(
                        this::scrapLink
                ).collect(Collectors.toList());
    }

    default String scrapBuyPrice(HtmlPage page, String xPathBuyPrice) {
        try {
            return ((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText();
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
        }
        return "0.0";
    }

    default String scrapRedemptionPrice(HtmlPage page, String xPathRedemptionPrice) {
        try {
            return redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice));
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
        }
        return "0.0";
    }
}
