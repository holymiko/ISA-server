package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.GrahamStockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStock;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStockHistory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public abstract class GrahamStockMapper {

    public abstract GrahamStockDTO toStockDTO(GrahamStock entity);

    @Mappings({
            @Mapping(target = "ticker", source = "entity.ticker.ticker")
    })
    public abstract GrahamStockHistory toGrahamStockHistory(GrahamStock entity);

}
