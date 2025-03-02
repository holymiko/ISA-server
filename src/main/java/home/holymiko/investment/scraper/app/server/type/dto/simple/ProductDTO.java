package home.holymiko.investment.scraper.app.server.type.dto.simple;

import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductDTO {

    private final long id;
    private final String name;
    private final Metal metal;
    private final Form form;
    private final double grams;
    private final boolean isTopProduct;

}
