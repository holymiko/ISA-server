package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DTO.advanced.ProductDTO_AllPrices;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.PriceDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.Price;
import home.holymiko.InvestmentScraperApp.Server.Entity.Product;
import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PriceMapper {

    PriceDTO toDTO(Price entity, double grams);

    @AfterMapping
    static void setSpreadAndPricePerGram(@MappingTarget PriceDTO target, Price entity, double grams/* @Context BranchRepo branchRepo*/) {
        target.setSpread(entity.getRedemption() / entity.getPrice());
        target.setPricePerGram(entity.getPrice() / grams);
    }

    ProductDTO_AllPrices toProductDTO_AllPrices(Product entity);

}
