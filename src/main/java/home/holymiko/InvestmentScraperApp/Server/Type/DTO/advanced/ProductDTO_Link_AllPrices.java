package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_Link_AllPrices extends ProductDTO {

    private List<LinkDTO_Price> latestPrices;
    private List<LinkDTO_Prices> prices;

    public ProductDTO_Link_AllPrices(long id, String name, Metal metal, Form form, double grams, List<LinkDTO_Price> latestPrices, List<LinkDTO_Prices> prices) {
        super(id, name, metal, form, grams);
        this.prices = prices;
        this.latestPrices = latestPrices;
    }
}
