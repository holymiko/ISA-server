package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.InvestmentMetalDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.InvestmentMetalDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.InvestmentMetal;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class InvestmentMetalMapper {

    private static final String PRODUCT_DTO_ALL_PRICES = "java( productMapper.toProductDTO_AllPrices(entity.getProduct()) )";
    private static final String PRODUCT_DTO_LATEST_PRICES = "java( productMapper.toProductDTO_LatestPrices(entity.getProduct()) )";
    private static final String PRODUCT_DTO_ONE_LATEST_PRICE = "java( productMapper.toProductDTO_OneLatestPrice(entity.getProduct()) )";
    private static final String PRODUCT_DTO_LATEST_PRICES_ONE_LATEST_PRICE = "java( productMapper.toProductDTO_LatestPrices_OneLatestPrice(entity.getProduct()) )";

    @Autowired
    protected ProductMapper productMapper;

    public abstract InvestmentMetalDTO toInvestmentMetalDTO(InvestmentMetal entity);

//    public abstract InvestmentMetalDTO_ProductDTO toInvestmentMetalDTO_ProductDTO(InvestmentMetal entity, ProductDTO productDTO);

    @Mapping(target = "productDTO", expression = PRODUCT_DTO_ALL_PRICES)
    public abstract InvestmentMetalDTO_ProductDTO toDTO_AllPrices(InvestmentMetal entity);

    @Mapping(target = "productDTO", expression = PRODUCT_DTO_LATEST_PRICES)
    public abstract InvestmentMetalDTO_ProductDTO toDTO_LatestPrices(InvestmentMetal entity);

    @Mapping(target = "productDTO", expression = PRODUCT_DTO_ONE_LATEST_PRICE)
    public abstract InvestmentMetalDTO_ProductDTO toDTO_OneLatestPrice(InvestmentMetal entity);

    @Mapping(target = "productDTO", expression = PRODUCT_DTO_LATEST_PRICES_ONE_LATEST_PRICE)
    public abstract InvestmentMetalDTO_ProductDTO toDTO_LatestPrices_OneLatestPrice(InvestmentMetal entity);

    public List<InvestmentMetalDTO_ProductDTO> toDTO_LatestPrices_OneLatestPrice(List<InvestmentMetal> entities) {
        return entities
                .stream()
                .map(
                        this::toDTO_LatestPrices_OneLatestPrice
                )
                .collect(Collectors.toList());
    }

    public List<InvestmentMetalDTO_ProductDTO> toDTO_AllPrices(List<InvestmentMetal> entities) {
        return entities.stream()
                .map(
                        this::toDTO_AllPrices
                )
                .collect(Collectors.toList());
    }

    public List<InvestmentMetalDTO_ProductDTO> toDTO_LatestPrices(List<InvestmentMetal> entities) {
        return entities.stream()
                .map(
                        this::toDTO_LatestPrices
                )
                .collect(Collectors.toList());
    }

    public List<InvestmentMetalDTO_ProductDTO> toDTO_OneLatestPrice(List<InvestmentMetal> entities) {
        return entities.stream()
                .map(
                        this::toDTO_OneLatestPrice
                )
                .collect(Collectors.toList());
    }


}
