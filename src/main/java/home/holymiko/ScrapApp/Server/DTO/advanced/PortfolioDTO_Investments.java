package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.PortfolioDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioDTO_Investments extends PortfolioDTO {

    private final List<InvestmentMetalDTO_OneLatestPrice> investments;

    public PortfolioDTO_Investments(long id, String owner, double beginPrice, double value, double yield, List<InvestmentMetalDTO_OneLatestPrice> investments) {
        super(id, owner, beginPrice, value, yield);
        this.investments = investments;
    }
}

