package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Getter
public class Product {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private Producer producer;
    private Form form;
    private Metal metal;
    // TODO Convert to Double, test on the frontEnd as well
    private double grams;
    private int year;
    private boolean isSpecial;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Link> links;      // + Dealer

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<PricePair> latestPricePairs;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<PricePair> pricePairs;

    public Product() {
    }

    public Product(String name, Producer producer, Form form, Metal metal, double grams, int year, boolean isSpecial) {
        this.name = name;
        this.producer = producer;
        this.form = form;
        this.metal = metal;
        this.grams = grams;
        this.year = year;
        this.isSpecial = isSpecial;
        this.links = new ArrayList<>();
        this.latestPricePairs = new ArrayList<>();
        this.pricePairs = new ArrayList<>();
    }


    public PricePair getLatestPriceByDealer(Dealer dealer) {
        return this.latestPricePairs.stream()
                .filter(
                        price -> price.getDealer() == dealer
                ).collect(Collectors.toList()).get(0);
    }

    public PricePair getPriceByBestRedemption() {
        if(latestPricePairs == null || latestPricePairs.isEmpty()) {
            return null;
        }
        PricePair max = latestPricePairs.get(0);
        for (PricePair pricePair : latestPricePairs) {
            if(pricePair.getRedemption().getAmount() > max.getRedemption().getAmount()) {
                max = pricePair;
            }
        }
        return max;
    }

    public List<String> getLinksAsString() {
        return links
                .stream()
                .map(Link::getUrl)
                .collect(Collectors.toList());
    }

    public void setLatestPricePairs(List<PricePair> latestPricePairs) {
        this.latestPricePairs = latestPricePairs;
    }

    public void setLatestPrice(PricePair latestPricePair) {
        if(this.latestPricePairs == null) {
            this.latestPricePairs = Collections.singletonList(latestPricePair);
            return;
        }
        this.latestPricePairs = this.latestPricePairs.stream()
                .filter(price -> price.getDealer() != latestPricePair.getDealer())      // Filter Dealer to prevent duplicate
                .collect(Collectors.toList());
        this.latestPricePairs.add(latestPricePair);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(List<Link> link) {
        this.links = link;
    }

    public void setPricePairs(List<PricePair> pricePairs) {
        this.pricePairs = pricePairs;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", producer=" + producer +
                ", form=" + form +
                ", metal=" + metal +
                ", grams=" + grams +
                ", year=" + year +
                ", isSpecial=" + isSpecial +
                ",\n links=" + links +
//                ", latestPricePairs=" + latestPricePairs +
//                ", pricePairs=" + pricePairs +
                '}';
    }
}
