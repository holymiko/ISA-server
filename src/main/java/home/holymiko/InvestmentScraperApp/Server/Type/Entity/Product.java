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

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Price> latestPrices;

    @OneToMany(fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Price> prices;

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
        this.latestPrices = new ArrayList<>();
        this.prices = new ArrayList<>();
    }


    public Price getLatestPriceByDealer(Dealer dealer) {
        return this.latestPrices.stream()
                .filter(
                        price -> price.getDealer() == dealer
                ).collect(Collectors.toList()).get(0);
    }

    public Price getPriceByBestRedemption() {
        if(latestPrices == null || latestPrices.isEmpty()) {
            return null;
        }
        Price max = latestPrices.get(0);
        for (Price price : latestPrices) {
            if(price.getRedemption() > max.getRedemption()) {
                max = price;
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
//                ", latestPrices=" + latestPrices +
//                ", prices=" + prices +
                '}';
    }
}
