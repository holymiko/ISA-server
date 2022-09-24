package home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metal.dealerMetalClient;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import org.apache.commons.lang3.NotImplementedException;

public interface MetalClientInterface extends ProductListInterface, RedemptionInterface {

    default HtmlPage getPage(final String link) throws ResourceNotFoundException {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default String scrapNameFromProductPage(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default double scrapPriceFromProductPage(HtmlPage productDetailPage) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default String scrapPriceFromProductPage(HtmlPage page, String xPathBuyPrice) {
        try {
            return ((HtmlElement) page.getFirstByXPath(xPathBuyPrice)).asText();
        } catch (Exception e) {
            System.out.println("WARNING - Kupni cena = 0");
        }
        return "0.0";
    }

}
