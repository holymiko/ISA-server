package home.holymiko.investment.scraper.app.server.type.dto.create;

import home.holymiko.investment.scraper.app.server.type.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@AllArgsConstructor
public class AccountCreateDTO {

    private final String username;
    @Setter
    private String password;
    @Setter
    private Role role;

}
