package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.InvestmentStockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.StockDTO;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentStockDTO_StockDTO extends InvestmentStockDTO {

    private final StockDTO stockDTO;

    public InvestmentStockDTO_StockDTO(long id, Integer amount, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate, StockDTO stockDTO) {
        super(id, amount, beginPrice, endPrice, beginDate, endDate);
        this.stockDTO = stockDTO;
    }
}

