package home.holymiko.investment.scraper.app.server.type.dto.advanced;

import home.holymiko.investment.scraper.app.server.type.dto.simple.PortfolioDTO;
import lombok.Getter;

@Getter
public class PortfolioDTO_InvestmentCount extends PortfolioDTO {

    private final double investmentCount;

    public PortfolioDTO_InvestmentCount(long id, String owner, double beginPrice, double value, double investmentCount) {
        super(id, owner, beginPrice, value);
        this.investmentCount = investmentCount;
    }
}
