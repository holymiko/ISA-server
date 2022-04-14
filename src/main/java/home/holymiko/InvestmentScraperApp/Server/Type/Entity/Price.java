package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Price {

    @Id
    private LocalDateTime dateTime;
    private Double price;
    private Double redemption;
    private Dealer dealer;

    public Price() {
    }

    public Price(LocalDateTime dateTime, Double price, Double redemption, Dealer dealer) {
        this.dateTime = dateTime;
        this.price = price;
        this.redemption = redemption;
        this.dealer = dealer;
    }

    @Override
    public String toString() {
        return Double.toString(price);
    }
}
