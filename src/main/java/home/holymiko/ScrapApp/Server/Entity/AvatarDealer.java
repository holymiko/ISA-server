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
    private List<AvatarMetal> avatarMetalList;

    private double AverageBuyPrice;                         // Computed Values
    private double AverageRedemption;
    private int productsCount;

    public AvatarDealer(Dealer dealer, List<AvatarMetal> avatarMetalList) {
        this.dealer = dealer;
        this.avatarMetalList = avatarMetalList;
    }

    public AvatarDealer(Dealer dealer, List<AvatarMetal> avatarMetalList, double averageBuyPrice, double averageRedemption, int productsCount) {
        this.dealer = dealer;
        this.avatarMetalList = avatarMetalList;
        AverageBuyPrice = averageBuyPrice;
        AverageRedemption = averageRedemption;
        this.productsCount = productsCount;
    }

    public void setAvatarProducerList(List<AvatarMetal> avatarMetalList) {
        this.avatarMetalList = avatarMetalList;
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

    public List<AvatarMetal> getAvatarProducerList() {
        return avatarMetalList;
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
