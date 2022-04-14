package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
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

    public PriceDTO(LocalDateTime dateTime, double price, double redemption, Dealer dealer, double grams) {
        this.dateTime = dateTime;
        this.price = price;
        this.redemption = redemption;
        this.dealer = dealer;

        if(price > 0) {
            this.spread = redemption / price;
        } else {
            this.spread = 0;
        }

        if (grams > 0) {
            this.pricePerGram = price / grams;
        } else {
            this.pricePerGram = 0;
        }
    }


}
