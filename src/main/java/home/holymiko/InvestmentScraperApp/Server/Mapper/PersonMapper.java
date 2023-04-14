package home.holymiko.InvestmentScraperApp.Server.Mapper;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PersonAccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.PersonAccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.PersonCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PersonDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {

    public abstract PersonDTO toPersonDTO(Person entity);

    public abstract Person toPerson(PersonCreateDTO createDTO);

    public abstract Person toPerson(PersonAccountCreateDTO createDTO);

}
