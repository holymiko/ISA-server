package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.util.Pair;

import java.util.List;

public interface RedemptionInterface {

    default List<Pair<String, Double>> scrapRedemptionFromList() {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default double scrapRedemptionPrice(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default String scrapRedemptionPrice(HtmlPage page, String xPathRedemptionPrice) {
        try {
            return redemptionHtmlToText(page.getFirstByXPath(xPathRedemptionPrice));
        } catch (Exception e) {
            System.out.println("WARNING - Vykupni cena = 0");
        }
        return "0.0";
    }

    default String redemptionHtmlToText(HtmlElement redemptionPriceHtml) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }
}
