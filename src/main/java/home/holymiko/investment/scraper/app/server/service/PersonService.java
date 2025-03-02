package home.holymiko.investment.scraper.app.server.service;

import home.holymiko.investment.scraper.app.server.api.repository.AccountRepository;
import home.holymiko.investment.scraper.app.server.api.repository.PersonRepository;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.mapper.AccountMapper;
import home.holymiko.investment.scraper.app.server.mapper.PersonMapper;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.PersonAccountDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.PersonAccountCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.PersonCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.PersonDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Account;
import home.holymiko.investment.scraper.app.server.type.entity.Person;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class PersonService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonService.class);
    private final PersonRepository personRepository;
    private final AccountService accountService;
    private final PersonMapper personMapper;

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    /////// FIND AS DTO

    public List<PersonAccountDTO> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toPersonAccountDTO)
                .collect(Collectors.toList());
    }

    public PersonAccountDTO findByIdAsDTO(long id) {
        return accountMapper.toPersonAccountDTO( accountService.findByPersonId(id) );
    }

    /////// POST

    @Transactional
    public PersonDTO update(long id, PersonDTO personDTO) {
        Person person = findById(id);
        // TODO Use mapper and test
        person.setFirstName(personDTO.getFirstName());
        person.setMiddleName(personDTO.getMiddleName());
        person.setLastName(personDTO.getLastName());
        person.setEmail(personDTO.getEmail());
        person.setPhone(personDTO.getPhone());

        return personMapper.toPersonDTO(
                personRepository.save(person)
        );
    }

    @Transactional
    public PersonAccountDTO save(PersonCreateDTO personCreateDTO) {
        Account account = this.accountService.findById( personCreateDTO.getAccountId() );
        Assert.isNull(account.getPerson(), "Account with id: "+personCreateDTO.getAccountId()+ " already has person linked");
        Person person = this.personRepository.save(
                personMapper.toPerson(personCreateDTO)
        );
        // Sync account
        account.setPerson(person);
        this.accountService.save(account);

        return this.accountMapper.toPersonAccountDTO(account);
    }

    @Transactional
    public PersonAccountDTO save(PersonAccountCreateDTO personAccountCreateDTO) {
        Long accountId = this.accountService.save( personAccountCreateDTO.getAccount() ).getId();
        Person person = this.personRepository.save(
                personMapper.toPerson(personAccountCreateDTO)
        );
        // Sync account
        Account account = this.accountService.findById( accountId );
        account.setPerson(person);
        this.accountService.save(account);

        return this.accountMapper.toPersonAccountDTO(account);
    }


    /////// DELETE
    @Transactional
    public void deleteById(long id) {
        Account account = this.accountService.findByPersonId(id);
        this.personRepository.delete( findById(id) );
        // Sync account
        account.setPerson(null);
        this.accountService.save(account);
    }

    /////// PUT

    /////// UTILS

    private Person findById(Long id) {
        Optional<Person> optionalProduct = this.personRepository.findById(id);
        if(optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Person with id "+id+" was not found");
        }
        return optionalProduct.get();
    }

}