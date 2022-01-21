package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity;

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
    private Stock stock;
    private Integer amount;
    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public InvestmentStock(Stock stock, Integer amount, double beginPrice, LocalDate beginDate) {
        this.stock = stock;
        this.amount = amount;
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public InvestmentStock() {
    }

}
