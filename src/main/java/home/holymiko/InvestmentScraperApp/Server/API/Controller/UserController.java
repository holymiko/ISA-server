package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Type.DTO.create.UserCreateDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.UserDTO;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/user")
@AllArgsConstructor
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @GetMapping({"/me", "/me/"})
    public UserDTO getAssignedClient() {
        return new UserDTO("Tomas", "Dummy", "User", "tomas_dummy_user@world.com", 45665466);
    }

    @PostMapping
    public void createUser(@RequestBody UserCreateDTO userCreateDTO) {
        LOGGER.info("User save");
//        this.accountService.save(accountCreateDTO);
//        LOGGER.info("User save success");
    }

}
