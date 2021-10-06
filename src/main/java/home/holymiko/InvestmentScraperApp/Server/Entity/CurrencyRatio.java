package home.holymiko.InvestmentScraperApp.Server.Entity;

import home.holymiko.InvestmentScraperApp.Server.Enum.Currency;
import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import lombok.Getter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
public class CurrencyRatio {
    @Id
    @GeneratedValue
    private long id;
    private final Currency source;
    private final Currency target;
    private final double ratio;


    public CurrencyRatio(Currency source, Currency target, double ratio) {
        this.source = source;
        this.target = target;
        this.ratio = ratio;
    }
}

