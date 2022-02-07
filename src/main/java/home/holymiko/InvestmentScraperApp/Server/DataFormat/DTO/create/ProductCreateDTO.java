package home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.create;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Producer;
import lombok.Getter;

@Getter
public class ProductCreateDTO {

    private final String name;
    private final Producer producer;
    private final Form form;
    private final Metal metal;
    private final double grams;
    private final int year;
    private final boolean isSpecial;

    public ProductCreateDTO(String name, Producer producer, Form form, Metal metal, double grams, int year, boolean isSpecial) {
        this.name = name;
        this.producer = producer;
        this.form = form;
        this.metal = metal;
        this.grams = grams;
        this.year = year;
        this.isSpecial = isSpecial;
    }
}
