package home.holymiko.investment.scraper.app.server.type.dto.simple;

import home.holymiko.investment.scraper.app.server.type.entity.Ticker;
import home.holymiko.investment.scraper.app.server.type.enums.GrahamGrade;
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
