package home.holymiko.InvestmentScraperApp.Server.Core.config;

import org.springframework.boot.convert.ApplicationConversionService;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableScheduling // Enables @Schedule
public class SpringConfig implements WebMvcConfigurer {

    /**
     * Enum mapping config. Used for case insensitivity on Controller
     */
    @Override
    public void addFormatters(FormatterRegistry registry) {
        ApplicationConversionService.configure(registry);
    }
}
