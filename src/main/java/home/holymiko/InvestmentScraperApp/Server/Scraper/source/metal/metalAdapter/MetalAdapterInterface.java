package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import org.apache.commons.lang3.NotImplementedException;

public interface MetalAdapterInterface extends ProductListInterface, BuyOutInterface {

    default HtmlPage getPage(final String link) throws ResourceNotFoundException {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default String scrapNameFromProductPage(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default double scrapPriceFromProductPage(HtmlPage productDetailPage) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

}
