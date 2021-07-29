package home.holymiko.ScrapApp.Server.DTO;

import home.holymiko.ScrapApp.Server.Entity.Price;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final List<String> links;
    private final List<Price> latestPrices;
    private final List<LocalDateTime> prices;

    public ProductDTO(long id, String metal, String name, double grams, List<String> link, List<Price> latestPrices, List<LocalDateTime> prices) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.links = link;
        this.latestPrices = latestPrices;
        this.prices = prices;
    }

    public List<Price> getLatestPrices() {
        return this.latestPrices;
    }

    public double getGrams() {
        return grams;
    }

    public long getId() {
        return id;
    }

    public List<LocalDateTime> getPrices() {
        return prices;
    }

    public String getName() {
        return name;
    }

    public List<String> getLinks() {
        return links;
    }

    public String getMetal() {
        return metal;
    }

}
