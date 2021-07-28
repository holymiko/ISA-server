package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
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

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public double getPrice() {
        return price;
    }

    public double getRedemption() {
        return redemption;
    }

    public Dealer getDealer() {
        return dealer;
    }

    @Override
    public String toString() {
        return Double.toString(price);
    }
}
