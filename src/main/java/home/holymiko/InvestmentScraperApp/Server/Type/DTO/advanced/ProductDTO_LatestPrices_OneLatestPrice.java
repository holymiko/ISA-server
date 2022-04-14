package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_LatestPrices_OneLatestPrice extends ProductDTO {

    private final List<PriceDTO> latestPrices;
    private final PriceDTO latestPrice;

    public ProductDTO_LatestPrices_OneLatestPrice(long id, String name, Metal metal, Form form, double grams, List<PriceDTO> latestPrices, PriceDTO latestPrice) {
        super(id, name, metal, form, grams);
        this.latestPrices = latestPrices;
        this.latestPrice = latestPrice;
    }
}
