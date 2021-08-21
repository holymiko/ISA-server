package home.holymiko.InvestmentScraperApp.Server.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.DTO.simple.InvestmentMetalDTO;
import home.holymiko.InvestmentScraperApp.Server.DTO.simple.ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Enum.Dealer;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class InvestmentMetalDTO_ProductDTO extends InvestmentMetalDTO {

    private final ProductDTO productDTO;

    public InvestmentMetalDTO_ProductDTO(long id, Dealer dealer, double yield, double beginPrice, double endPrice, LocalDate beginDate, LocalDate endDate, ProductDTO productDTO) {
        super(id, dealer, yield, beginPrice, endPrice, beginDate, endDate);
        this.productDTO = productDTO;
    }

}
