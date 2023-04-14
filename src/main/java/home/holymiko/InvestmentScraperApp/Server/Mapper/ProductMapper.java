package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.ProductRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    private static final String LINKS_AS_STRINGS = "java( product.getLinksAsString() )";
    private static final String LATEST_PRICES_DTOS = "java( pricePairMapper.toPriceDTOs(pricePairRepository.findLatestPricePairsByProductId(product.getId()), product.getGrams()) )";

    @Autowired
    protected PricePairMapper pricePairMapper;

    public abstract ProductDTO toProductDTO(Product entity);

    @Mappings({
            @Mapping(target = "links", expression = LINKS_AS_STRINGS),
            @Mapping(target = "prices", expression = "java( pricePairMapper.toPriceDTOs(product.getPricePairs(), product.getGrams()) )"),
            @Mapping(target = "latestPrices", expression = LATEST_PRICES_DTOS)
    })
    public abstract ProductDTO_AllPrices toProductDTO_AllPrices(Product product, @Context PricePairRepository pricePairRepository);

    @Mappings({
            @Mapping(target = "links", expression = LINKS_AS_STRINGS),
            @Mapping(target = "latestPrices", expression = LATEST_PRICES_DTOS)
    })
    public abstract ProductDTO_LatestPrices toProductDTO_LatestPrices(Product product, @Context PricePairRepository pricePairRepository);

}
