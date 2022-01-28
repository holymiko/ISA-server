package home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.create;

import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioCreateDTO {

    private final String owner;
    private final List<Long> investmentIds;

    public PortfolioCreateDTO(String owner, List<Long> investments) {
        this.owner = owner;
        this.investmentIds = investments;
    }

}
