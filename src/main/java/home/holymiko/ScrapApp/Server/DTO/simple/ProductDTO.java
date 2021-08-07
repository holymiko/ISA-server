package home.holymiko.ScrapApp.Server.DTO.simple;

import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import lombok.Getter;

import java.util.List;

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
