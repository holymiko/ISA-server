package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.ProductDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_AllPrices extends ProductDTO {

    private final List<String> links;
    private final List<PriceDTO> latestPrices;
    private final List<PriceDTO> prices;

    public ProductDTO_AllPrices(long id, String metal, String name, double grams, List<String> links, List<PriceDTO> latestPrices, List<PriceDTO> prices) {
        super(id, metal, name, grams);
        this.links = links;
        this.latestPrices = latestPrices;
        this.prices = prices;
    }
}
