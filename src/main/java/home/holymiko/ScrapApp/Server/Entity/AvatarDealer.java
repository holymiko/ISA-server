package home.holymiko.ScrapApp.Server.Entity;


import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;

import javax.persistence.*;
import java.util.List;

@Entity
public class AvatarDealer {
    @Id
    @GeneratedValue
    private long id;
    private final Dealer dealer;
    @OneToMany(fetch = FetchType.EAGER)
    private List<AvatarProducer> avatarProducerList;

    private double AverageBuyPrice;                         // Computed Values
    private double AverageRedemption;
    private int productsCount;

    public AvatarDealer(Dealer dealer, List<AvatarProducer> avatarProducerList) {
        this.dealer = dealer;
        this.avatarProducerList = avatarProducerList;
    }

    public AvatarDealer(Dealer dealer, List<AvatarProducer> avatarProducerList, double averageBuyPrice, double averageRedemption, int productsCount) {
        this.dealer = dealer;
        this.avatarProducerList = avatarProducerList;
        AverageBuyPrice = averageBuyPrice;
        AverageRedemption = averageRedemption;
        this.productsCount = productsCount;
    }

    public void setAvatarProducerList(List<AvatarProducer> avatarProducerList) {
        this.avatarProducerList = avatarProducerList;
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

    public List<AvatarProducer> getAvatarProducerList() {
        return avatarProducerList;
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
