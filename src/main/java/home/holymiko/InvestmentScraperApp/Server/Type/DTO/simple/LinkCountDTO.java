package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
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
