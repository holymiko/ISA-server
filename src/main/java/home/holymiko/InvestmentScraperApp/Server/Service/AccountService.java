package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.AccountRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Mapper.AccountMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Account;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

@Service
@AllArgsConstructor
public class AccountService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    public String authenticate(String username, String password) {
        findByUsernameAndPassword(username, password);
        return "myDummyToken";
    }

    /////// FIND AS DTO

    public AccountDTO findByIdAsDTO(long id) {
        return accountMapper.toAccountDTO( findById(id) );
    }

    /////// POST
    @Transactional
    public void save(AccountCreateDTO accountCreateDTO) {
        LOGGER.info("Account save");
        Assert.hasText(accountCreateDTO.getUsername(), "'Username' must not be empty");
        Assert.isTrue(accountCreateDTO.getUsername().length() >= 6, "'Username' must be at least 6 characters long");
        Assert.hasText(accountCreateDTO.getPassword(), "'Password' must not be empty");
        Assert.isTrue(accountCreateDTO.getPassword().matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"), "'Password' must contain at least one lower letter, one upper letter, one number and be at least 6 characters long");

        this.accountRepository.save(
                accountMapper.toAccount(accountCreateDTO)
        );
        LOGGER.info("Account saved");
    }


    /////// DELETE
    @Transactional
    public void deleteAccountById(long id) {
        LOGGER.info("Delete account");
        this.accountRepository.deleteById(id);
        LOGGER.info("Account deleted");
    }

    @Transactional
    public void deleteAccountByUsername(String username) {
        LOGGER.info("Delete account");
        this.accountRepository.deleteByUsername(username);
        LOGGER.info("Account deleted");
    }


    /////// PUT
    @Transactional
    public void changePasswordById(long id, String password) {
        LOGGER.info("Change password");
        Account account = findById(id);
        account.setPassword(password);
        this.accountRepository.save(account);
        LOGGER.info("Password changed");
    }

    @Transactional
    public void changePasswordByUsername(String username, String password) {
        LOGGER.info("Change password");
        Account account = this.accountRepository.findByUsername(username).orElseThrow(
                () -> new ResourceNotFoundException("Account with given username was not found")
        );
        account.setPassword(password);
        this.accountRepository.save(account);
        LOGGER.info("Password changed");
    }

    /////// UTILS

    private Account findByUsernameAndPassword(String username, String password) {
        Optional<Account> optional = accountRepository.findByUsernameAndPassword(username, password);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Account with given credentials was not found");
        }
        return optional.get();
    }

    private Account findById(Long id) {
        Optional<Account> optionalProduct = this.accountRepository.findById(id);
        if(optionalProduct.isEmpty()) {
            throw new ResourceNotFoundException("Account with id "+id+" was not found");
        }
        return optionalProduct.get();
    }
}