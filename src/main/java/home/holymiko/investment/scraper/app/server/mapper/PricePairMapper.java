package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.dto.advanced.PricePairDTO_Dealer;
import home.holymiko.investment.scraper.app.server.type.dto.simple.PricePairDTO;
import home.holymiko.investment.scraper.app.server.type.entity.PricePair;
import home.holymiko.investment.scraper.app.server.type.entity.PricePairHistory;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
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
        @Mapping(target = "availability", source = "entity.availability"),
        @Mapping(target = "availabilityMessage", source = "entity.availabilityMessage"),
        @Mapping(target = "grams", source = "grams"),
        @Mapping(target = "dealer", source = "dealer")
    })
    PricePairDTO_Dealer toPriceDTO(PricePair entity, Double grams, Dealer dealer);

    @Mappings({
            @Mapping(target = "price", source = "entity.sellPrice.amount"),
            @Mapping(target = "redemption", source = "entity.redemption.amount"),
            @Mapping(target = "priceDateTime", source = "entity.sellPrice.dateTime"),
            @Mapping(target = "redemptionDateTime", source = "entity.redemption.dateTime"),
            @Mapping(target = "availability", source = "entity.availability"),
            @Mapping(target = "availabilityMessage", ignore = true),
            @Mapping(target = "grams", source = "grams"),
            @Mapping(target = "dealer", source = "dealer")
    })
    PricePairDTO_Dealer toPriceDTO(PricePairHistory entity, Double grams, Dealer dealer);

    @Mappings({
            @Mapping(target = "price", source = "entity.sellPrice.amount"),
            @Mapping(target = "redemption", source = "entity.redemption.amount"),
            @Mapping(target = "priceDateTime", source = "entity.sellPrice.dateTime"),
            @Mapping(target = "redemptionDateTime", source = "entity.redemption.dateTime"),
            @Mapping(target = "availability", source = "entity.availability"),
            @Mapping(target = "availabilityMessage", source = "entity.availabilityMessage"),
            @Mapping(target = "grams", source = "grams")
    })
    PricePairDTO toPriceDTO(PricePair entity, Double grams);

    @Mappings({
            @Mapping(target = "price", source = "entity.sellPrice.amount"),
            @Mapping(target = "redemption", source = "entity.redemption.amount"),
            @Mapping(target = "priceDateTime", source = "entity.sellPrice.dateTime"),
            @Mapping(target = "redemptionDateTime", source = "entity.redemption.dateTime"),
            @Mapping(target = "availability", source = "entity.availability"),
            @Mapping(target = "availabilityMessage", ignore = true),
            @Mapping(target = "grams", source = "grams")
    })
    PricePairDTO toPriceDTO(PricePairHistory entity, Double grams);

    // Used in ProductMapper
    default List<PricePairDTO_Dealer> toPriceDTOs(List<PricePairHistory> pricePairs, Double grams, Dealer dealer) {
        return pricePairs
                .stream()
                .map(x -> toPriceDTO(x, grams, dealer))
                .collect(Collectors.toList());
    }

    default List<PricePairDTO> toPriceDTOs(List<PricePairHistory> pricePairs, Double grams) {
        return pricePairs
                .stream()
                .map(x -> toPriceDTO(x, grams))
                .collect(Collectors.toList());
    }

}
