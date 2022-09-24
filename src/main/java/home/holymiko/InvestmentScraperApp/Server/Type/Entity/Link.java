package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.persistence.*;

@Entity
@Getter
public class Link {
    @Id
    @GeneratedValue
    private Long Id;
    private Dealer dealer;
    @NotNull
    private String url;
    private Long productId;

    public Link() {
    }

    public Link(Dealer dealer, String url) {
        this.dealer = dealer;
        this.url = url;
    }

    public void setDealer(Dealer dealer) {
        this.dealer = dealer;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Link{" +
                "Id=" + Id +
                ", dealer=" + dealer +
                ", url='" + url + '\'' +
//                ", product=" + product +
                '}';
    }
}
