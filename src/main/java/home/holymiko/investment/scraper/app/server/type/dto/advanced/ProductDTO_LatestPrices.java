package home.holymiko.investment.scraper.app.server.type.dto.advanced;

import home.holymiko.investment.scraper.app.server.type.dto.simple.ProductDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_LatestPrices extends ProductDTO {

    private List<String> links;
    private List<PricePairDTO_Dealer> latestPrices;

    public ProductDTO_LatestPrices(long id, String name, Metal metal, Form form, double grams, boolean isTopProduct, List<String> links, List<PricePairDTO_Dealer> latestPrices) {
        super(id, name, metal, form, grams, isTopProduct);
        this.links = links;
        this.latestPrices = latestPrices;
    }
}
