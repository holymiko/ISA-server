package home.holymiko.ScrapApp.Server.Entity;

import lombok.Getter;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
public class Portfolio {
    @Id
    @GeneratedValue
    private long id;
    private String owner;
    @OneToMany(fetch = FetchType.EAGER)
    private List<InvestmentMetal> investmentMetals;

    public Portfolio(List<InvestmentMetal> investmentMetals, String owner) {
        this.investmentMetals = investmentMetals;
        this.owner = owner;
    }

    public Portfolio() {
    }

}
