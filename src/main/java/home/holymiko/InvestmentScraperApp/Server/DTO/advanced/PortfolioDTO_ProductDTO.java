package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioDTO_ProductDTO extends PortfolioDTO {

    private final List<InvestmentMetalDTO_ProductDTO> investments;

    public PortfolioDTO_ProductDTO(long id, String owner, double beginPrice, double value, List<InvestmentMetalDTO_ProductDTO> investments) {
        super(id, owner, beginPrice, value);
        this.investments = investments;
    }
}

