package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
public class PricePair {

    @Id
    @GeneratedValue
    private long id;
    private Dealer dealer;
    @OneToOne
    private Price sellPrice;
    @OneToOne
    private Price redemption;

    public PricePair() {
    }

    public PricePair(Dealer dealer, Price sellPrice, Price redemption) {
        this.dealer = dealer;
        this.sellPrice = sellPrice;
        this.redemption = redemption;
    }

}
