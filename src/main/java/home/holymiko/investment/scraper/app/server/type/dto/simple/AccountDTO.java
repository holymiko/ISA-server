package home.holymiko.investment.scraper.app.server.type.dto.simple;

import home.holymiko.investment.scraper.app.server.type.enums.Role;
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
