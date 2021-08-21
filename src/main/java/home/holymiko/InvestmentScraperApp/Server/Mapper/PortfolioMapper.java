package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.InvestmentMetalDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.PortfolioDTO_InvestmentCount;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.Portfolio;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(componentModel = "spring")
public abstract class PortfolioMapper {

    private static final String VALUE = "java( portfolio.getPortfolioValue() )";
    private static final String INVESTMENT_METAL_DTO_ALL_PRICES = "java( investmentMetalMapper.toDTO_AllPrices(portfolio.getInvestmentMetals()) )";
    private static final String INVESTMENT_METAL_DTO_LATEST_PRICES = "java( investmentMetalMapper.toDTO_LatestPrices(portfolio.getInvestmentMetals()) )";
    private static final String INVESTMENT_METAL_DTO_ONE_LATEST_PRICE = "java( investmentMetalMapper.toDTO_OneLatestPrice(portfolio.getInvestmentMetals()) )";
    private static final String INVESTMENT_METAL_DTO_LATEST_PRICES_ONE_LATEST_PRICE = "java( investmentMetalMapper.toDTO_LatestPrices_OneLatestPrice(portfolio.getInvestmentMetals()) )";


    @Autowired
    protected InvestmentMetalMapper investmentMetalMapper;

    @Mapping(target = "value", expression = VALUE)
    public abstract PortfolioDTO toPortfolioDTO(Portfolio portfolio);

    @Mappings({
            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investmentCount", expression = "java( portfolio.getInvestmentMetals().size() )")
    })
    public abstract PortfolioDTO_InvestmentCount toPortfolioDTO_InvestmentCount(Portfolio portfolio);

    @Mappings({
            @Mapping(target = "value", expression = VALUE)
    })
    public abstract PortfolioDTO_ProductDTO toPortfolioDTO_ProductDTO(Portfolio portfolio, List<InvestmentMetalDTO_ProductDTO> investments);

    @Mappings({
            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investments", expression = INVESTMENT_METAL_DTO_ONE_LATEST_PRICE)
    })
    public abstract PortfolioDTO_ProductDTO toDTO_OneLatestPrice(Portfolio portfolio);

    @Mappings({
            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investments", expression = INVESTMENT_METAL_DTO_LATEST_PRICES_ONE_LATEST_PRICE)
    })
    public abstract PortfolioDTO_ProductDTO toDTO_LatestPrices_OneLatestPrice(Portfolio portfolio);

    @Mappings({
            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investments", expression = INVESTMENT_METAL_DTO_LATEST_PRICES)
    })
    public abstract PortfolioDTO_ProductDTO toDTO_LatestPrices(Portfolio portfolio);

    @Mappings({
            @Mapping(target = "value", expression = VALUE),
            @Mapping(target = "investments", expression = INVESTMENT_METAL_DTO_ALL_PRICES)
    })
    public abstract PortfolioDTO_ProductDTO toDTO_AllPrices(Portfolio portfolio);

}
