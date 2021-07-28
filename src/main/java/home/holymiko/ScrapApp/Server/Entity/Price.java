package home.holymiko.ScrapApp.Server.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Price {

    @Id
    private LocalDateTime dateTime;
    private double price;
    private double redemption;

    public Price() {
    }

    public Price(LocalDateTime dateTime, double price, double redemption, double grams) {
        this.dateTime = dateTime;
        this.price = price;
        this.redemption = redemption;
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

    @Override
    public String toString() {
        return Double.toString(price);
    }
}
