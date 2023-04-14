package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "exchangeRate")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@NoArgsConstructor
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

}

