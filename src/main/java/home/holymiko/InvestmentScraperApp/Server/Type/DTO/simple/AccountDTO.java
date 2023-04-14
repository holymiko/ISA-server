package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Doesn't include password
 */
@Getter
@Setter
@AllArgsConstructor
public class AccountDTO {

    private Long id;
    private String username;
    private Long personId;
    private Role role;

}
