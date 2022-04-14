package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PortfolioDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioDTO_ProductDTO extends PortfolioDTO {

    private final List<InvestmentMetalDTO_ProductDTO> investmentsMetal;
    private final List<InvestmentStockDTO_StockDTO> investmentsStock;


    public PortfolioDTO_ProductDTO(long id, String owner, double beginPrice, double value, List<InvestmentMetalDTO_ProductDTO> investmentsMetal, List<InvestmentStockDTO_StockDTO> investmentsStock) {
        super(id, owner, beginPrice, value);
        this.investmentsMetal = investmentsMetal;
        this.investmentsStock = investmentsStock;
    }
}

