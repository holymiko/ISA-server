package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import home.holymiko.InvestmentScraperApp.Server.Core.Handler;
import home.holymiko.InvestmentScraperApp.Server.Service.AccountService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.AccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.CredentialDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Role;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

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

    @GetMapping
    public List<AccountDTO> all() {
        LOGGER.info("Get all accounts");
        return accountService.findAll();
    }

    @GetMapping("/{id}")
    public AccountDTO byId(@PathVariable Long id) {
        LOGGER.info("Get account by id");
        Assert.notNull(id, "ID cannot be null");
        return accountService.findByIdAsDTO(id);
    }


    /////// POST
    @PostMapping
    public AccountDTO createAccount(@RequestBody AccountCreateDTO accountCreateDTO) {
        LOGGER.info("Account save");
        return this.accountService.save(accountCreateDTO);
    }

    /////// PUT
    @PutMapping(path = "/role/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Accepts 'role' param, ignores others")
    public void changeRoleById(@PathVariable Long id, @RequestBody @JsonProperty("role") Map<String, Role> roleMap) {
        LOGGER.info("Change role by ID");
        Role role = roleMap.get("role");
        Assert.notNull(id, "ID was not given");
        Assert.notNull(role, "Role was not given");
        this.accountService.changRoleById(id, role);
        LOGGER.info("Role was successfully changed");
    }

    @PutMapping(path = "/password/{id}")
    @Operation(description = "Accepts 'password' param, ignores others")
    public void changePasswordById(@PathVariable Long id, @RequestBody @JsonProperty("password") Map<String, String> passwordMap) {
        LOGGER.info("Change password by id");
        String password = passwordMap.get("password");
        Assert.notNull(id, "ID was not given");
        this.accountService.changePasswordById(id, password);
        LOGGER.info("Password was successfully changed");
    }

    @PutMapping(path = "/password")
    public void changePasswordByUsername(@RequestBody CredentialDTO credentials) {
        LOGGER.info("Change password by username");
        this.accountService.changePasswordByUsername(credentials.getUsername(), credentials.getPassword());
        LOGGER.info("Password change success");
    }

    /////// DELETE
    @DeleteMapping("/{id}")
    @Operation(description = "Deletes account and linked person based on account ID")
    public void deleteAccountById(@PathVariable Long id) {
        LOGGER.info("Delete account by Id");
        Assert.notNull(id, "ID was not given");
        this.accountService.deleteById(id);
        LOGGER.info("Account delete success");
    }

    @DeleteMapping
    @Operation(description = "Deletes account and linked person based on account username")
    public void deleteAccountByUsername(@RequestParam String username) {
        LOGGER.info("Delete account by username");
        this.accountService.deleteByUsername(username);
        LOGGER.info("Account delete success");
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException: " + ex.getMessage());
        Handler.handleIllegalArgumentException(HttpStatus.BAD_REQUEST, ex, response);
    }
}
