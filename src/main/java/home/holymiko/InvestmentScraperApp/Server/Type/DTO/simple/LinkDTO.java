package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
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

    @Override
    public String toString() {
        return "LinkDTO{" +
                "Id=" + Id +
                ", dealer=" + dealer +
                ", url='" + url + '\'' +
                ", productId=" + productId +
                '}';
    }
}
