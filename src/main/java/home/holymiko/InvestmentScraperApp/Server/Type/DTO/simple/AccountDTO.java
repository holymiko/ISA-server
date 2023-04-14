package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Doesn't include password
 */
@Getter
@AllArgsConstructor
public class AccountDTO {

    private final Long id;
    private final String username;
    private final Role role;

}
