package home.holymiko.investment.scraper.app.server.type.dto.create;

import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.type.enums.Producer;
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
