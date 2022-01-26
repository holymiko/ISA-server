package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
public class Link {
    @Id
    @GeneratedValue
    private Long Id;
    private Dealer dealer;
    @NotNull
    private String link;
    // TODO Rename to URL
    @ManyToOne
    private Product product;

    public Link() {
    }

    public Link(Dealer dealer, String link) {
        this.dealer = dealer;
        this.link = link;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
