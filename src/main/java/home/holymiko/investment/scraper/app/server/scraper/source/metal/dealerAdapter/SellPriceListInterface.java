package home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter;

import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.util.Pair;

import java.util.List;

public interface SellPriceListInterface {

    default List<Pair<String, Double>> scrapSellPriceFromList() {
        throw new NotImplementedException("Method 'scrapSellPriceFromList' haven't been implemented yet");
    }


}
