package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@XmlRootElement(name = "price")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
@NoArgsConstructor
public class Price {

    @Id
    @GeneratedValue
    private long id;
    private LocalDateTime dateTime;
    private Double amount;
    private boolean isRedemption;

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
