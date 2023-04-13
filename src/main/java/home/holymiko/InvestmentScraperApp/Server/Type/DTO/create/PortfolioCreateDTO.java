package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class PortfolioCreateDTO {

    private final String owner;
    private final List<Long> investmentIds;

}
