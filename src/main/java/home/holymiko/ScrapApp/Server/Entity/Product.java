package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Enum.Form;
import home.holymiko.ScrapApp.Server.Enum.Metal;
import home.holymiko.ScrapApp.Server.Enum.Producer;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.Year;
import java.util.Collections;
import java.util.Comparator;
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
    private double grams;
    private int year;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Link> links;      // + Dealer

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Price> latestPrices;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Price> prices;

    public Product() {
    }

    public Product(String name, Producer producer, Form form, Metal metal, double grams, int year, List<Link> links, List<Price> latestPrices, List<Price> prices) {
        this.name = name;
        this.producer = producer;
        this.form = form;
        this.metal = metal;
        this.grams = grams;
        this.year = year;
        this.links = links;
        this.latestPrices = latestPrices;
        this.prices = prices;
    }

    public Price getLatestPriceByDealer(Dealer dealer) {
        return this.latestPrices.stream()
                .filter(
                        price -> price.getDealer() == dealer
                ).collect(Collectors.toList()).get(0);
    }

    public double getBestRedemption() {
        return this.latestPrices
                .stream()
                .map(
                        Price::getRedemption
                )
                .max(
                        Comparator.naturalOrder()
                ).get();
    }

    public void setLatestPrices(List<Price> latestPrices) {
        this.latestPrices = latestPrices;
    }

    public void setLatestPrice(Price latestPrice) {
        if(this.latestPrices == null) {
            this.latestPrices = Collections.singletonList(latestPrice);
            return;
        }
        this.latestPrices = this.latestPrices.stream()
                .filter(price -> price.getDealer() != latestPrice.getDealer())      // Filter Dealer to prevent duplicate
                .collect(Collectors.toList());
        this.latestPrices.add(latestPrice);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(List<Link> link) {
        this.links = link;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    @Override
    public String toString() {
        return name +
                ", grams=" + grams +
                ", prices=" + prices;
    }
}
