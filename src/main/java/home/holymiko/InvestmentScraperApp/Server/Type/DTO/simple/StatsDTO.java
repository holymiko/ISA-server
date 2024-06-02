package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

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
    private final Long linksNoProduct;
    private final Long linksNoProductAurumPro;
    private final Long linksNoProductBessergold;
    private final Long linksNoProductBessergoldDe;
    private final Long linksNoProductCeskaMincovna;
    private final Long linksNoProductGoldASilver;
    private final Long linksNoProductSilverum;
    private final Long linksNoProductZlataky;
}
