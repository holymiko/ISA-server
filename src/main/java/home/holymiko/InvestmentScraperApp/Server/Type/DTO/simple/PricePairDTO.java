package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Availability;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PricePairDTO {

    private final long id;
    private final LocalDateTime priceDateTime;
    private final LocalDateTime redemptionDateTime;
    private final double price;
    private final double redemption;
    private final double spread;
    private final double pricePerGram;
    private final String availability;
    private final String availabilityMessage;

    public PricePairDTO(long id, LocalDateTime priceDateTime, LocalDateTime redemptionDateTime, double price, double redemption, double grams, String availability, String availabilityMessage) {
        this.id = id;
        this.priceDateTime = priceDateTime;
        this.redemptionDateTime = redemptionDateTime;
        this.price = price;
        this.redemption = redemption;
        this.availability = availability;
        this.availabilityMessage = availabilityMessage;

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
