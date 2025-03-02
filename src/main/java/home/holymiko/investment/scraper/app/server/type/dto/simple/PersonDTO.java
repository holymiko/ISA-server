package home.holymiko.investment.scraper.app.server.type.dto.simple;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Doesn't include password
 */
@Getter
@AllArgsConstructor
public class PersonDTO {

    private final Long id;

    private final String firstName;
    private final String middleName;
    private final String lastName;

    private final String email;
    private final Long phone;

}
