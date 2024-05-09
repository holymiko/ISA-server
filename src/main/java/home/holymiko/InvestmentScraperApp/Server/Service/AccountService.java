package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.AccountRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Mapper.AccountMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PersonAccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Account;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class AccountService implements UserDetailsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountService.class);
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;


    /////// FIND AS DTO

    public List<AccountDTO> findAll() {
        return accountRepository.findAll()
                .stream()
                .map(accountMapper::toAccountDTO)
                .collect(Collectors.toList());
    }

    public PersonAccountDTO findByUserNameAsPersonAccountDto(String username) throws UsernameNotFoundException {
        return accountMapper.toPersonAccountDTO(
                findByUsername(username)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account user = findByUsername(username);
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public AccountDTO findByIdAsDTO(long id) {
        return accountMapper.toAccountDTO( findById(id) );
    }

    /////// POST
    @Transactional
    public AccountDTO save(AccountCreateDTO accountCreateDTO) {
        // Unique username
        if( this.accountRepository.findByUsername(accountCreateDTO.getUsername()).isPresent() ) {
            throw new IllegalArgumentException("Account with username '"+accountCreateDTO.getUsername()+"' already exists");
        }
        assertUsername(accountCreateDTO.getUsername());
        assertPassword(accountCreateDTO.getPassword());
        // Default role
        if(accountCreateDTO.getRole() == null) {
            accountCreateDTO.setRole(Role.USER);
            LOGGER.info("Role set to default: " + accountCreateDTO.getRole());
        }
        return accountMapper.toAccountDTO(
            this.accountRepository.save(
                    accountMapper.toAccount(accountCreateDTO)
            )
        );
    }

    @Transactional
    public AccountDTO save(Account account) {
        return accountMapper.toAccountDTO(
                this.accountRepository.save(account)
        );
    }


    /////// DELETE
    @Transactional
    public void deleteById(long id) {
        this.accountRepository.delete( findById(id) );
    }

    @Transactional
    public void deleteByUsername(String username) {
        this.accountRepository.delete( findByUsername(username) );
    }


    /////// PUT
    @Transactional
    public void changePassword(Account account, String password) {
        assertPassword(password);
        account.setPassword(password);
        this.accountRepository.save(account);
    }

    public void changRoleById(long id, Role role) {
        Account account = findById(id);
        account.setRole(role);
        accountRepository.save(account);
    }
    public void changePasswordById(long id, String password) {
        changePassword( findById(id), password );
    }

    public void changePasswordByUsername(String username, String password) {
        changePassword( findByUsername(username), password );
    }

    /////// UTILS

    private Account findByUsernameAndPassword(String username, String password) {
        Optional<Account> optional = accountRepository.findByUsernameAndPassword(username, password);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Account with given credentials was not found");
        }
        return optional.get();
    }
    // TODO Create param endpoint
    private Account findByUsername(String username) {
        Optional<Account> optional = this.accountRepository.findByUsername(username);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Account with username "+username+" was not found");
        }
        return optional.get();
    }

    public Account findById(Long id) {
        Optional<Account> optional = this.accountRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Account with id "+id+" was not found");
        }
        return optional.get();
    }

    public Account findByPersonId(Long id) {
        Optional<Account> optional = this.accountRepository.findByPersonId(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("Account with personId "+id+" was not found");
        }
        return optional.get();
    }

    private static void assertUsername(String username) {
        Assert.hasText(username, "'Username' must not be empty");
        Assert.isTrue(username.length() >= 6, "'Username' must be at least 6 characters long");
    }

    private static void assertPassword(String password) {
        Assert.hasText(password, "'Password' must not be empty");
        Assert.isTrue(password.matches("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z]).{6,}$"), "'Password' must contain at least one lower letter, one upper letter, one number and be at least 6 characters long");
    }
}