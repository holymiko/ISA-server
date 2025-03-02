package home.holymiko.investment.scraper.app.server.type.entity;

import home.holymiko.investment.scraper.app.server.type.enums.GrahamGrade;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;

import java.util.Date;

@XmlRootElement(name = "grahamStockHistory")
@XmlAccessorType(XmlAccessType.FIELD)
@Entity
@Getter
public class StockGrahamHistory {
    @Id
    @GeneratedValue
    private long id;

    private Date timeStamp;
    private String name;
    private String ticker;
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

    public StockGrahamHistory(Date timeStamp, String name, String ticker, GrahamGrade grahamGrade, String currency, Double ratingScore, Double sizeInSales, Double assetsLiabilities, Double netAssetsLongTermDebt, Double earningsStability, Double dividendRecord, Double earningGrowth, Double grahamNumber, Double ncav, Double equityDebt, Double sizeInAssets, Double defensivePrice, Double enterprisingPrice, Double ncavPrice, Double intrinsicPrice, Double previousClose, Double intrinsicValue) {
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
