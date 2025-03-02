package home.holymiko.investment.scraper.app.server.type.entity;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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

    @OneToOne
    private PricePair pricePair;

    @OneToMany(mappedBy = "linkId", fetch = FetchType.LAZY)
    @Fetch(FetchMode.SELECT)
    private List<PricePairHistory> pricePairsHistory = new ArrayList<>();

    private Long productId;

    public Link(Dealer dealer, String uri, String name) {
        this.uri = uri;
        this.name = name;
        this.dealer = dealer;
    }
}
