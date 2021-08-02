package home.holymiko.ScrapApp.Server.Entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Portfolio {
    @Id
    @GeneratedValue
    private long id;
    private String owner;
    private double beginPrice;
    private double value;
    private double yield;
    @OneToMany(fetch = FetchType.EAGER)
    private List<InvestmentMetal> investmentMetals;

    public Portfolio(List<InvestmentMetal> investmentMetals, String owner) {
        this.investmentMetals = investmentMetals;
        this.owner = owner;
        this.setBeginPrice();
        this.setYield();
    }

    public Portfolio() {
    }


    ///// MY SET

    public void setBeginPrice() {
        double total = 0;
        for (InvestmentMetal investmentMetal : this.investmentMetals) {
            total += investmentMetal.getBeginPrice();
        }
        this.beginPrice = total;
    }

    public void setYield() {
        this.setValue();
        this.yield = this.value / this.beginPrice;
    }

    public void setValue() {
        double totalRedemption = 0;
        for (InvestmentMetal investmentMetal : this.investmentMetals) {
            totalRedemption += investmentMetal.getProduct().getLatestPriceByDealer( investmentMetal.getDealer() ).getRedemption();
        }
        this.value = totalRedemption;
    }


    ////// GET

    public String getTextYield() {
        if (yield >= 1)
            return "+" + String.format("%.2f", (yield - 1) * 100) + "%";
        return "-" + String.format("%.2f", (100 - yield * 100)) + "%";
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

    public double getValue() {
        return value;
    }

    public List<InvestmentMetal> getInvestments() {
        return investmentMetals;
    }

    public String getOwner() {
        return owner;
    }

}
