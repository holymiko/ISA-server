package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.InvestmentMetalDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Entity.InvestmentMetal;
import lombok.Getter;

@Getter
public class InvestmentMetalDTO_ProductDTO extends InvestmentMetalDTO {

    private final ProductDTO productDTO;

    public InvestmentMetalDTO_ProductDTO(InvestmentMetal investmentMetal, ProductDTO productDTO) {
        super(
                investmentMetal.getId(),
                investmentMetal.getDealer(),
                investmentMetal.getYield(),
                investmentMetal.getBeginPrice(),
                investmentMetal.getEndPrice(),
                investmentMetal.getBeginDate(),
                investmentMetal.getEndDate()
        );
        this.productDTO = productDTO;
    }

//    public InvestmentMetalDTO_ProductDTO(long id, Dealer dealer, double yield, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate, ProductDTO productDTO) {
//        super(id, dealer, yield, beginPrice, endPrice, beginDate, endDate);
//        this.productDTO = productDTO;
//    }
}
