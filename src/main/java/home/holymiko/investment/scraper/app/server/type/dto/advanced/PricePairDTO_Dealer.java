package home.holymiko.investment.scraper.app.server.type.dto.advanced;

import home.holymiko.investment.scraper.app.server.type.dto.simple.PricePairDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
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
