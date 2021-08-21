package home.holymiko.InvestmentScraperApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

@SpringBootApplication
public class InvestmentScraperApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(InvestmentScraperApp.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));
        app.run();
    }
}
