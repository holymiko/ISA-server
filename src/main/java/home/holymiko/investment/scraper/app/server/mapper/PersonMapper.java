package home.holymiko.investment.scraper.app.server.mapper;

import home.holymiko.investment.scraper.app.server.type.dto.create.PersonAccountCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.PersonCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.PersonDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Person;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class PersonMapper {

    public abstract PersonDTO toPersonDTO(Person entity);

    public abstract Person toPerson(PersonCreateDTO createDTO);

    public abstract Person toPerson(PersonAccountCreateDTO createDTO);

}
