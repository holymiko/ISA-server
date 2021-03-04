package home.holymiko.ScrapApp.Server.DTO;

import home.holymiko.ScrapApp.Server.Entity.Product;
import java.time.LocalDate;

public class InvestmentDTO {
    private final long id;
    private final ProductDTO product;
    private final double yield;
    private final double beginPrice;
    private final double endPrice;
    private final LocalDate beginDate;
    private final LocalDate endDate;

    public InvestmentDTO(long id, ProductDTO product, double yield, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate) {
        this.id = id;
        this.product = product;
        this.yield = yield;
        this.beginPrice = beginPrice;
        this.endPrice = endPrice;
        this.beginDate = beginDate;
        this.endDate = endDate;
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

    public ProductDTO getProduct() {
        return product;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
