package home.holymiko.investment.scraper.app.server.type.dto.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatsDTO {

    private final Long rates;
    private final Long products;
    private final Long pricePairs;
    private final Long pricePairsHistory;
    private final Long tickers;
    private final Long stocks;
    private final Long links;
    private final Long linksAurumPro;
    private final Long linksBessergold;
    private final Long linksBessergoldDe;
    private final Long linksCeskaMincovna;
    private final Long linksGoldASilver;
    private final Long linksSilverum;
    private final Long linksZlataky;
    private final Long linksWithProduct;
    private final Long linksWithProductAurumPro;
    private final Long linksWithProductBessergold;
    private final Long linksWithProductBessergoldDe;
    private final Long linksWithProductCeskaMincovna;
    private final Long linksWithProductGoldASilver;
    private final Long linksWithProductSilverum;
    private final Long linksWithProductZlataky;
}
