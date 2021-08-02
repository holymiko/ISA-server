package home.holymiko.ScrapApp.Server.DTO.create;

import java.util.List;

public class PortfolioCreateDTO {
    private final String owner;
    private final List<Long> investmentIds;

    public PortfolioCreateDTO(String owner, List<Long> investments) {
        this.owner = owner;
        this.investmentIds = investments;
    }

    public String getOwner() {
        return owner;
    }

    public List<Long> getInvestmentIds() {
        return investmentIds;
    }

}
