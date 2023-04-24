package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PricePairDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class LinkDTO_Prices {

    private Long id;
    private Dealer dealer;
    private String uri;
    private List<PricePairDTO> prices;

}
