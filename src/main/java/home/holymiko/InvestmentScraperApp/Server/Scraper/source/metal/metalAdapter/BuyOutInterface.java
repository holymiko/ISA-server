package home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter;

import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.util.Pair;

import java.util.List;

public interface BuyOutInterface {

    default List<Pair<String, Double>> scrapBuyOutFromList() {
        throw new NotImplementedException("Method haven't been implemented yet");
    }

    default double scrapBuyOutPrice(HtmlPage page) {
        throw new NotImplementedException("Method haven't been implemented yet");
    }
}
