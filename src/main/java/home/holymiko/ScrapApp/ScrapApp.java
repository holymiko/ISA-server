package home.holymiko.ScrapApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.util.Collections;

@SpringBootApplication
public class ScrapApp {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(ScrapApp.class);
        app.setDefaultProperties(Collections.singletonMap("server.port", "8080"));
        app.run();
    }
}
