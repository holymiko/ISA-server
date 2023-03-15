package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Service.AccountService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.AccountDTO;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/account")
public class AccountController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccountController.class);

    private final AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) { this.accountService = accountService; }

    /////// GET

    @ResourceNotFound
    @GetMapping({"/id/{id}", "/id/"})
    public Optional<AccountDTO> byId(@PathVariable(required = false) Long id) {
        LOGGER.info("Get account by Id");
        Assert.notNull(id, "Id cannot be null");
        return accountService.findByIdAsDTO(id);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.getOutputStream().write(ExceptionUtils.getMessage(ex).getBytes());
        write(ex, response, ex.getClass().getName());
    }

    protected void write(Exception ex, HttpServletResponse response, String msg) throws IOException {
        LOGGER.error("Exception: ", ex);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    }
}
