package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class InvestmentStockDTO {

    private final long id;
    private final Integer amount;
    private final double beginPrice;
    private final double endPrice;
    private final LocalDate beginDate;
    private final LocalDate endDate;

}
