package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PricePairDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_AllPrices extends ProductDTO_LatestPrices {

    private List<PricePairDTO> prices;

    public ProductDTO_AllPrices(long id, String name, Metal metal, Form form, double grams, List<String> links, List<PricePairDTO> latestPrices, List<PricePairDTO> prices) {
        super(id, name, metal, form, grams, links, latestPrices);
        this.prices = prices;
    }
}
