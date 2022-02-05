package home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Producer;
import lombok.Getter;

@Getter
public class ProductDTO2 {

    private final String name;
    private final Metal metal;
    private final Form form;
    private final Producer producer;
    private final double grams;
    private final int year;

    public ProductDTO2(String name, Metal metal, Form form, Producer producer, double grams, int year) {
        this.name = name;
        this.metal = metal;
        this.form = form;
        this.producer = producer;
        this.grams = grams;
        this.year = year;
    }
}
