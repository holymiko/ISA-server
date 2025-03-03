package home.holymiko.investment.scraper.app.server.api.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import home.holymiko.investment.scraper.app.server.core.Handler;
import home.holymiko.investment.scraper.app.server.service.AccountService;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.PersonAccountDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.AccountCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.AccountDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.LoginReqDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Role;
import home.holymiko.investment.scraper.app.server.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/v2/account")
@AllArgsConstructor
public class AccountController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final JwtUtil jwtUtil;
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

    @GetMapping("/me")
    public PersonAccountDTO me(HttpServletRequest request) throws ServletException, IOException {
        Claims claims = jwtUtil.resolveClaims(request);

        if(claims != null & jwtUtil.validateClaims(claims)){
            String username = claims.getSubject();
            LOGGER.info("me: "+username);
            return accountService.findByUserNameAsPersonAccountDto(username);
        }
        return null;
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
    public void changePasswordByUsername(@RequestBody LoginReqDTO credentials) {
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
