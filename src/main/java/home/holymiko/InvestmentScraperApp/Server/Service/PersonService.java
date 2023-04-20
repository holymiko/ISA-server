package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.AccountRepository;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.PersonRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Mapper.AccountMapper;
import home.holymiko.InvestmentScraperApp.Server.Mapper.PersonMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PersonAccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.PersonAccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.PersonCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PersonDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Account;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Person;
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