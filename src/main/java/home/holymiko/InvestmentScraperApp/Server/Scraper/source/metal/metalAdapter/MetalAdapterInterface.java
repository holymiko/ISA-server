package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.NotImplementedException;

public interface MetalAdapterInterface extends ProductListInterface, BuyOutInterface {

    default String scrapNameFromProductPage(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default double scrapPriceFromProductPage(HtmlPage productDetailPage) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

}
