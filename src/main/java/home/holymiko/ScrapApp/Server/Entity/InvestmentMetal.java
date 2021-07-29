package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class InvestmentMetal {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)             // To avoid problem with Hibernate closing session
    @Fetch(FetchMode.SELECT)                        // To avoid duplicates
    private Product product;
    private Dealer dealer;
    private double yield;
    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public InvestmentMetal(Product product, Dealer dealer, double beginPrice, LocalDate beginDate) {
        this.product = product;
        this.dealer = dealer;
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public InvestmentMetal() {
    }


    ///// SET

    public void setYield() {
        setYield(getProduct().getLatestPriceByDealer(dealer).getRedemption() / getBeginPrice());
    }

    public void setYield(double yield) {
        this.yield = yield;
    }


    ///// GET

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

    public double getEndPrice() {
        return endPrice;
    }

    public long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public LocalDate getBeginDate() {
        return beginDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return product.toString() +
                ", beginPrice=" + beginPrice +
                ", endPrice=" + endPrice +
                ", beginDate=" + beginDate +
                ", endDate=" + endDate;
    }
}
