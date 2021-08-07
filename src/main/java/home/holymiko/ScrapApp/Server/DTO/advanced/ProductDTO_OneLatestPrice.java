package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import lombok.Getter;

@Getter
public class ProductDTO_OneLatestPrice {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final PriceDTO latestPrice;

    public ProductDTO_OneLatestPrice(long id, String metal, String name, double grams, PriceDTO latestPrice) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.latestPrice = latestPrice;
    }
}
