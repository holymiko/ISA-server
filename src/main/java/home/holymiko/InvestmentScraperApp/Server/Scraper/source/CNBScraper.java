package home.holymiko.InvestmentScraperApp.Server.Scraper.source;

import com.gargoylesoftware.htmlunit.TextPage;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;
import home.holymiko.InvestmentScraperApp.Server.Service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
Class scraping data from Czech National Bank
 */
@Component
public class CNBScraper extends Client {
    private static final String SEARCH_URL = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";
    private static final Logger LOGGER = LoggerFactory.getLogger(CNBScraper.class);

    private final CurrencyService currencyService;

    @Autowired
    public CNBScraper(CurrencyService currencyService) {
        this.currencyService = currencyService;
    }

    public void scrapExchangeRate() throws ResourceNotFoundException {
        LOGGER.info("Scrap exchange rates");
        TextPage textPage = loadTextPage(SEARCH_URL);

        String[] rows = textPage.getContent().trim().split("\n");
        String[] date = rows[0].trim().split(" ")[0].trim().split("\\.");
        Date extractedDate = java.sql.Date.valueOf(
                date[2] + "-" + date[1] + "-" + date[0]
        );

        for (int i = 2; i < rows.length; i++) {
            String[] elements = rows[i].trim().split("\\|");
            this.currencyService.delete(elements[3], extractedDate);
            this.currencyService.save(
                    new ExchangeRate(
                            extractedDate,
                            elements[0],
                            elements[1],
                            Integer.parseInt(elements[2]),
                            elements[3],
                            Double.parseDouble(elements[4].replace(",", "."))
                    )
            );

        }


    }
}
