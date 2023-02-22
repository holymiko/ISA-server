package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "investmentMetal")
@XmlAccessorType(XmlAccessType.FIELD)
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
//      TODO Change beginPrice to bestRedemption/bestBuyOut
        this.beginPrice = beginPrice;
        this.beginDate = beginDate;
    }

    public InvestmentMetal() {
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
