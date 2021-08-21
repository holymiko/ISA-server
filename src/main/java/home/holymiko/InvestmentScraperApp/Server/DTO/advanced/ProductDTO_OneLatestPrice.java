package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.ProductDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO_OneLatestPrice extends ProductDTO {

    private PriceDTO latestPrice;

    public ProductDTO_OneLatestPrice(long id, String metal, String name, double grams, PriceDTO latestPrice) {
        super(id, metal, name, grams);
        this.latestPrice = latestPrice;
    }
}
