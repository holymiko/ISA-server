package home.holymiko.ScrapApp.Server.Entity;

import com.sun.istack.NotNull;
import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class Link {
    @Id
    @GeneratedValue
    private Long Id;
    private Dealer dealer;
    @NotNull
    private String link;

    public Link() {
    }

    public Link(Dealer dealer, String link) {
        this.dealer = dealer;
        this.link = link;
    }

    public Dealer getDealer() {
        return dealer;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
