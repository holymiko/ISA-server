package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.Handler;
import home.holymiko.InvestmentScraperApp.Server.Service.PersonService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PersonAccountDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.PersonAccountCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.PersonCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PersonDTO;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/person")
@AllArgsConstructor
public class PersonController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    private final PersonService personService;

    @GetMapping({"/me", "/me/"})
    public PersonDTO getAssignedClient() {
        return new PersonDTO(Long.parseLong("450"), "Tomas", "Dummy", "Person", "tomas_dummy_user@world.com", 45665466L);
    }

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
