package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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

    public PricePair() {
    }

    public PricePair(Dealer dealer, Price sellPrice, Price redemption, Long productId) {
        this.dealer = dealer;
        this.sellPrice = sellPrice;
        this.redemption = redemption;
        this.productId = productId;
    }
}
