package home.holymiko.InvestmentScraperApp.Server.Type.DTO.create;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PersonDTO;
import lombok.Getter;

@Getter
public class PersonAccountCreateDTO extends PersonDTO {

    // Credentials & Role
    private final AccountCreateDTO account;

    public PersonAccountCreateDTO(Long id, String firstName, String middleName, String lastName, String email, int phone, AccountCreateDTO account) {
        super(id, firstName, middleName, lastName, email, phone);
        this.account = account;
    }
}
