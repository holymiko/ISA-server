package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.GrahamStockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.InvestmentStockDTO;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentStockDTO_StockDTO extends InvestmentStockDTO {

    private final GrahamStockDTO grahamStockDTO;

    public InvestmentStockDTO_StockDTO(long id, Integer amount, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate, GrahamStockDTO grahamStockDTO) {
        super(id, amount, beginPrice, endPrice, beginDate, endDate);
        this.grahamStockDTO = grahamStockDTO;
    }
}

