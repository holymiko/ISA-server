package home.holymiko.investment.scraper.app.server.type.dto.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class LoginReqDTO {

    private String username;
    private String password;

}
