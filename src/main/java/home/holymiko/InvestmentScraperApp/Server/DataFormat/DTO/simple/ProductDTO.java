package home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple;

import lombok.Getter;

@Getter
public class ProductDTO {

    private final long id;
    private final String metal;
    private final String name;
    private final double grams;

    public ProductDTO(long id, String metal, String name, double grams) {
        this.id = id;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
    }
}
