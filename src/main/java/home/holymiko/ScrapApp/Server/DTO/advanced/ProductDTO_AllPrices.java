package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.Entity.Price;

import java.util.List;

public class ProductDTO_AllPrices {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final List<String> links;
    private final List<Price> latestPrices;
    private final List<Price> prices;

    public ProductDTO_AllPrices(long id, String metal, String name, double grams, List<String> links, List<Price> latestPrices, List<Price> prices) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.links = links;
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

    public List<Price> getPrices() {
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
