package home.holymiko.investment.scraper.app.server.type.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.time.LocalDate;

@XmlRootElement(name = "investmentStock")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@NoArgsConstructor
public class InvestmentStock {
    @Id
    @GeneratedValue
    private long id;
    @ManyToOne(fetch = FetchType.EAGER)             // To avoid problem with Hibernate closing session
    @Fetch(FetchMode.SELECT)                        // To avoid duplicates
    private StockGraham stockGraham;
    private Integer amount;
    private double beginPrice;
    private double endPrice;
    private LocalDate beginDate;
    private LocalDate endDate;

    public InvestmentStock(StockGraham stockGraham, Integer amount, double beginPrice, LocalDate beginDate) {
        this.stockGraham = stockGraham;
        this.amount = amount;
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

}
