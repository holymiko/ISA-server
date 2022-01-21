package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentStockDTO {
    private final long id;
    private final Integer amount;
    private final double beginPrice;
    private final double endPrice;
    private final LocalDate beginDate;
    private final LocalDate endDate;

    public InvestmentStockDTO(long id, Integer amount, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate) {
        this.id = id;
        this.amount = amount;
        this.beginPrice = beginPrice;
        this.endPrice = endPrice;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }
}
