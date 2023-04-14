package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Handler;
import home.holymiko.InvestmentScraperApp.Server.Service.AccountService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/account")
@AllArgsConstructor
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    /////// GET

    @GetMapping({"/id/{id}", "/id/"})
    public AccountDTO byId(@PathVariable(required = false) Long id) {
        LOGGER.info("Get account by id");
        Assert.notNull(id, "ID cannot be null");
        return accountService.findByIdAsDTO(id);
    }

    @GetMapping()
    public List<AccountDTO> all() {
        LOGGER.info("Get all accounts");
        return accountService.findAll();
    }


    /////// POST
    @PostMapping
    public void createAccount(@RequestBody AccountCreateDTO accountCreateDTO) {
        LOGGER.info("Account save");
        this.accountService.save(accountCreateDTO);
        LOGGER.info("Account save success");
    }

    /////// PUT
    @PutMapping({"/id/{id}", "/id/"})
    public void changePasswordById(@PathVariable(required = false) Long id, @RequestBody String password) {
        LOGGER.info("Change password by id");
        Assert.notNull(id, "ID was not given");
        this.accountService.changePasswordById(id, password);
        LOGGER.info("Password was successfully changed");
    }

    @PutMapping({"/username/{username}", "/username/"})
    public void changePasswordByUsername(@PathVariable String username, @RequestBody String password) {
        LOGGER.info("Change password by username");
        this.accountService.changePasswordByUsername(username, password);
        LOGGER.info("Password change success");
    }

    /////// DELETE
    @DeleteMapping({"/id/{id}", "/id/"})
    public void deleteAccountById(@PathVariable(required = false) Long id) {
        LOGGER.info("Delete account by Id");
        Assert.notNull(id, "ID was not given");
        this.accountService.deleteAccountById(id);
        LOGGER.info("Account delete success");
    }

    @DeleteMapping({"/username/{username}", "/username/"})
    public void deleteAccountByUsername(@PathVariable String username) {
        LOGGER.info("Delete account by username");
        this.accountService.deleteAccountByUsername(username);
        LOGGER.info("Account delete success");
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("Exception: ", ex);
        Handler.handleIllegalArgumentException(HttpStatus.BAD_REQUEST, ex, response);
    }
}
