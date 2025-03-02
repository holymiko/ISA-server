package home.holymiko.investment.scraper.app.server.type.dto.create;

import home.holymiko.investment.scraper.app.server.type.dto.simple.PersonDTO;
import lombok.Getter;

@Getter
public class PersonAccountCreateDTO extends PersonDTO {

    // Credentials & Role
    private final AccountCreateDTO account;

    public PersonAccountCreateDTO(Long id, String firstName, String middleName, String lastName, String email, Long phone, AccountCreateDTO account) {
        super(id, firstName, middleName, lastName, email, phone);
        this.account = account;
    }
}
