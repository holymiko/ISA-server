package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.Getter;

@Getter
public class LinkDTO {
    private final Long Id;
    private final Dealer dealer;
    private final String uri;
    private final Long productId;

    public LinkDTO(Long id, Dealer dealer, String uri, Long productId) {
        Id = id;
        this.dealer = dealer;
        this.uri = uri;
        this.productId = productId;
    }

    @Override
    public String toString() {
        return "LinkDTO{" +
                "Id=" + Id +
                ", dealer=" + dealer +
                ", url='" + uri + '\'' +
                ", productId=" + productId +
                '}';
    }
}
