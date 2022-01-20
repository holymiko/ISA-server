package home.holymiko.InvestmentScraperApp.Server.Entity;

import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
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
    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public InvestmentStock(Stock stock, double beginPrice, LocalDate beginDate) {
        this.stock = stock;
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public InvestmentStock() {
    }

}
