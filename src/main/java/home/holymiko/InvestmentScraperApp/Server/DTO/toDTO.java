package home.holymiko.InvestmentScraperApp.Server.DTO;

import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.*;
import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.PortfolioDTO_InvestmentCount;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.*;
import home.holymiko.InvestmentScraperApp.Server.Mapper.InvestmentMetalMapper;
import home.holymiko.InvestmentScraperApp.Server.Mapper.PortfolioMapper;
import home.holymiko.InvestmentScraperApp.Server.Mapper.PriceMapper;
import home.holymiko.InvestmentScraperApp.Server.Mapper.ProductMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class toDTO {

    private final ProductMapper productMapper;
    private final PriceMapper priceMapper;
    private final InvestmentMetalMapper investmentMetalMapper;
    private final PortfolioMapper portfolioMapper;

    @Autowired
    public toDTO(ProductMapper productMapper, PriceMapper priceMapper, InvestmentMetalMapper investmentMetalMapper, PortfolioMapper portfolioMapper) {
        this.productMapper = productMapper;
        this.priceMapper = priceMapper;
        this.investmentMetalMapper = investmentMetalMapper;
        this.portfolioMapper = portfolioMapper;
    }

    //////// PRICE DTO ////////


    //////// PRODUCT DTO ////////


    //////// INVESTMENT METAL DTO ////////


    public InvestmentMetalDTO_ProductDTO toDTO_OneLatestPrice(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                productMapper.toProductDTO_OneLatestPrice(
                        investmentMetal.getProduct(),
                        priceMapper
                )
        );
    }

    public InvestmentMetalDTO_ProductDTO toDTO_LatestPrices(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                productMapper.toProductDTO_LatestPrices(
                        investmentMetal.getProduct()
                )
        );
    }

    public InvestmentMetalDTO_ProductDTO toDTO_LatestPrices_OneLatestPrice(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                productMapper.toProductDTO_LatestPrices_OneLatestPrice(
                        investmentMetal.getProduct()
                )
        );
    }

    public InvestmentMetalDTO_ProductDTO toDTO_AllPrices(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                productMapper.toProductDTO_AllPrices(
                        investmentMetal.getProduct()
                )
        );
    }

    //////// PORTFOLIO DTO ////////

    public PortfolioDTO toSimpleDTO(Portfolio portfolio) {
        return portfolioMapper.toPortfolioDTO(portfolio);
    }

    /**
     * Converts Portfolio's Investments to IDs
     *
     * @param portfolio Portfolio to be converted
     * @return Portfolio with collection of IDs
     */
    public PortfolioDTO toPortfolioDTO_InvestmentCount(Portfolio portfolio) {
        return portfolioMapper.toPortfolioDTO_InvestmentCount(portfolio);
    }

    /**
     * Converts Portfolio's Investments to InvestmentDTOs
     * @param portfolio Portfolio to be converted
     * @return Portfolio with collection of InvestmentDTOs
     */
    public PortfolioDTO toDTO_OneLatestPrice(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                this::toDTO_OneLatestPrice
                        )
                        .collect(Collectors.toList())
        );
    }

    public PortfolioDTO toDTO_LatestPrices_OneLatestPrice(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                this::toDTO_LatestPrices_OneLatestPrice
                        )
                        .collect(Collectors.toList())
        );
    }

    public PortfolioDTO toDTO_LatestPrices(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                this::toDTO_LatestPrices
                        )
                        .collect(Collectors.toList())
        );
    }

    public PortfolioDTO_ProductDTO toDTO_AllPrices(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                this::toDTO_AllPrices
                        )
                        .collect(Collectors.toList())
        );
    }
}

