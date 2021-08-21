package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.PortfolioDTO_InvestmentCount;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.Portfolio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring")
public interface PortfolioMapper {

    static final String VALUE = "java( entity.getPortfolioValue() )";

    @Mapping(target = "value", expression = VALUE)
    PortfolioDTO toPortfolioDTO(Portfolio entity);

    @Mappings({
            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investmentCount", expression = "java( entity.getInvestmentMetals().size() )")
    })
    PortfolioDTO_InvestmentCount toPortfolioDTO_InvestmentCount(Portfolio entity);

}
