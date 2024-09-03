package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductCreateDTO {

    private final String name;
    private final Producer producer;
    private final Form form;
    private final Metal metal;
    private final Double grams;
    private final int year;
    private final boolean isHidden;
    private final boolean isTopProduct;

}
