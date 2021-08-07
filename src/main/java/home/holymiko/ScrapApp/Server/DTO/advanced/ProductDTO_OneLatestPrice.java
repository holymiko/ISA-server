package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.ProductDTO;
import lombok.Getter;

@Getter
public class ProductDTO_OneLatestPrice extends ProductDTO {

    private final PriceDTO latestPrice;

    public ProductDTO_OneLatestPrice(long id, String metal, String name, double grams, PriceDTO latestPrice) {
        super(id, metal, name, grams);
        this.latestPrice = latestPrice;
    }
}
