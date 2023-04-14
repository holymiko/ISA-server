package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Handler;
import home.holymiko.InvestmentScraperApp.Server.Service.AccountService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.CredentialDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/auth")
@AllArgsConstructor
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final AccountService accountService;

    // TODO Rebuild this, credentials should be hashed
    @PostMapping
    public String authenticate(@RequestBody CredentialDTO authDTO) {
        Assert.notNull(authDTO, "Credentials were not given");
        Assert.notNull(authDTO.getUsername(), "Username is missing");
        Assert.notNull(authDTO.getPassword(), "Password is missing");
        return accountService.authenticate(authDTO.getUsername(), authDTO.getPassword());
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("Exception: ", ex);
        Handler.handleIllegalArgumentException(HttpStatus.UNAUTHORIZED, ex, response);
    }

}
