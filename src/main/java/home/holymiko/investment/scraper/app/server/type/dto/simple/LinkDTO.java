package home.holymiko.investment.scraper.app.server.type.dto.simple;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class LinkDTO {

    private final Long id;
    private final Dealer dealer;
    private final String name;
    private final String uri;
    private final Long productId;

}
