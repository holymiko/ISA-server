package home.holymiko.investment.scraper.app.server.type.dto.create;

import home.holymiko.investment.scraper.app.server.type.dto.simple.PersonDTO;
import lombok.Getter;

@Getter
public class PersonCreateDTO extends PersonDTO {

    private final Long accountId;

    public PersonCreateDTO(Long id, String firstName, String middleName, String lastName, String email, Long phone, Long accountId) {
        super(id, firstName, middleName, lastName, email, phone);
        this.accountId = accountId;
    }
}
