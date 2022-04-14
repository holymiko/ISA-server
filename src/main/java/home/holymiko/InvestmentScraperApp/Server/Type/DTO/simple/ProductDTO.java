package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;

@Getter
public class ProductDTO {

    private final long id;
    private final String name;
    private final Metal metal;
    private final Form form;
    private final double grams;

    public ProductDTO(long id, String name, Metal metal, Form form, double grams) {
        this.id = id;
        this.name = name;
        this.metal = metal;
        this.form = form;
        this.grams = grams;
    }
}
