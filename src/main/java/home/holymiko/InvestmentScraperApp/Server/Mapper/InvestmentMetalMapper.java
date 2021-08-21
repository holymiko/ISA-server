package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.InvestmentMetalDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.InvestmentMetal;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface InvestmentMetalMapper {

    InvestmentMetalDTO toInvestmentMetalDTO(InvestmentMetal entity);

//    @Mapping(target = "productDTO", source = "investmentMetal.product")
//    InvestmentMetalDTO_ProductDTO toDTO_OneLatestPrice(InvestmentMetal investmentMetal);

//    @AfterMapping
//    static void setOneLatestPrice(@MappingTarget InvestmentMetalDTO target/* @Context BranchRepo branchRepo*/) {
//    }
//    @AfterMapping
//    static void setAllLatestPrices(@MappingTarget InvestmentMetalDTO target/* @Context BranchRepo branchRepo*/) {
//    }

//    @Mapping(target = "latestPrice", source = "priceDTO")
//    InvestmentMetalDTO_ProductDTO toDTO_OneLatestPrice(InvestmentMetal product, PriceDTO priceDTO);


}
