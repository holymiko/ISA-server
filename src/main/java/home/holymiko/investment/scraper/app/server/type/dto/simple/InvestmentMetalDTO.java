package home.holymiko.investment.scraper.app.server.type.dto.simple;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InvestmentMetalDTO {

    private final long id;
    private final Dealer dealer;
    private final double yield;
    private final double beginPrice;
    private final double endPrice;
    private final LocalDate beginDate;
    private final LocalDate endDate;

}
