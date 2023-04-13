package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.Pattern;

@Getter
@AllArgsConstructor
public class AccountCreateDTO {

    private final String username;
    private final String password;
    private final Role role;

}
