package home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple.ProductDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class ProductDTO_LatestPrices_OneLatestPrice extends ProductDTO {

    private final List<PriceDTO> latestPrices;
    private final PriceDTO latestPrice;

    public ProductDTO_LatestPrices_OneLatestPrice(long id, String metal, String name, double grams, List<PriceDTO> latestPrices, PriceDTO latestPrice) {
        super(id, metal, name, grams);
        this.latestPrices = latestPrices;
        this.latestPrice = latestPrice;
    }
}
