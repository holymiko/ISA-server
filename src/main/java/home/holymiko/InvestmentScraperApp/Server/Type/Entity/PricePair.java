package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.AccessLevel;
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
    @Setter(AccessLevel.NONE)
    private long id;
    @OneToOne
    private Price sellPrice;
    @OneToOne
    private Price redemption;
    private Long linkId;

    public PricePair(Price sellPrice, Price redemption, Long linkId) {
        this.sellPrice = sellPrice;
        this.redemption = redemption;
        this.linkId = linkId;
    }
}
