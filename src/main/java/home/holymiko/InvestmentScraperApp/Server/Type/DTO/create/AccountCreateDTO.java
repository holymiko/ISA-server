package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AccountCreateDTO {

    private final String username;
    private final String password;
    @Setter
    private Role role;

}
