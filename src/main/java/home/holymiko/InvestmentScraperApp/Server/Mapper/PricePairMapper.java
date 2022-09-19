package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PricePairDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PricePairMapper {

    @Mappings({
        @Mapping(target = "price", source = "entity.sellPrice.amount"),
        @Mapping(target = "redemption", source = "entity.redemption.amount"),
        @Mapping(target = "priceDateTime", source = "entity.sellPrice.dateTime"),
        @Mapping(target = "redemptionDateTime", source = "entity.redemption.dateTime"),
        @Mapping(target = "grams", source = "entity.product.grams")
    })
    PricePairDTO toPriceDTO(PricePair entity);

    default List<PricePairDTO> toPriceDTOs(List<PricePair> pricePairs) {
        return pricePairs
                .stream()
                .map(this::toPriceDTO)
                .collect(Collectors.toList());
    }

}
