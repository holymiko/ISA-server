package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class InvestmentStock {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)             // To avoid problem with Hibernate closing session
    @Fetch(FetchMode.SELECT)                        // To avoid duplicates
    private GrahamStock grahamStock;
    private Integer amount;
    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public InvestmentStock(GrahamStock grahamStock, Integer amount, double beginPrice, LocalDate beginDate) {
        this.grahamStock = grahamStock;
        this.amount = amount;
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public InvestmentStock() {
    }

}
