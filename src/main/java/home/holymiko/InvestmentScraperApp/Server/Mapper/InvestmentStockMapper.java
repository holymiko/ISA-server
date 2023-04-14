package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.InvestmentStockDTO_StockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.InvestmentStockDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.InvestmentStock;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class InvestmentStockMapper {

    private static final String PRODUCT_DTO_ALL_PRICES = "java( grahamStockMapper.toStockDTO(entity.getGrahamStock()) )";

    @Autowired
    protected GrahamStockMapper grahamStockMapper;

    public abstract InvestmentStockDTO toInvestmentStockDTO(InvestmentStock entity);

    @Mapping(target = "grahamStockDTO", expression = PRODUCT_DTO_ALL_PRICES)
    public abstract InvestmentStockDTO_StockDTO toDTO_StockDTO(InvestmentStock entity);

    public abstract List<InvestmentStockDTO_StockDTO> toDTO_StockDTO(List<InvestmentStock> entities);
}
