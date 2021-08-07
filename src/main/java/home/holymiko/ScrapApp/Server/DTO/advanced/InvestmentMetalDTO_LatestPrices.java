package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.InvestmentMetalDTO;
import home.holymiko.ScrapApp.Server.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentMetalDTO_LatestPrices extends InvestmentMetalDTO {

    private final ProductDTO_LatestPrices product;

    public InvestmentMetalDTO_LatestPrices(long id, Dealer dealer, double yield, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate, ProductDTO_LatestPrices product) {
        super(id, dealer, yield, beginPrice, endPrice, beginDate, endDate);
        this.product = product;
    }
}
