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
    private List<Investment> investments;

    public Portfolio(List<Investment> investments, String owner) {
        this.investments = investments;
        this.owner = owner;
    }

    public Portfolio() {
    }

    public long getId() {
        return id;
    }

    public double getYield() {
        return yield;
    }

    public String getTextYield() {
        if (yield >= 1)
            return "+" + String.format("%.2f", (yield - 1) * 100) + "%";
        return "-" + String.format("%.2f", (100 - yield * 100)) + "%";
    }

    public double getBeginPrice() {
        return beginPrice;
    }

    public void setBeginPrice() {
        double total = 0;
        for (Investment investment : this.investments) {
            total += investment.getBeginPrice();
        }
        beginPrice = total;
    }

    public void setValue() {
        double totalRedemption = 0;
        for (Investment investment : this.investments)
            totalRedemption += investment.getProduct().getLatestPrice().getRedemption();
        value = totalRedemption;
    }

    public double getValue() {
        return value;
    }

    public void setYield() {
        setValue();
        yield = getValue() / this.beginPrice;
    }

    public List<Investment> getInvestments() {
        return investments;
    }

    public String getOwner() {
        return owner;
    }

}
