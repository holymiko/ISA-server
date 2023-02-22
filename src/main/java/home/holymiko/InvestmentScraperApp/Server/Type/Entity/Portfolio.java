package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "portfolio")
@XmlAccessorType(XmlAccessType.FIELD)
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

}
