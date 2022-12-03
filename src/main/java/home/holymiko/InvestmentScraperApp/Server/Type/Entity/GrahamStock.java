package home.holymiko.InvestmentScraperApp.Server.Type.Entity;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.Date;

@Entity
@Getter
public class GrahamStock {
    @Id
    @GeneratedValue
    private int id;
    private Date timeStamp;
    private String name;
    @OneToOne
    private Ticker ticker;
    private GrahamGrade grahamGrade;
    // TODO Think about converting to Currency enum
    private String currency;
    private Double ratingScore;

    // Graham Ratings
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

    // Graham Result
    private Double defensivePrice;
    private Double enterprisingPrice;
    private Double ncavPrice;
    private Double intrinsicPrice;
    private Double previousClose;
    private Double intrinsicValue;

    // TODO Financial Condition
    // TODO Per Share Value
    // TODO Dividend History
    // TODO About Stock
    // TODO private String submittedBy
    // TODO private ? createdOn
    // TODO private ? updatedOn

    public GrahamStock() {}

    public GrahamStock(Ticker ticker) {
        this.ticker = ticker;
    }

    public GrahamStock(Date timeStamp, String name, Ticker ticker, GrahamGrade grahamGrade, String currency, Double ratingScore, Double sizeInSales, Double assetsLiabilities, Double netAssetsLongTermDebt, Double earningsStability, Double dividendRecord, Double earningGrowth, Double grahamNumber, Double ncav, Double equityDebt, Double sizeInAssets, Double defensivePrice, Double enterprisingPrice, Double ncavPrice, Double intrinsicPrice, Double previousClose, Double intrinsicValue) {
        this.timeStamp = timeStamp;
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
