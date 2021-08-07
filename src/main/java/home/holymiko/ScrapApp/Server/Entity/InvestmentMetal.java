package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Enum.Dealer;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class InvestmentMetal {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)             // To avoid problem with Hibernate closing session
    @Fetch(FetchMode.SELECT)                        // To avoid duplicates
    private Product product;
    private Dealer dealer;
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

    public double getYield() {
        return this.getProduct().getBestRedemption() / getBeginPrice();
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
