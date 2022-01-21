package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

@Entity
@Getter
public class ExchangeRate {
    @Id
    @GeneratedValue
    private long id;
    private Date date;
    private String country;
    private String currency;
    private Integer amount;
    private String code;
    private double exchangeRate;


    public ExchangeRate(Date date, String country, String currency, Integer amount, String code, double exchangeRate) {
        this.date = date;
        this.country = country;
        this.currency = currency;
        this.amount = amount;
        this.code = code;
        this.exchangeRate = exchangeRate;
    }

    public ExchangeRate() {

    }
}

