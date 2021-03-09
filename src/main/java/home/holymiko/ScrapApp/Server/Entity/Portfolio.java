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
        this.setBeginPrice();
        this.setYield();
    }

    public Portfolio() {
    }


    ///// MY SET

    public void setBeginPrice() {
        double total = 0;
        for (Investment investment : this.investments) {
            total += investment.getBeginPrice();
        }
        this.beginPrice = total;
    }

    public void setYield() {
        this.setValue();
        this.yield = this.value / this.beginPrice;
    }

    public void setValue() {
        double totalRedemption = 0;
        for (Investment investment : this.investments)
            totalRedemption += investment.getProduct().getLatestPrice().getRedemption();
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

    public List<Investment> getInvestments() {
        return investments;
    }

    public String getOwner() {
        return owner;
    }

}
