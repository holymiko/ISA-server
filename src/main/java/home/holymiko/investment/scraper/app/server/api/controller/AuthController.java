package home.holymiko.investment.scraper.app.server.api.controller;

import home.holymiko.investment.scraper.app.server.core.Handler;
import home.holymiko.investment.scraper.app.server.type.dto.simple.LoginReqDTO;
import home.holymiko.investment.scraper.app.server.type.dto.simple.LoginResDTO;
import home.holymiko.investment.scraper.app.server.type.entity.Account;
import home.holymiko.investment.scraper.app.server.utils.JwtUtil;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v2/auth")
@AllArgsConstructor
public class AuthController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    @ResponseBody
    @PostMapping(value = "/login")
    public ResponseEntity login(@RequestBody LoginReqDTO loginReq)  {
        Assert.notNull(loginReq, "Credentials were not given");
        Assert.notNull(loginReq.getUsername(), "Username is missing");
        Assert.notNull(loginReq.getPassword(), "Password is missing");

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginReq.getUsername(), loginReq.getPassword())
            );
            String name = authentication.getName();
            Account user = new Account(name, "", null);
            String token = jwtUtil.createToken(user);
            LoginResDTO loginRes = new LoginResDTO(name,token);

            return ResponseEntity.ok(loginRes);

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid username or password");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /////// Handlers

    @ExceptionHandler({IllegalArgumentException.class})
    public void handleRuntimeException(Exception ex, HttpServletResponse response) throws IOException {
        LOGGER.error("IllegalArgumentException: " + ex.getMessage());
        Handler.handleIllegalArgumentException(HttpStatus.UNAUTHORIZED, ex, response);
    }

}
