package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.InvestmentMetalDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioDTO_Investments {

    private final long id;
    private final String owner;
    private final double beginPrice;
    private final double value;
    private final double yield;
    private final List<InvestmentMetalDTO> investments;

    public PortfolioDTO_Investments(long id, String owner, double beginPrice, double value, double yield, List<InvestmentMetalDTO> investmentMetalDTOS) {
        this.id = id;
        this.owner = owner;
        this.beginPrice = beginPrice;
        this.value = value;
        this.yield = yield;
        this.investments = investmentMetalDTOS;
    }
}

