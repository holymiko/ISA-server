package home.holymiko.investment.scraper.app.server.service;


import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


@Service
@AllArgsConstructor
public class AppPropsService {

    public static final String APP_CONFIG_PATH = "target/classes/application.properties";


    // Loads application.properties from Docker container to runtime
    private final Properties appProps;

    public AppPropsService() throws IOException {
        appProps = new Properties();
        appProps.load(new FileInputStream(APP_CONFIG_PATH));
    }

    /**
     * @param key Accepts key from application.properties
     */
    public String getAppProperty(String key) {
        return appProps.getProperty(key);
    }
}