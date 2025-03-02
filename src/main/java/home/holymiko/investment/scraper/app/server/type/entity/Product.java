package home.holymiko.investment.scraper.app.server.type.entity;

import home.holymiko.investment.scraper.app.server.type.dto.create.ProductCreateDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.type.enums.Producer;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@Setter
@ToString
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
    private Double grams;
    private int year;

    @Column(name = "is_hidden", columnDefinition = "boolean default false")
    private boolean isHidden = false;

    @Column(name = "is_top_product", columnDefinition = "boolean default false")
    private boolean isTopProduct = false;

    @OneToMany(mappedBy = "productId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<Link> links = new ArrayList<>();      // includes Dealer

    public Product(ProductCreateDTO productCreateDTO) {
        this.name = productCreateDTO.getName();
        this.producer = productCreateDTO.getProducer();
        this.form = productCreateDTO.getForm();
        this.metal = productCreateDTO.getMetal();
        this.grams = productCreateDTO.getGrams();
        this.year = productCreateDTO.getYear();
        this.isHidden = productCreateDTO.isHidden();
        this.isTopProduct = productCreateDTO.isTopProduct();
    }

    public boolean getIsTopProduct() {
        return isTopProduct;
    }
}
