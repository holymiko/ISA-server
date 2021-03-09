package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;

import javax.persistence.*;
import java.util.List;

@Entity
public class AvatarMetal {
    @Id
    @GeneratedValue
    private long id;
    private final Dealer dealer;
    private final Producer producer;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Product> productList;

    private double AverageBuyPrice;
    private double AverageRedemption;
    private int productsCount;

    public AvatarMetal(Producer producer, Dealer dealer) {
        this.producer = producer;
        this.dealer = dealer;
    }

    public AvatarMetal(Producer producer, Dealer dealer, double averageBuyPrice, double averageRedemption, int productsCount) {
        this.producer = producer;
        this.dealer = dealer;
        AverageBuyPrice = averageBuyPrice;
        AverageRedemption = averageRedemption;
        this.productsCount = productsCount;
    }

    public void setAverageBuyPrice(double averageBuyPrice) {
        AverageBuyPrice = averageBuyPrice;
    }

    public void setAverageRedemption(double averageRedemption) {
        AverageRedemption = averageRedemption;
    }

    public void setProductsCount(int productsCount) {
        this.productsCount = productsCount;
    }

    public long getId() {
        return id;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public Producer getProducer() {
        return producer;
    }

    public double getAverageBuyPrice() {
        return AverageBuyPrice;
    }

    public double getAverageRedemption() {
        return AverageRedemption;
    }

    public int getProductsCount() {
        return productsCount;
    }
}
