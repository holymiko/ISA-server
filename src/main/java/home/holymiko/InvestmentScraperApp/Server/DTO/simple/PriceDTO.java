package home.holymiko.InvestmentScraperApp.Server.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PriceDTO {

    private final LocalDateTime dateTime;
    private final double price;
    private final double redemption;
    private final Dealer dealer;
    private double spread;
    private double pricePerGram;

    public PriceDTO(LocalDateTime dateTime, double price, double redemption, Dealer dealer, double spread, double pricePerGram) {
        this.dateTime = dateTime;
        this.price = price;
        this.redemption = redemption;
        this.dealer = dealer;
        this.spread = spread;
        this.pricePerGram = pricePerGram;
    }

}
