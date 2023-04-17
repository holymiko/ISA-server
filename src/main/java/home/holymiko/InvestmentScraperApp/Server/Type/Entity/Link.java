package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import com.sun.istack.NotNull;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "linkId", fetch = FetchType.EAGER)
    @Fetch(FetchMode.SELECT)
    private List<PricePair> pricePairs = new ArrayList<>();

    private Long productId;

    public Link(Dealer dealer, String uri, String name) {
        this.uri = uri;
        this.name = name;
        this.dealer = dealer;
    }
}
