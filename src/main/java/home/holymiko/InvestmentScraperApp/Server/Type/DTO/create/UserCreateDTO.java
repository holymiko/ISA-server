package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserCreateDTO {

    // Credentials & Role
    private final AccountCreateDTO account;

    private final String firstName;
    private final String middleName;
    private final String lastName;

    private final String email;
    private final Long phone;

}
