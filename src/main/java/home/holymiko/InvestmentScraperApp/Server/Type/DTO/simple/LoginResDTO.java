package home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Doesn't include password
 */
@Getter
@Setter
@AllArgsConstructor
public class LoginResDTO {

    private String username;
    private String token;

}
