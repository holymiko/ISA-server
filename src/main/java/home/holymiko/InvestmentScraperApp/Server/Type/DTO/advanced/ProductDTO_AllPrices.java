package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_AllPrices extends ProductDTO {

    private final List<String> links;
    private final List<PriceDTO> latestPrices;
    private final List<PriceDTO> prices;

    public ProductDTO_AllPrices(long id, String name, Metal metal, Form form, double grams, List<String> links, List<PriceDTO> latestPrices, List<PriceDTO> prices) {
        super(id, name, metal, form, grams);
        this.links = links;
        this.latestPrices = latestPrices;
        this.prices = prices;
    }
}
