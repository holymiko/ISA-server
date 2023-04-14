package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "pricePair")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PricePair {

    @Id
    @GeneratedValue
    private long id;
    private Dealer dealer;
    @OneToOne
    private Price sellPrice;
    @OneToOne
    private Price redemption;
    private Long productId;

    public PricePair(Dealer dealer, Price sellPrice, Price redemption, Long productId) {
        this.dealer = dealer;
        this.sellPrice = sellPrice;
        this.redemption = redemption;
        this.productId = productId;
    }
}
