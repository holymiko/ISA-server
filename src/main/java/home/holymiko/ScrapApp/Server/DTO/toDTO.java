package home.holymiko.ScrapApp.Server.DTO;

import home.holymiko.ScrapApp.Server.DTO.advanced.*;
import home.holymiko.ScrapApp.Server.DTO.advanced.PortfolioDTO_InvestmentCount;
import home.holymiko.ScrapApp.Server.DTO.simple.InvestmentMetalDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.Entity.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class toDTO {


    //////// PRICE DTO ////////

    private static PriceDTO toSimpleDTO(Price price, double grams) {
        return new PriceDTO(
                price.getDateTime(),
                price.getPrice(),
                price.getRedemption(),
                price.getDealer(),
                price.getRedemption() / price.getPrice(),
                price.getPrice() / grams
        );
    }

    private static List<PriceDTO> toSimpleDTOs(List<Price> prices, double grams) {
        return prices.stream().map(
                price -> toSimpleDTO(price, grams)
        ).collect(Collectors.toList());
    }


    //////// PRODUCT DTO ////////

    public static ProductDTO_OneLatestPrice toDTO_OneLatestPrice(Product product) {
        return new ProductDTO_OneLatestPrice(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                toSimpleDTO(
                        product.getPriceByBestRedemption(),
                        product.getGrams()
                )
        );
    }

    public static ProductDTO_LatestPrices_OneLatestPrice toDTO_LatestPrices_OneLatestPrice(Product product) {
        return new ProductDTO_LatestPrices_OneLatestPrice(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                toSimpleDTOs(
                        product.getLatestPrices(),
                        product.getGrams()
                ),
                toSimpleDTO(
                        product.getPriceByBestRedemption(),
                        product.getGrams()
                )
        );
    }

    public static ProductDTO_LatestPrices toDTO_LatestPrices(Product product) {
        return new ProductDTO_LatestPrices(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                product.getLinks()
                        .stream()
                        .map(Link::getLink)
                        .collect(Collectors.toList()),
                toSimpleDTOs(
                        product.getLatestPrices(),
                        product.getGrams()
                )
        );
    }

    public static ProductDTO_AllPrices toDTO_AllPrices(Product product) {
        return new ProductDTO_AllPrices(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                product.getLinks()
                        .stream()
                        .map(Link::getLink)
                        .collect(Collectors.toList()),
                toSimpleDTOs(
                        product.getLatestPrices(),
                        product.getGrams()
                ),
                toSimpleDTOs(
                        product.getPrices(),
                        product.getGrams()
                )
        );
    }

    public static Optional<ProductDTO_AllPrices> toDTO_AllPrices(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                toDTO_AllPrices(optionalProduct.get())
        );
    }

//    public static Optional<ProductDTO_LatestPrices> toDTO_LatestPrices(Optional<Product> optionalProduct) {
//        if (optionalProduct.isEmpty()) {
//            return Optional.empty();
//        }
//        return Optional.of(
//                toDTO_LatestPrices(optionalProduct.get())
//        );
//    }


    //////// INVESTMENT METAL DTO ////////

    public static InvestmentMetalDTO toSimpleDTO(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO(
                investmentMetal.getId(),
                investmentMetal.getDealer(),
                investmentMetal.getYield(),
                investmentMetal.getBeginPrice(),
                investmentMetal.getEndPrice(),
                investmentMetal.getBeginDate(),
                investmentMetal.getEndDate()
        );
    }

    public static InvestmentMetalDTO_ProductDTO toDTO_OneLatestPrice(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                toDTO_OneLatestPrice(
                        investmentMetal.getProduct()
                )
        );
    }

    public static InvestmentMetalDTO_ProductDTO toDTO_LatestPrices(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                toDTO_LatestPrices(
                        investmentMetal.getProduct()
                )
        );
    }

    public static InvestmentMetalDTO_ProductDTO toDTO_LatestPrices_OneLatestPrice(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                toDTO_LatestPrices_OneLatestPrice(
                        investmentMetal.getProduct()
                )
        );
    }

    public static InvestmentMetalDTO_ProductDTO toDTO_AllPrices(InvestmentMetal investmentMetal) {
        return new InvestmentMetalDTO_ProductDTO(
                investmentMetal,
                toDTO_AllPrices(
                        investmentMetal.getProduct()
                )
        );
    }

    //////// PORTFOLIO DTO ////////

    public static PortfolioDTO toSimpleDTO(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice
        );
    }

    public static Optional<PortfolioDTO> toSimpleDTO(Optional<Portfolio> optionalPortfolio) {
        if (optionalPortfolio.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toSimpleDTO(optionalPortfolio.get()));
    }

    /**
     * Converts Portfolio's Investments to IDs
     *
     * @param portfolio Portfolio to be converted
     * @return Portfolio with collection of IDs
     */
    public static PortfolioDTO toPortfolioDTO_InvestmentCount(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_InvestmentCount(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals().size()
        );
    }

    /**
     * Converts Portfolio's Investments to InvestmentDTOs
     * @param portfolio Portfolio to be converted
     * @return Portfolio with collection of InvestmentDTOs
     */
    public static PortfolioDTO toDTO_OneLatestPrice(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                toDTO::toDTO_OneLatestPrice
                        )
                        .collect(Collectors.toList())
        );
    }

    public static PortfolioDTO toDTO_LatestPrices_OneLatestPrice(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                toDTO::toDTO_LatestPrices_OneLatestPrice
                        )
                        .collect(Collectors.toList())
        );
    }

    public static PortfolioDTO toDTO_LatestPrices(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                toDTO::toDTO_LatestPrices
                        )
                        .collect(Collectors.toList())
        );
    }

    public static PortfolioDTO toDTO_AllPrices(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_ProductDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                toDTO::toDTO_AllPrices
                        )
                        .collect(Collectors.toList())
        );
    }
}

