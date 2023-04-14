package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.GrahamStockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGraham;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGrahamHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class StockGrahamMapper {

    public abstract GrahamStockDTO toStockDTO(StockGraham entity);

    @Mappings({
            @Mapping(target = "ticker", source = "entity.ticker.ticker")
    })
    public abstract StockGrahamHistory toGrahamStockHistory(StockGraham entity);

}
