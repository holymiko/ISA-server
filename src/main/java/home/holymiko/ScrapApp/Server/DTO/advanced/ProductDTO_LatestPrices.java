package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.Entity.Price;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO_LatestPrices {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final List<String> links;
    private final List<Price> latestPrices;

    public ProductDTO_LatestPrices(long id, String metal, String name, double grams, List<String> link, List<Price> latestPrices) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.links = link;
        this.latestPrices = latestPrices;
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
