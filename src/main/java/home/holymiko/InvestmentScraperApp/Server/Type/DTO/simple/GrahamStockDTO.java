package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Date;

@Getter
@AllArgsConstructor
public class GrahamStockDTO {

    private final int id;

    private final Date timeStamp;
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

}
