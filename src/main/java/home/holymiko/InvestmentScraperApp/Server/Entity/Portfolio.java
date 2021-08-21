package home.holymiko.InvestmentScraperApp.Server.Entity;

import lombok.Getter;

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
    private List<InvestmentMetal> investmentMetals;

    public Portfolio(List<InvestmentMetal> investmentMetals, String owner) {
        this.investmentMetals = investmentMetals;
        this.owner = owner;
    }

    public Portfolio() {
    }

    public double getBeginPrice() {
        return this.getInvestmentMetals()
                .stream()
                .map(
                        InvestmentMetal::getBeginPrice
                ).reduce(0.0, Double::sum);
    }

    public double getPortfolioValue() {
        return this.getInvestmentMetals()
                .stream()
                .map(
                        investmentMetal ->
                                investmentMetal.getProduct().getPriceByBestRedemption().getRedemption()
                ).reduce(0.0, Double::sum);
    }

}
