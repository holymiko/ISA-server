package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
public class AccountCreateDTO {

    private final String username;
    private final String password;

    public AccountCreateDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
