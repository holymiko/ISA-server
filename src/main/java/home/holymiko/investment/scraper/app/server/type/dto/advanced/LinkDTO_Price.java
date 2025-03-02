package home.holymiko.investment.scraper.app.server.type.dto.advanced;

import home.holymiko.investment.scraper.app.server.type.dto.simple.PricePairDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LinkDTO_Price {

    private Long id;
    private Dealer dealer;
    private String uri;
    private PricePairDTO price;

}
