package home.holymiko.InvestmentScraperApp.Server.DataFormat.DTO.simple;

import lombok.Getter;

@Getter
public class PortfolioDTO {

    private final long id;
    private final String owner;
    private final double beginPrice;
    private final double value;
    private final double yield;

    public PortfolioDTO(long id, String owner, double beginPrice, double value) {
        this.id = id;
        this.owner = owner;
        this.beginPrice = beginPrice;
        this.value = value;

        if(value > 0) {
            this.yield = value / beginPrice;
        } else {
            this.yield = 0;
        }
    }


}
