package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.dto.advanced.InvestmentStockDTO_StockDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.InvestmentStockDTO;
import home.holymiko.investment.scraper.app.server.type.entity.InvestmentStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class InvestmentStockMapper {

    private static final String PRODUCT_DTO_ALL_PRICES = "java( stockGrahamMapper.toStockDTO(entity.getStockGraham()) )";

    @Autowired
    protected StockGrahamMapper stockGrahamMapper;

    public abstract InvestmentStockDTO toInvestmentStockDTO(InvestmentStock entity);

    @Mapping(target = "grahamStockDTO", expression = PRODUCT_DTO_ALL_PRICES)
    public abstract InvestmentStockDTO_StockDTO toDTO_StockDTO(InvestmentStock entity);

    public abstract List<InvestmentStockDTO_StockDTO> toDTO_StockDTO(List<InvestmentStock> entities);
}
