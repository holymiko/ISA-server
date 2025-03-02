package home.holymiko.investment.scraper.app.server.type.dto.advanced;

import home.holymiko.investment.scraper.app.server.type.dto.simple.ProductDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_Link_AllPrices extends ProductDTO {

    private List<LinkDTO_Price> latestPrices;
    private List<LinkDTO_Prices> prices;

    public ProductDTO_Link_AllPrices(long id, String name, Metal metal, Form form, double grams, boolean isTopProduct, List<LinkDTO_Price> latestPrices, List<LinkDTO_Prices> prices) {
        super(id, name, metal, form, grams, isTopProduct);
        this.prices = prices;
        this.latestPrices = latestPrices;
    }
}
