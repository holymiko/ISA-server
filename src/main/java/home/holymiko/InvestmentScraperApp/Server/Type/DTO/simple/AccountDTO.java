package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import lombok.Getter;

@Getter
public class AccountDTO {

    private final Long id;

    private final String username;

    public AccountDTO (String username, Long id) {
        this.username = username;
        this.id = id;
    }
}
