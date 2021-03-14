package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Form;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private Producer producer;
    private Form form;
    private Metal metal;
    private double grams;
    @OneToOne
    private Link link;      // + Dealer
    @OneToOne
    private Price latestPrice;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Price> prices;

    public Product() {
    }

    public Product(Producer producer, Form form, Metal metal, String name, double grams, Link link, Price latestPrice, List<Price> prices) {
        this.producer = producer;
        this.form = form;
        this.metal = metal;
        this.name = name;
        this.grams = grams;
        this.link = link;
        this.latestPrice = latestPrice;
        this.prices = prices;
    }

    public void setLatestPrice() {
        Price latestPrice = prices.get(0);
        for (Price price : prices) {
            if (latestPrice.getDateTime().compareTo(price.getDateTime()) < 0)
                latestPrice = price;
        }
        this.latestPrice = latestPrice;
    }

    public void setLatestPrice(Price latestPrice) {
        this.latestPrice = latestPrice;
    }

    public Price getLatestPrice() {
        return this.latestPrice;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(Link link) {
        this.link = link;
    }

    public void setPrices(List<Price> prices) {
        this.prices = prices;
    }

    public void setGrams(double grams) {
        this.grams = grams;
    }

    public Dealer getDealer() {
        return this.link.getDealer();
    }

    public double getGrams() {
        return grams;
    }

    public long getId() {
        return id;
    }

    public List<Price> getPrices() {
        return prices;
    }

    public String getName() {
        return name;
    }

    public Link getLink() {
        return link;
    }

    public Metal getMetal() {
        return metal;
    }

    public Producer getProducer() {
        return producer;
    }

    public Form getForm() {
        return form;
    }

    public String getMetalString() {
        return metal.toString();
    }

    @Override
    public String toString() {
        return name +
                ", grams=" + grams +
                ", prices=" + prices;
    }
}
