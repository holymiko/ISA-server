package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Entity
@Getter
public class Price {

    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime dateTime;
    private Double amount;
    private boolean isRedemption;

    public Price() {
    }

    public Price(LocalDateTime dateTime, Double amount, boolean isRedemption) {
        this.dateTime = dateTime;
        this.amount = amount;
        this.isRedemption = isRedemption;
    }

    @Override
    public String toString() {
        return Double.toString(amount);
    }
}
