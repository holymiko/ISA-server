package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.Entity.Price;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_AllPrices {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final List<String> links;
    private final List<PriceDTO> latestPrices;
    private final List<PriceDTO> prices;

    public ProductDTO_AllPrices(long id, String metal, String name, double grams, List<String> links, List<PriceDTO> latestPrices, List<PriceDTO> prices) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.links = links;
        this.latestPrices = latestPrices;
        this.prices = prices;
    }
}
