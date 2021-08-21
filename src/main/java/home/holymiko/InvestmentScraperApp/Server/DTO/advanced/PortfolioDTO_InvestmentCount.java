package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import lombok.Getter;

@Getter
public class PortfolioDTO_InvestmentCount extends PortfolioDTO {

    private final double investmentCount;

    public PortfolioDTO_InvestmentCount(long id, String owner, double beginPrice, double value, double yield, double investmentCount) {
        super(id, owner, beginPrice, value, yield);
        this.investmentCount = investmentCount;
    }
}
