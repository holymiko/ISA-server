package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.Entity.Price;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ProductDTO_LatestPrices {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final List<String> links;
    private final List<PriceDTO> latestPrices;

    public ProductDTO_LatestPrices(long id, String metal, String name, double grams, List<String> links, List<PriceDTO> latestPrices) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.links = links;
        this.latestPrices = latestPrices;
    }
}
