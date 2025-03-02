package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.dto.simple.GrahamStockDTO;
import home.holymiko.investment.scraper.app.server.type.entity.StockGraham;
import home.holymiko.investment.scraper.app.server.type.entity.StockGrahamHistory;
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
