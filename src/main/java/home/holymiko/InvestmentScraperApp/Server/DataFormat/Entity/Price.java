package home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import lombok.Getter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Price {

    @Id
    private LocalDateTime dateTime;
    private double price;
    private double redemption;
    private Dealer dealer;

    public Price() {
    }

    public Price(LocalDateTime dateTime, double price, double redemption, Dealer dealer) {
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
