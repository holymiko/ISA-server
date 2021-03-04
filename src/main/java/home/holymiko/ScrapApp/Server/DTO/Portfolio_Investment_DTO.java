package home.holymiko.ScrapApp.Server.DTO;

import java.util.List;

public class Portfolio_Investment_DTO {

    private final long id;
    private final String owner;
    private final double beginPrice;
    private final double value;
    private final double yield;
    private final List<InvestmentDTO> investments;

    public Portfolio_Investment_DTO(long id, String owner, double beginPrice, double value, double yield, List<InvestmentDTO> investmentDTOS) {
        this.id = id;
        this.owner = owner;
        this.beginPrice = beginPrice;
        this.value = value;
        this.yield = yield;
        this.investments = investmentDTOS;
    }

    public long getId() {
        return id;
    }

    public double getYield() {
        return yield;
    }

    public double getBeginPrice() {
        return beginPrice;
    }

    public double getValue() {
        return value;
    }

    public List<InvestmentDTO> getInvestments() {
        return investments;
    }

    public String getOwner() {
                return owner;
            }

}

