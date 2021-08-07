package home.holymiko.ScrapApp.Server.DTO.simple;

import lombok.Getter;

@Getter
public class PortfolioDTO {

    private final long id;
    private final String owner;
    private final double beginPrice;
    private final double value;
    private final double yield;

    public PortfolioDTO(long id, String owner, double beginPrice, double value, double yield) {
        this.id = id;
        this.owner = owner;
        this.beginPrice = beginPrice;
        this.value = value;
        this.yield = yield;
    }
}
