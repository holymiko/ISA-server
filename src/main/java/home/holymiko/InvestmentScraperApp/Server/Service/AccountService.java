package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.AccountRepository;
import home.holymiko.InvestmentScraperApp.Server.Mapper.AccountMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Account;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.util.Assert;

@Service
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Autowired
    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    /////// FIND AS DTO

    public Optional<AccountDTO> findByIdAsDTO(long id) {
        Optional<Account> optionalAccountDTO = this.accountRepository.findById(id);
        return optionalAccountDTO.map(
                accountMapper::toAccountDTO
        );
    }

    /////// POST

    public void save(AccountCreateDTO accountCreateDTO) {
        Assert.hasText(accountCreateDTO.getUsername(), "'Username' must not be empty");
        Assert.isTrue(accountCreateDTO.getUsername().length() >= 6, "'Username' must be at least 6 characters long");
        Assert.hasText(accountCreateDTO.getPassword(), "'Password' must not be empty");
        Assert.isTrue(accountCreateDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"), "'Password' must contain at least one lower letter, one upper letter, one number and be at least 6 characters long");
        this.accountRepository.save(new Account(accountCreateDTO.getUsername(), accountCreateDTO.getPassword(), Role.USER));
        LOGGER.info("Save account");
    }

    /////// DELETE

    public void deleteAccountById(long id) {
        this.accountRepository.deleteById(id);
        LOGGER.info("Delete account");
    }

    public void deleteAccountByUsername(String username) {
        this.accountRepository.deleteByUsername(username);
        LOGGER.info("Delete account");
    }

    /////// PUT

    public void changePasswordById(long id, String password) {
        this.accountRepository.changePasswordById(id, password);
        LOGGER.info("Change password");
    }

    public void changePasswordByUsername(String username, String password) {
        this.accountRepository.changePasswordByUsername(username, password);
    }
}