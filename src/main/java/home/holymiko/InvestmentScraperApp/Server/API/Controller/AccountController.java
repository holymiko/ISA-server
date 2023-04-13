package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.AccountService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    public AccountDTO byId(@PathVariable Long id) {
        LOGGER.info("Get account by Id");
        Assert.notNull(id, "Id cannot be null");
        return accountService.findByIdAsDTO(id);
    }




    /////// POST
    @PostMapping
    public void createAccount(@RequestBody AccountCreateDTO accountCreateDTO) {
        LOGGER.info("Create account");
        this.accountService.save(accountCreateDTO);
    }


    /////// DELETE
    @DeleteMapping({"/id/{id}", "/id/"})
    public void deleteAccountById(@PathVariable long id) {
        LOGGER.info("Delete account by Id");
        this.accountService.deleteAccountById(id);
    }

    @DeleteMapping({"/username/{username}", "/username/"})
    public void deleteAccountByUsername(@PathVariable String username) {
        LOGGER.info("Delete account by Username");
        this.accountService.deleteAccountByUsername(username);
    }


    /////// PUT
    @PutMapping({"/id/{id}", "/id/"})
    public void changePasswordById(@PathVariable long id,@RequestBody String password) {
        LOGGER.info("Change password by Id");
        this.accountService.changePasswordById(id, password);
    }

    @PutMapping({"/username/{username}", "/username/"})
    public void changePasswordByUsername(@PathVariable String username, @RequestBody String password) {
        LOGGER.info("Change password by Id");
        System.out.println(username + password);
        this.accountService.changePasswordByUsername(username, password);
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("Exception: ", ex);
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getOutputStream().write(ExceptionUtils.getMessage(ex).getBytes());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
