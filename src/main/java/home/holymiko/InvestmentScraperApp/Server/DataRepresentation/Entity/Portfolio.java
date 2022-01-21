package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity;

import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Portfolio {
    @Id
    @GeneratedValue
    private long id;
    private String owner;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<InvestmentMetal> investmentMetals;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<InvestmentStock> investmentStocks;

    public Portfolio(String owner, List<InvestmentMetal> investmentMetals, List<InvestmentStock> investmentStocks) {
        this.owner = owner;
        this.investmentMetals = investmentMetals;
        this.investmentStocks = investmentStocks;
    }

    public Portfolio() {
    }

    public double getBeginPrice() {
        return investmentMetals.stream()
                .map(
                        InvestmentMetal::getBeginPrice
                ).reduce(0.0, Double::sum)
            +
                investmentStocks.stream()
                .map(
                        investmentStock
                                -> investmentStock.getBeginPrice() * investmentStock.getAmount()
                ).reduce(0.0, Double::sum);
    }

    public double getPortfolioValue() {
        return investmentMetals.stream()
                .map(
                        investmentMetal ->
                                investmentMetal.getProduct().getPriceByBestRedemption().getRedemption()
                ).reduce(0.0, Double::sum)
            +
                investmentStocks.stream()
                .map(
                        investmentStock
                                -> investmentStock.getStock().getPreviousClose() * investmentStock.getAmount()
                ).reduce(0.0, Double::sum);
    }

}
