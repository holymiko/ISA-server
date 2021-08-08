package home.holymiko.ScrapApp.Server.DTO.advanced;

import home.holymiko.ScrapApp.Server.DTO.simple.InvestmentMetalDTO;
import home.holymiko.ScrapApp.Server.DTO.simple.ProductDTO;
import home.holymiko.ScrapApp.Server.Entity.InvestmentMetal;
import home.holymiko.ScrapApp.Server.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentMetalDTO_ProductDTO extends InvestmentMetalDTO {

    private final ProductDTO product;

    public InvestmentMetalDTO_ProductDTO(long id, Dealer dealer, double yield, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate, ProductDTO product) {
        super(id, dealer, yield, beginPrice, endPrice, beginDate, endDate);
        this.product = product;
    }

    public InvestmentMetalDTO_ProductDTO(InvestmentMetal investmentMetal, ProductDTO product) {
        super(
                investmentMetal.getId(),
                investmentMetal.getDealer(),
                investmentMetal.getYield(),
                investmentMetal.getBeginPrice(),
                investmentMetal.getEndPrice(),
                investmentMetal.getBeginDate(),
                investmentMetal.getEndDate()
        );
        this.product = product;
    }
}
