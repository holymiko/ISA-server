package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.ProductCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@NoArgsConstructor
public class Product {

    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private long id;
    private String name;
    private Producer producer;
    private Form form;
    private Metal metal;
    // TODO Convert to Double, test on the frontEnd as well
    private double grams;
    private int year;
    private boolean isSpecial;

    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Link> links = new ArrayList<>();      // includes Dealer

    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<PricePair> pricePairs = new ArrayList<>();

    public Product(String name, Producer producer, Form form, Metal metal, double grams, int year, boolean isSpecial) {
        this.name = name;
        this.producer = producer;
        this.form = form;
        this.metal = metal;
        this.grams = grams;
        this.year = year;
        this.isSpecial = isSpecial;
    }

    public Product(ProductCreateDTO productCreateDTO) {
        this.name = productCreateDTO.getName();
        this.producer = productCreateDTO.getProducer();
        this.form = productCreateDTO.getForm();
        this.metal = productCreateDTO.getMetal();
        this.grams = productCreateDTO.getGrams();
        this.year = productCreateDTO.getYear();
        this.isSpecial = productCreateDTO.isSpecial();
    }


    public List<String> getLinksAsString() {
        return links
                .stream()
                .map(Link::getUri)
                .collect(Collectors.toList());
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLinks(List<Link> link) {
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
