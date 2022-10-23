package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
@Getter
public class Stock {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToOne
    private Ticker ticker;
    private GrahamGrade grahamGrade;
    // TODO Think about converting to Currency enum
    private String currency;
    private Double ratingScore;

    // Ratings
    private Double sizeInSales;
    private Double assetsLiabilities;
    private Double netAssetsLongTermDebt;
    private Double earningsStability;
    private Double dividendRecord;
    private Double earningGrowth;
    private Double grahamNumber;
    private Double ncav;
    private Double equityDebt;
    private Double sizeInAssets;

    // Results
    private Double defensivePrice;
    private Double enterprisingPrice;
    private Double ncavPrice;
    private Double intrinsicPrice;
    private Double previousClose;
    private Double intrinsicValue;

    public Stock() {}

    public Stock(Ticker ticker) {
        this.ticker = ticker;
    }

    public Stock(String name, Ticker ticker, GrahamGrade grahamGrade, String currency, Double ratingScore, Double sizeInSales, Double assetsLiabilities, Double netAssetsLongTermDebt, Double earningsStability, Double dividendRecord, Double earningGrowth, Double grahamNumber, Double ncav, Double equityDebt, Double sizeInAssets, Double defensivePrice, Double enterprisingPrice, Double ncavPrice, Double intrinsicPrice, Double previousClose, Double intrinsicValue) {
        this.name = name;
        this.ticker = ticker;
        this.grahamGrade = grahamGrade;
        this.currency = currency;
        this.ratingScore = ratingScore;
        this.sizeInSales = sizeInSales;
        this.assetsLiabilities = assetsLiabilities;
        this.netAssetsLongTermDebt = netAssetsLongTermDebt;
        this.earningsStability = earningsStability;
        this.dividendRecord = dividendRecord;
        this.earningGrowth = earningGrowth;
        this.grahamNumber = grahamNumber;
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
