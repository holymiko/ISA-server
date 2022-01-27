package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import lombok.Getter;

@Getter
public class LinkDTO {
    private final Long Id;
    private final Dealer dealer;
    private final String url;
    private final Long productId;

    public LinkDTO(Long id, Dealer dealer, String url, Long productId) {
        Id = id;
        this.dealer = dealer;
        this.url = url;
        this.productId = productId;
    }
}
