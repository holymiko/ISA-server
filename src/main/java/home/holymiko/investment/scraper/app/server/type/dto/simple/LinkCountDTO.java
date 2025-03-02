package home.holymiko.investment.scraper.app.server.type.dto.simple;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LinkCountDTO {

    private final Dealer dealer;
    private final Long productCount;
    private final Long linkWithoutProductCount;
    private final Long hiddenProductCount;

}
