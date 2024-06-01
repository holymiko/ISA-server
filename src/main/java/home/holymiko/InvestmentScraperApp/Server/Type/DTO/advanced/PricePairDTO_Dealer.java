package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PricePairDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PricePairDTO_Dealer extends PricePairDTO {

    private final Dealer dealer;

    public PricePairDTO_Dealer(long id, LocalDateTime priceDateTime, LocalDateTime redemptionDateTime, double price, double redemption, Dealer dealer, double grams, String availability, String availabilityMessage) {
        super(id, priceDateTime, redemptionDateTime, price, redemption, grams, availability, availabilityMessage);
        this.dealer = dealer;
    }


}
