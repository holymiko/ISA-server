package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Availability;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@XmlRootElement(name = "pricePairHistory")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class PricePairHistory {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    @OneToOne
    private Price sellPrice;
    @OneToOne
    private Price redemption;
    private Availability availability;
    private Long linkId;

    public PricePairHistory(Price sellPrice, Price redemption, Availability availability, Long linkId) {
        this.sellPrice = sellPrice;
        this.redemption = redemption;
        this.availability = availability;
        this.linkId = linkId;
    }
}
