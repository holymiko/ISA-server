package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.dealerMetalScraper;


import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public interface MetalScraperInterface {

    List<Link> scrapAllLinks(WebClient client);

    String redemptionHtmlToText(HtmlElement redemptionPriceHtml);

    String scrapProductName(HtmlPage page);

    List<HtmlElement> scrapProductList(HtmlPage page);

    double scrapBuyPrice(HtmlPage page);

    double scrapRedemptionPrice(HtmlPage page);

    /**
     *
     * @param elementProduct - HtmlElement of page listing products
     * @return
     */
    Link scrapLink(HtmlElement elementProduct);

    /**
     * Finds list of elements, based on class variable xPathProductList
     * For each calls scrapLink abstract method.
     * @param searchUrlList URL of page, where the search will be done
     * @return
     */
    default List<Link> scrapLinks(WebClient client, String searchUrlList) {
        HtmlPage page;
        try {
            page = client.getPage(searchUrlList);
        } catch (IOException e) {
            return null;
        }
        // Scraps new link for each element
        return scrapProductList(page).stream()
                .map(
                        this::scrapLink
                ).collect(Collectors.toList());
    }

    default double scrapBuyPrice(HtmlPage page, String xPathBuyPrice) {
        try {
            return Convert.currencyToNumberConvert(
                    ((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText()
            );
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
        }
        return 0.0;
    }

    default double scrapRedemptionTime(HtmlPage page, String xPathRedemptionPrice) {
        try {
            return Convert.currencyToNumberConvert(
                    redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice))
            );
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
        }
        return 0.0;
    }
}
