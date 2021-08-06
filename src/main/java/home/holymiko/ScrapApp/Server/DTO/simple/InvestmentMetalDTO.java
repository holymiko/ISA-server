package home.holymiko.ScrapApp.Server.DTO.simple;

import home.holymiko.ScrapApp.Server.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.ScrapApp.Server.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentMetalDTO {
    private final long id;
    private final ProductDTO_LatestPrices product;
    private final Dealer dealer;
    private final double yield;
    private final double beginPrice;
    private final double endPrice;
    private final LocalDate beginDate;
    private final LocalDate endDate;

    public InvestmentMetalDTO(long id, ProductDTO_LatestPrices product, Dealer dealer, double yield, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate) {
        this.id = id;
        this.product = product;
        this.dealer = dealer;
        this.yield = yield;
        this.beginPrice = beginPrice;
        this.endPrice = endPrice;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}