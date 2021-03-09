package home.holymiko.ScrapApp.Server.DTO;

import home.holymiko.ScrapApp.Server.Entity.Price;

import java.time.LocalDateTime;
import java.util.List;

public class ProductDTO {
    private final long id;
    private final String metal;
    private final String name;
    private final double grams;
    private final String link;
    private final Price latestPrice;
    private final List<LocalDateTime> prices;

    public ProductDTO(long id, String metal, String name, double grams, String link, Price latestPrice, List<LocalDateTime> prices) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.link = link;
        this.latestPrice = latestPrice;
        this.prices = prices;
    }

    public Price getLatestPrice() {
        return this.latestPrice;
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

    public String getLink() {
        return link;
    }

    public String getMetal() {
        return metal;
    }

}
