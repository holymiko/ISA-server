package home.holymiko.ScrapApp.Server.Entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Price {

    @Id
    private LocalDateTime dateTime;
    private double price;
    private double redemption;
    private double split;
    private double pricePerGram;

    public Price() {
    }

    public Price(LocalDateTime dateTime, double price, double redemption, double grams) {
        this.dateTime = dateTime;
        this.price = price;
        this.redemption = redemption;
        this.split = redemption / price;
        this.pricePerGram = price / grams;
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

    public double getSplit() {
        return split;
    }

    public double getPricePerGram() {
        return pricePerGram;
    }

    @Override
    public String toString() {
        return Double.toString(price);
    }
}
