package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Date;


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

