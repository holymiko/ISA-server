package home.holymiko.InvestmentScraperApp.Server.Entity;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class Investment {

    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public Investment(double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate) {
        this.beginPrice = beginPrice;
        this.endPrice = endPrice;
        this.beginDate = beginDate;
        this.endDate = endDate;
    }

    public Investment(double beginPrice, LocalDate beginDate) {
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public Investment() {
    }
}
