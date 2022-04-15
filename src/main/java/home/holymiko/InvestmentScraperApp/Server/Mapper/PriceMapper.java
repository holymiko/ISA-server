package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PriceMapper {


    @Mappings({
        @Mapping(target = "price", source = "entity.sellPrice.amount"),
        @Mapping(target = "redemption", source = "entity.redemption.amount"),
        @Mapping(target = "priceDateTime", source = "entity.sellPrice.dateTime"),
        @Mapping(target = "redemptionDateTime", source = "entity.redemption.dateTime")
    })
    PriceDTO toPriceDTO(PricePair entity, double grams);

    default List<PriceDTO> toPriceDTOs(List<PricePair> pricePairs, double grams) {
        return pricePairs
                .stream()
                .map(
                        price -> toPriceDTO(price, grams)
                )
                .collect(Collectors.toList());
    }

}
