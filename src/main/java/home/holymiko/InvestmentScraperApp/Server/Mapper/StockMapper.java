package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.StockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Stock;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class StockMapper {

    public abstract StockDTO toStockDTO(Stock entity);

}
