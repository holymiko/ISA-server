package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_InvestmentCount;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Portfolio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Class broken to pieces. Needs refactor and repair. Unreliable
 */
@Deprecated
@Mapper(componentModel = "spring")
public abstract class PortfolioMapper {

    private static final String VALUE = "java( portfolio.getPortfolioValue() )";
    private static final String INVESTMENT_COUNT = "java( portfolio.getInvestmentMetals().size() + portfolio.getInvestmentStocks().size() )";
    private static final String INVESTMENT_STOCK_DTO = "java( investmentStockMapper.toDTO_StockDTO(portfolio.getInvestmentStocks()) )";
    private static final String INVESTMENT_METAL_DTO_ALL_PRICES = "java( investmentMetalMapper.toDTO_AllPrices(portfolio.getInvestmentMetals()) )";
    private static final String INVESTMENT_METAL_DTO_LATEST_PRICES = "java( investmentMetalMapper.toDTO_LatestPrices(portfolio.getInvestmentMetals()) )";


    @Autowired
    protected InvestmentMetalMapper investmentMetalMapper;

    @Autowired
    protected InvestmentStockMapper investmentStockMapper;


//    @Mapping(target = "value", expression = VALUE)
    public abstract PortfolioDTO toPortfolioDTO(Portfolio portfolio);

    @Mappings({
//            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investmentCount", expression = INVESTMENT_COUNT)
    })
    public abstract PortfolioDTO_InvestmentCount toPortfolioDTO_InvestmentCount(Portfolio portfolio);

    @Mappings({
//            @Mapping(target = "value", expression = VALUE),
//            @Mapping(target = "investmentsMetal", expression = INVESTMENT_METAL_DTO_LATEST_PRICES),
            @Mapping(target = "investmentsStock", expression = INVESTMENT_STOCK_DTO)
    })
    public abstract PortfolioDTO_ProductDTO toDTO_LatestPrices(Portfolio portfolio);

    @Mappings({
//            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investmentsMetal", expression = INVESTMENT_METAL_DTO_ALL_PRICES),
            @Mapping(target = "investmentsStock", expression = INVESTMENT_STOCK_DTO)
    })
    public abstract PortfolioDTO_ProductDTO toDTO_AllPrices(Portfolio portfolio);

}
