package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.*;

import javax.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "link")
@XmlAccessorType(XmlAccessType.FIELD)
@ToString
@Getter
@Setter
@Entity
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue
    @Setter(AccessLevel.NONE)
    private Long id;
    @NotNull
    private String uri;
    private String name;
    private Dealer dealer;
    private Long productId;

    public Link(Dealer dealer, String uri) {
        this.dealer = dealer;
        this.uri = uri;
    }

    public Link(String uri, String name, Dealer dealer) {
        this.uri = uri;
        this.name = name;
        this.dealer = dealer;
    }
}
