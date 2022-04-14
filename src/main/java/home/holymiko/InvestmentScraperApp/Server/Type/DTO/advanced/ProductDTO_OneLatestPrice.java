package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO_OneLatestPrice extends ProductDTO {

    private PriceDTO latestPrice;

    public ProductDTO_OneLatestPrice(long id, String name, Metal metal, Form form, double grams, PriceDTO latestPrice) {
        super(id, name, metal, form, grams);
        this.latestPrice = latestPrice;
    }
}
