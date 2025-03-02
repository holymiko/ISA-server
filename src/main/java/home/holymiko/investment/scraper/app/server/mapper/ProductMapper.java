package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.type.entity.Product;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.LinkDTO_Price;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.LinkDTO_Prices;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_AllPrices;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_LatestPrices;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.ProductDTO_Link_AllPrices;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    protected PricePairMapper pricePairMapper;

    @Mappings({
            @Mapping(target = "prices",  ignore = true),
            @Mapping(target = "latestPrices", ignore = true)
    })
    public abstract ProductDTO_Link_AllPrices toProductDTO_Link_AllPrices(Product entity);

    @Mappings({
            @Mapping(target = "links", ignore = true),
            @Mapping(target = "prices",  ignore = true),
            @Mapping(target = "latestPrices", ignore = true)
    })
    public abstract ProductDTO_AllPrices toProductDTO_AllPrices(Product product);

    @Mappings({
            @Mapping(target = "isTopProduct", source = "product.isTopProduct"),
            @Mapping(target = "links", ignore = true),
            @Mapping(target = "latestPrices", ignore = true)
    })
    public abstract ProductDTO_LatestPrices toProductDTO_LatestPrices(Product product);

    @BeforeMapping
    public void afterMapping(Product product, @MappingTarget ProductDTO_LatestPrices productDTO) {
        productDTO.setLinks(
                product.getLinks().stream().map(Link::getUri).collect(Collectors.toList())
        );
        productDTO.setLatestPrices(
                product.getLinks().stream().map(
                        link -> pricePairMapper.toPriceDTO(
                                link.getPricePair(),
                                product.getGrams(),
                                link.getDealer()
                        )
                ).collect(Collectors.toList())
        );
    }

    @BeforeMapping
    public void afterMapping2(Product product, @MappingTarget ProductDTO_AllPrices productDTO) {
        productDTO.setPrices(
                product.getLinks().stream().map(
                                link -> pricePairMapper.toPriceDTOs(
                                        link.getPricePairsHistory(),
                                        product.getGrams(),
                                        link.getDealer()
                                )
                        )
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        );
    }

    @BeforeMapping
    public void afterMapping3(Product product, @MappingTarget ProductDTO_Link_AllPrices productDTO) {
        productDTO.setLatestPrices(
                product.getLinks().stream().map(
                        link -> new LinkDTO_Price(
                                link.getId(),
                                link.getDealer(),
                                link.getUri(),
                                pricePairMapper.toPriceDTO(
                                        link.getPricePair(),
                                        product.getGrams()
                                )
                        )
                ).collect(Collectors.toList())
        );

        productDTO.setPrices(
                product.getLinks().stream().map(
                        link -> new LinkDTO_Prices(
                                link.getId(),
                                link.getDealer(),
                                link.getUri(),
                                pricePairMapper.toPriceDTOs(
                                        link.getPricePairsHistory(),
                                        product.getGrams()
                                )
                        )
                ).collect(Collectors.toList())
        );
    }
}
