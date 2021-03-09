package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;

import javax.persistence.*;
import java.util.Comparator;
import java.util.List;

@Entity
public class Product {

    @Id
    @GeneratedValue
    private long id;
    private Metal metal;
    private String name;
    private double grams;
    @OneToOne
    private Link link;
    @OneToOne
    private Price latestPrice;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Price> prices;

    public Product() {
    }

    public Product(Metal metal, String name, double grams, Link link, Price latestPrice, List<Price> prices) {
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

    public String getMetalString() {
        return metal.toString();
    }

    @Override
    public String toString() {
        return name +
                ", grams=" + grams +
                ", prices=" + prices;
    }

    public static Comparator<Product> ProductSplitComparator
            = new Comparator<Product>() {

        public int compare(Product product1, Product product2) {

            Double split1 = product1.getLatestPrice().getSplit();
            Double split2 = product2.getLatestPrice().getSplit();

            //ascending order
//            return split1.compareTo(split2);

            //descending order
            return split2.compareTo(split1);
        }

    };

    public static Comparator<Product> ProductPerGramComparator
            = (Product1, Product2) -> {

        Double ProductName1 = Product1.getLatestPrice().getPricePerGram();
        Double ProductName2 = Product2.getLatestPrice().getPricePerGram();

        //ascending order
        return ProductName1.compareTo(ProductName2);

        //descending order
//                return ProductName2.compareTo(ProductName1);
    };
    public static Comparator<Product> ProductPriceComparator
            = (Product1, Product2) -> {

        Double ProductName1 = Product1.getLatestPrice().getPrice();
        Double ProductName2 = Product2.getLatestPrice().getPrice();

        //ascending order
        return ProductName1.compareTo(ProductName2);

        //descending order
//                return ProductName2.compareTo(ProductName1);
    };
}
