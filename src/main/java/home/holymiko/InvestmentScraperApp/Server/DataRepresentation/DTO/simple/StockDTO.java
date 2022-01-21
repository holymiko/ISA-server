package home.holymiko.InvestmentScraperApp.Server.DataRepresentation.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.GrahamGrade;
import lombok.Getter;

@Getter
public class StockDTO {
    private final int id;
    private final String name;
    private final Ticker ticker;
    private final GrahamGrade grahamGrade;
    private final String currency;

    private final Double ratingScore;
    private final Double sizeInSales;
    private final Double assetsLiabilities;
    private final Double netAssetsLongTermDebt;
    private final Double earningsStability;
    private final Double dividend;
    private final Double ncav;
    private final Double equityDebt;
    private final Double sizeInAssets;

    private final Double defensivePrice;
    private final Double enterprisingPrice;
    private final Double ncavPrice;
    private final Double intrinsicPrice;
    private final Double previousClose;
    private final Double intrinsicValue;

    public StockDTO(int id, String name, Ticker ticker, GrahamGrade grahamGrade, String currency, Double ratingScore, Double sizeInSales, Double assetsLiabilities, Double netAssetsLongTermDebt, Double earningsStability, Double dividend, Double ncav, Double equityDebt, Double sizeInAssets, Double defensivePrice, Double enterprisingPrice, Double ncavPrice, Double intrinsicPrice, Double previousClose, Double intrinsicValue) {
        this.id = id;
        this.name = name;
        this.ticker = ticker;
        this.grahamGrade = grahamGrade;
        this.currency = currency;
        this.ratingScore = ratingScore;
        this.sizeInSales = sizeInSales;
        this.assetsLiabilities = assetsLiabilities;
        this.netAssetsLongTermDebt = netAssetsLongTermDebt;
        this.earningsStability = earningsStability;
        this.dividend = dividend;
        this.ncav = ncav;
        this.equityDebt = equityDebt;
        this.sizeInAssets = sizeInAssets;
        this.defensivePrice = defensivePrice;
        this.enterprisingPrice = enterprisingPrice;
        this.ncavPrice = ncavPrice;
        this.intrinsicPrice = intrinsicPrice;
        this.previousClose = previousClose;
        this.intrinsicValue = intrinsicValue;
    }
}
