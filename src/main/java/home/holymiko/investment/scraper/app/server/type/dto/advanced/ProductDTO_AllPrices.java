package home.holymiko.investment.scraper.app.server.type.dto.advanced;

import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_AllPrices extends ProductDTO_LatestPrices {

    private List<PricePairDTO_Dealer> prices;

    public ProductDTO_AllPrices(long id, String name, Metal metal, Form form, double grams, boolean isTopProduct, List<String> links, List<PricePairDTO_Dealer> latestPrices, List<PricePairDTO_Dealer> prices) {
        super(id, name, metal, form, grams, isTopProduct, links, latestPrices);
        this.prices = prices;
    }
}
