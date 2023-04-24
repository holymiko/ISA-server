package home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PersonDTO;
import lombok.Getter;

@Getter
public class PersonAccountDTO extends PersonDTO {

    private final AccountDTO account;

    public PersonAccountDTO(Long id, String firstName, String middleName, String lastName, String email, Long phone, AccountDTO account) {
        super(id, firstName, middleName, lastName, email, phone);
        this.account = account;
    }
}
