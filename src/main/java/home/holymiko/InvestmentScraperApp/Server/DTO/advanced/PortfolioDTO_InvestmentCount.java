package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import lombok.Getter;

@Getter
public class PortfolioDTO_InvestmentCount extends PortfolioDTO {
    // TODO Adapt for InvestmentStock. Modify client side as well
    private final double investmentCount;

    public PortfolioDTO_InvestmentCount(long id, String owner, double beginPrice, double value, double investmentCount) {
        super(id, owner, beginPrice, value);
        this.investmentCount = investmentCount;
    }
}
