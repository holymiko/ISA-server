package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices_OneLatestPrice;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_OneLatestPrice;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    private static final String LINKS_AS_STRINGS = "java( product.getLinksAsString() )";
    private static final String LATEST_PRICE_DTO = "java( priceMapper.toPriceDTO(product.getPriceByBestRedemption(), product.getGrams()) )";
    private static final String LATEST_PRICES_DTOS = "java( priceMapper.toPriceDTOs(product.getLatestPrices(), product.getGrams()) )";

    @Autowired
    protected PriceMapper priceMapper;

    public abstract ProductDTO toProductDTO(Product entity);

    @Mapping(target = "latestPrice", source = "priceDTO")
    public abstract ProductDTO_OneLatestPrice toProductDTO_OneLatestPrice(Product product, PriceDTO priceDTO);

    @Mapping(target = "latestPrice", expression = LATEST_PRICE_DTO)
    public abstract ProductDTO_OneLatestPrice toProductDTO_OneLatestPrice(Product product);

    @Mappings({
            @Mapping(target = "links", expression = LINKS_AS_STRINGS),
            @Mapping(target = "latestPrices", expression = LATEST_PRICES_DTOS)
    })
    public abstract ProductDTO_LatestPrices toProductDTO_LatestPrices(Product product);

    @Mappings({
            @Mapping(target = "latestPrice", expression = LATEST_PRICE_DTO),
            @Mapping(target = "latestPrices", expression = LATEST_PRICES_DTOS)
    })
    public abstract ProductDTO_LatestPrices_OneLatestPrice toProductDTO_LatestPrices_OneLatestPrice(Product product);

    @Mappings({
            @Mapping(target = "links", expression = LINKS_AS_STRINGS),
            @Mapping(target = "prices", expression = "java( priceMapper.toPriceDTOs(product.getPrices(), product.getGrams()) )"),
            @Mapping(target = "latestPrices", expression = LATEST_PRICES_DTOS)
    })
    public abstract ProductDTO_AllPrices toProductDTO_AllPrices(Product product);

}
