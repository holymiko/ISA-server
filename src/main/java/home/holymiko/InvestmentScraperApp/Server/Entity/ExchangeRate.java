package home.holymiko.InvestmentScraperApp.Server.Entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
public class ExchangeRate {
    @Id
    @GeneratedValue
    private long id;
    private final String country;
    private final String currency;
    private final Integer amount;
    private final String code;
    private final double exchangeRate;


    public ExchangeRate(String country, String currency, Integer amount, String code, double exchangeRate) {
        this.country = country;
        this.currency = currency;
        this.amount = amount;
        this.code = code;
        this.exchangeRate = exchangeRate;
    }
}

