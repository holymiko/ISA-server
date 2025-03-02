package home.holymiko.investment.scraper.app.server.type.entity;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
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

@XmlRootElement(name = "investmentMetal")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@NoArgsConstructor
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
//      TODO Change beginPrice to bestRedemption/bestBuyOut
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
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
