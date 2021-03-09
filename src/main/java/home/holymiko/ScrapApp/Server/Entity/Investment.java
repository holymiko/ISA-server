package home.holymiko.ScrapApp.Server.Entity;

import com.sun.istack.NotNull;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
public class Investment {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)             // To avoid problem with Hibernate closing session
    @Fetch(FetchMode.SELECT)                        // To avoid duplicates
    private Product product;
    private double yield;
    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public Investment(Product product, double beginPrice, LocalDate beginDate) {
        this.product = product;
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public Investment(Product product, LocalDate beginDate) {
        this.product = product;
        this.beginDate = beginDate;
    }

    public Investment(Product product) {
        this.product = product;
    }

    public Investment() {

    }

    ///// SET

    public void setYield() {
        setYield(getProduct().getLatestPrice().getRedemption() / getBeginPrice());
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
