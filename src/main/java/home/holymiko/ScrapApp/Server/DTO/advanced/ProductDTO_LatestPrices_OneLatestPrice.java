package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.ProductDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_LatestPrices_OneLatestPrice extends ProductDTO {

    private final List<PriceDTO> latestPrices;
    private final PriceDTO latestPrice;

    public ProductDTO_LatestPrices_OneLatestPrice(long id, String metal, String name, double grams, List<PriceDTO> latestPrices, PriceDTO latestPrice) {
        super(id, metal, name, grams);
        this.latestPrices = latestPrices;
        this.latestPrice = latestPrice;
    }
}
