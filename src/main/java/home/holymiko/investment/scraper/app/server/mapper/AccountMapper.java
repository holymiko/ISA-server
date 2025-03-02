package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.dto.advanced.PersonAccountDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.AccountCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.AccountDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Account;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public abstract class AccountMapper {

    @Mappings({
            @Mapping(target = "personId", source = "entity.person.id")
    })
    public abstract AccountDTO toAccountDTO(Account entity);

    @Mappings({
            @Mapping(target = "id", source = "entity.person.id"),
            @Mapping(target = "firstName", source = "entity.person.firstName"),
            @Mapping(target = "middleName", source = "entity.person.middleName"),
            @Mapping(target = "lastName", source = "entity.person.lastName"),
            @Mapping(target = "email", source = "entity.person.email"),
            @Mapping(target = "phone", source = "entity.person.phone"),
            @Mapping(target = "account", source = "entity"),
    })
    public abstract PersonAccountDTO toPersonAccountDTO(Account entity);
    public abstract Account toAccount(AccountCreateDTO createDTO);
}
