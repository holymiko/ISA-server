package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.ProductDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_LatestPrices extends ProductDTO {

    private final List<String> links;
    private final List<PriceDTO> latestPrices;

    public ProductDTO_LatestPrices(long id, String metal, String name, double grams, List<String> links, List<PriceDTO> latestPrices) {
        super(id, metal, name, grams);
        this.links = links;
        this.latestPrices = latestPrices;
    }
}
