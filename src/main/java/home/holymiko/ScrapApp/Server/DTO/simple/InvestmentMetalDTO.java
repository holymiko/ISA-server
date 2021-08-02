package home.holymiko.ScrapApp.Server.DTO.simple;

import home.holymiko.ScrapApp.Server.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;

import java.time.LocalDate;

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

    public Dealer getDealer() {
        return dealer;
    }

    public long getId() {
        return id;
    }

    public double getYield() {
        return yield;
    }

    public double getBeginPrice() {
        return beginPrice;
    }

    public double getEndPrice() {
        return endPrice;
    }

    public ProductDTO_LatestPrices getProduct() {
        return product;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
