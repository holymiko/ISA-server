package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple.ProductDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ProductDTO_LatestPrices extends ProductDTO {

    private List<String> links;
    private final List<PriceDTO> latestPrices;

    public ProductDTO_LatestPrices(long id, String metal, String name, double grams, List<String> links, List<PriceDTO> latestPrices) {
        super(id, metal, name, grams);
        this.links = links;
        this.latestPrices = latestPrices;
    }
}
