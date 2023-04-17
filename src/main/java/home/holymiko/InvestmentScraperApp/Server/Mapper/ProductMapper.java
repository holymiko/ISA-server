package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.PricePairRepository;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.ProductDTO_LatestPrices;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ProductMapper {

    @Autowired
    protected PricePairMapper pricePairMapper;

    public abstract ProductDTO toProductDTO(Product entity);

    @Mappings({
            @Mapping(target = "links", ignore = true),
            @Mapping(target = "prices",  ignore = true),
            @Mapping(target = "latestPrices", ignore = true)
    })
    public abstract ProductDTO_AllPrices toProductDTO_AllPrices(Product product, @Context PricePairRepository pricePairRepository);

    @Mappings({
            @Mapping(target = "links", ignore = true),
            @Mapping(target = "latestPrices", ignore = true)
    })
    public abstract ProductDTO_LatestPrices toProductDTO_LatestPrices(Product product, @Context PricePairRepository pricePairRepository);

    @BeforeMapping
    public void afterMapping(Product product, @Context PricePairRepository pricePairRepository, @MappingTarget ProductDTO_LatestPrices productDTO) {
        productDTO.setLinks(
                product.getLinks().stream().map(Link::getUri).collect(Collectors.toList())
        );
        productDTO.setLatestPrices(
                product.getLinks().stream().map(
                        link -> pricePairMapper.toPriceDTO(
                                pricePairRepository.findLatestPricePairByLinkId(link.getId()),
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
                                link -> pricePairMapper.toPriceDTOs(link.getPricePairs(), product.getGrams(), link.getDealer())
                        )
                        .flatMap(List::stream)
                        .collect(Collectors.toList())
        );
    }

}
