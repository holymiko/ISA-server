package home.holymiko.ScrapApp.Server.DTO;

import home.holymiko.ScrapApp.Server.DTO.advanced.*;
import home.holymiko.ScrapApp.Server.DTO.simple.PortfolioDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.PriceDTO;
import home.holymiko.ScrapApp.Server.Entity.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class toDTO {

    private static List<PriceDTO> toPriceDTOs(List<Price> prices, double grams){
        return prices.stream().map(
                price -> toPriceDTO(price, grams)
        ).collect(Collectors.toList());
    }

    private static PriceDTO toPriceDTO(Price price, double grams){
        return new PriceDTO(
                price.getDateTime(),
                price.getPrice(),
                price.getRedemption(),
                price.getDealer(),
                price.getRedemption() / price.getPrice(),
                price.getPrice() / grams
        );
    }

    public static ProductDTO_OneLatestPrice toDTOOneLatestPrice(Product product) {
        return new ProductDTO_OneLatestPrice(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                toPriceDTO(
                        product.getPriceByBestRedemption(),
                        product.getGrams()
                )
        );
    }

    public static ProductDTO_LatestPrices toDTOLatestPrices(Product product) {
        return new ProductDTO_LatestPrices(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                product.getLinks()
                        .stream()
                        .map(Link::getLink)
                        .collect(Collectors.toList()),
                toPriceDTOs(
                        product.getLatestPrices(),
                        product.getGrams()
                )
        );
    }

    public static ProductDTO_AllPrices toDTOAllPrices(Product product) {
        return new ProductDTO_AllPrices(
                product.getId(),
                product.getMetal().name(),
                product.getName(),
                product.getGrams(),
                product.getLinks()
                        .stream()
                        .map(Link::getLink)
                        .collect(Collectors.toList()),
                toPriceDTOs(
                        product.getLatestPrices(),
                        product.getGrams()
                ),
                toPriceDTOs(
                        product.getPrices(),
                        product.getGrams()
                )
        );
    }

    public static Optional<ProductDTO_AllPrices> toDTOAllPrices(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                toDTOAllPrices(optionalProduct.get())
        );
    }

    public static Optional<ProductDTO_LatestPrices> toDTOLatestPrices(Optional<Product> optionalProduct) {
        if (optionalProduct.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(
                toDTOLatestPrices(optionalProduct.get())
        );
    }

    public static InvestmentMetalDTO_OneLatestPrice metalToDTO(InvestmentMetal investmentMetal){
        return new InvestmentMetalDTO_OneLatestPrice(
                investmentMetal.getId(),
                toDTOOneLatestPrice(investmentMetal.getProduct()),
                investmentMetal.getDealer(),
                investmentMetal.getYield(),
                investmentMetal.getBeginPrice(),
                investmentMetal.getEndPrice(),
                investmentMetal.getBeginDate(),
                investmentMetal.getEndDate()
        );
    }

    /**
     * Converts Portfolio's Investments to IDs
     * @param portfolio Portfolio to be converted
     * @return Portfolio with collection of IDs
     */
    public static PortfolioDTO toPortfolioDTO(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals().size()
        );
    }

    public static Optional<PortfolioDTO> toPortfolioDTO(Optional<Portfolio> optionalPortfolio) {
        if (optionalPortfolio.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toPortfolioDTO(optionalPortfolio.get()));
    }

    /**
     * Converts Portfolio's Investments to InvestmentDTOs
     * @param portfolio Portfolio to be converted
     * @return Portfolio with collection of InvestmentDTOs
     */
    public static PortfolioDTO_Investments toPortfolioInvestmentDTO(Portfolio portfolio) {
        double beginPrice = portfolio.getBeginPrice();
        double value = portfolio.getPortfolioValue();

        return new PortfolioDTO_Investments(
                portfolio.getId(),
                portfolio.getOwner(),
                beginPrice,
                value,
                value / beginPrice,
                portfolio.getInvestmentMetals()
                        .stream()
                        .map(
                                toDTO::metalToDTO
                        )
                        .collect(Collectors.toList())
        );
    }
}
