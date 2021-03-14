package home.holymiko.ScrapApp.Server.Entity;

import home.holymiko.ScrapApp.Server.Entity.Enum.GrahamGrade;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class Stock {
    @Id
    @GeneratedValue
    private int id;
    private String name;
    @OneToOne
    private Ticker ticker;
    private GrahamGrade grahamGrade;
    private String currency;

    private Double ratingScore;
    private Double sizeInSales;
    private Double assetsLiabilities;
    private Double netAssetsLongTermDebt;
    private Double earningsStability;
    private Double dividend;
    private Double ncav;
    private Double equityDebt;
    private Double sizeInAssets;

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

    public Stock(String name, Ticker ticker, GrahamGrade grahamGrade, String currency, Double ratingScore,

                 Double sizeInSales, Double assetsLiabilities,
                 Double netAssetsLongTermDebt, Double earningsStability, Double dividend,
                 Double ncav, Double equityDebt, Double sizeInAssets,

                 Double defensivePrice, Double enterprisingPrice,
                 Double ncavPrice, Double intrinsicPrice,
                 Double previousClose, Double intrinsicValue) {
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

///// SET

    public void setName(String name) {
        this.name = name;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setTicker(Ticker ticker) {
        this.ticker = ticker;
    }

    public void setRatingScore(Double ratingScore) { this.ratingScore = ratingScore; }

    public void setSizeInSales(Double sizeInSales) {
        this.sizeInSales = sizeInSales;
    }

    public void setAssetsLiabilities(Double assetsLiabilities) {
        this.assetsLiabilities = assetsLiabilities;
    }

    public void setNetAssetsLongTermDebt(Double netAssetsLongTermDebt) {
        this.netAssetsLongTermDebt = netAssetsLongTermDebt;
    }

    public void setEarningsStability(Double earningsStability) {
        this.earningsStability = earningsStability;
    }

    public void setDividend(Double dividend) {
        this.dividend = dividend;
    }

    public void setNcav(Double ncav) {
        this.ncav = ncav;
    }

    public void setEquityDebt(Double equityDebt) {
        this.equityDebt = equityDebt;
    }

    public void setSizeInAssets(Double sizeInAssets) {
        this.sizeInAssets = sizeInAssets;
    }

    public void setDefensivePrice(Double defensivePrice) {
        this.defensivePrice = defensivePrice;
    }

    public void setEnterprisingPrice(Double enterprisingPrice) {
        this.enterprisingPrice = enterprisingPrice;
    }

    public void setNcavPrice(Double ncavPrice) {
        this.ncavPrice = ncavPrice;
    }

    public void setGrahamGrade(GrahamGrade grahamGrade) {
        this.grahamGrade = grahamGrade;
    }

    public void setIntrinsicPrice(Double intrinsicPrice) {
        this.intrinsicPrice = intrinsicPrice;
    }

    public void setPreviousClose(Double previousClose) {
        this.previousClose = previousClose;
    }

    public void setIntrinsicValue(Double intrinsicValue) {
        this.intrinsicValue = intrinsicValue;
    }



    ///// GET

    public String getName() {
        return name;
    }

    public String getCurrency() {
        return currency;
    }

    public int getId() {
        return id;
    }

    public Ticker getTicker() {
        return ticker;
    }

    public Double getRatingScore() {
        return ratingScore;
    }

    public Double getSizeInSales() {
        return sizeInSales;
    }

    public Double getAssetsLiabilities() {
        return assetsLiabilities;
    }

    public Double getNetAssetsLongTermDebt() {
        return netAssetsLongTermDebt;
    }

    public Double getEarningsStability() {
        return earningsStability;
    }

    public Double getDividend() {
        return dividend;
    }

    public Double getNcav() {
        return ncav;
    }

    public Double getEquityDebt() {
        return equityDebt;
    }

    public Double getSizeInAssets() {
        return sizeInAssets;
    }

    public Double getDefensivePrice() {
        return defensivePrice;
    }

    public Double getEnterprisingPrice() {
        return enterprisingPrice;
    }

    public Double getNcavPrice() {
        return ncavPrice;
    }

    public GrahamGrade getGrahamGrade() {
        return grahamGrade;
    }

    public Double getIntrinsicPrice() {
        return intrinsicPrice;
    }

    public Double getPreviousClose() {
        return previousClose;
    }

    public Double getIntrinsicValue() {
        return intrinsicValue;
    }
}
