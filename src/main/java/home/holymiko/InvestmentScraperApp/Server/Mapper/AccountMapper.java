package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    public abstract AccountDTO toAccountDTO(Account entity);

    public abstract Account toAccount(AccountCreateDTO createDTO);
}
