package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Price;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceDTO toPriceDTO(Price entity, double grams);

    default List<PriceDTO> toPriceDTOs(List<Price> prices, double grams) {
        return prices
                .stream()
                .map(
                        price -> toPriceDTO(price, grams)
                )
                .collect(Collectors.toList());
    }

}
