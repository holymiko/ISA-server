package home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.NotImplementedException;

public interface ProductDetailInterface extends ProductListInterface, SellPriceListInterface {

    default String scrapNameFromProductPage(HtmlPage page) {
        throw new NotImplementedException("Method 'scrapNameFromProductPage' haven't been implemented yet");
    }

    default double scrapBuyPriceFromProductPage(HtmlPage productDetailPage) throws NumberFormatException {
        throw new NotImplementedException("Method 'scrapBuyPriceFromProductPage' haven't been implemented yet");
    }

    default String scrapAvailabilityFromProductPage(HtmlPage productDetailPage) {
        throw new NotImplementedException("Method 'scrapAvailabilityFromProductPage' haven't been implemented yet");
    }

    default double scrapSellPriceFromProductPage(HtmlPage page) {
        throw new NotImplementedException("Method 'scrapSellPriceFromProductPage' haven't been implemented yet");
    }

}
