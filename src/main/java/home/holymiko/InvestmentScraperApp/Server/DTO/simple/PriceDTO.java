package home.holymiko.InvestmentScraperApp.Server.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PriceDTO {

    private final LocalDateTime dateTime;
    private final double price;
    private final double redemption;
    private final Dealer dealer;
    private final double spread;
    private final double pricePerGram;

    public PriceDTO(LocalDateTime dateTime, double price, double redemption, Dealer dealer, double spread, double pricePerGram) {
        this.dateTime = dateTime;
        this.price = price;
        this.redemption = redemption;
        this.dealer = dealer;
        this.spread = spread;
        this.pricePerGram = pricePerGram;
    }

}
