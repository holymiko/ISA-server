package home.holymiko.investment.scraper.app.server.api.controller;

import home.holymiko.investment.scraper.app.server.core.Handler;
import home.holymiko.investment.scraper.app.server.service.PersonService;
import home.holymiko.investment.scraper.app.server.type.dto.advanced.PersonAccountDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.PersonAccountCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.create.PersonCreateDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v2/person")
@AllArgsConstructor
public class PersonController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;


    @GetMapping
    public List<PersonAccountDTO> all() {
        LOGGER.info("Get all accounts");
        return this.personService.findAll();
    }

    @GetMapping("/{id}")
    public PersonAccountDTO byId(@PathVariable Long id) {
        LOGGER.info("Get all accounts");
        return this.personService.findByIdAsDTO( id );
    }

    @PostMapping
    @Operation(description = "First checks that account with given ID doesn't have linked person. Then creates person and links it to account.")
    public PersonAccountDTO createPerson(@RequestBody PersonCreateDTO personCreateDTO) {
        LOGGER.info("Person save");
        Assert.notNull(personCreateDTO, "personCreateDTO must not be null");
        return this.personService.save(personCreateDTO);
    }

    @PutMapping("/{id}")
    @Operation(description = "Update person")
    public PersonDTO updatePerson(@PathVariable Long id, @RequestBody PersonDTO personDTO) {
        LOGGER.info("Person update");
        Assert.notNull(id, "ID cannot be null");
        Assert.notNull(personDTO, "personDTO must not be null");
        return this.personService.update(id, personDTO);
    }

    @PostMapping("/account")
    @Operation(description = "Creates first account and then person. Entities are linked together.")
    public PersonAccountDTO createPersonWithAccount(@RequestBody PersonAccountCreateDTO personAccountCreateDTO) {
        LOGGER.info("Person save");
        Assert.notNull(personAccountCreateDTO, "personAccountCreateDTO must not be null");
        Assert.notNull(personAccountCreateDTO.getAccount(), "account must not be null");
        return this.personService.save(personAccountCreateDTO);
    }

    @DeleteMapping("/{id}")
    @Operation(description = "Deletes person and removes personId from linked account")
    public void deletePersonById(@PathVariable Long id) {
        LOGGER.info("Delete person by ID");
        Assert.notNull(id, "ID was not given");
        this.personService.deleteById(id);
        LOGGER.info("Person delete success");
    }

    /////// Handlers
    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException: " + ex.getMessage());
        Handler.handleIllegalArgumentException(HttpStatus.BAD_REQUEST, ex, response);
    }

}
