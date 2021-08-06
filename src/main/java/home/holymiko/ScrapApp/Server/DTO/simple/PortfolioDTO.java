package home.holymiko.ScrapApp.Server.DTO.simple;

import lombok.Getter;

import java.util.List;

@Getter
public class PortfolioDTO {
    private final long id;
    private final String owner;
    private final double beginPrice;
    private final double value;
    private final double yield;
    private final List<Long> investmentIds;

    public PortfolioDTO(long id, String owner, double beginPrice, double value, double yield, List<Long> investments) {
        this.id = id;
        this.owner = owner;
        this.beginPrice = beginPrice;
        this.value = value;
        this.yield = yield;
        this.investmentIds = investments;
    }

    public String getTextYield() {
        if (yield >= 1)
            return "+" + String.format("%.2f", (yield - 1) * 100) + "%";
        return "-" + String.format("%.2f", (100 - yield * 100)) + "%";
    }

}
