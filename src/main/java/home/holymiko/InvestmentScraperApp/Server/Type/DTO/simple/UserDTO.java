package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Doesn't include password
 */
@Getter
@AllArgsConstructor
public class UserDTO {

    private final String firstName;
    private final String middleName;
    private final String lastName;

    private final String email;
    private final int phone;

}
