package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PricePairDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_LatestPrices extends ProductDTO {

    private List<String> links;
    private List<PricePairDTO> latestPrices;

    public ProductDTO_LatestPrices(long id, String name, Metal metal, Form form, double grams, List<String> links, List<PricePairDTO> latestPrices) {
        super(id, name, metal, form, grams);
        this.links = links;
        this.latestPrices = latestPrices;
    }
}
