package home.holymiko.InvestmentScraperApp.Server.Scraper.sources;

import com.gargoylesoftware.htmlunit.TextPage;
import home.holymiko.InvestmentScraperApp.Server.Entity.ExchangeRate;
import home.holymiko.InvestmentScraperApp.Server.Scraper.Scraper;
import home.holymiko.InvestmentScraperApp.Server.Service.ExchangeRateService;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ExchangeRateScraper extends Scraper {

    private static final String SEARCH_URL = "https://www.cnb.cz/cs/financni-trhy/devizovy-trh/kurzy-devizoveho-trhu/kurzy-devizoveho-trhu/denni_kurz.txt";

    private final ExchangeRateService exchangeRateService;

    @Autowired
    public ExchangeRateScraper(ExchangeRateService exchangeRateService) {
        this.exchangeRateService = exchangeRateService;

        try {
            scrap();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    public void scrap() throws NotFoundException {
        TextPage textPage = null;
        try {
            textPage = client.getPage(SEARCH_URL);                // Product textPage
        } catch (Exception e)  {
//            e.printStackTrace();
        }
        if(textPage == null) {
            throw new NotFoundException("Page not found");
        }


        String[] rows = textPage.getContent().trim().split("\n");

        for (int i = 2; i < rows.length; i++) {
            System.out.println(rows[i]);
            String[] elements = rows[i].trim().split("\\|");

            this.exchangeRateService.save(
                    new ExchangeRate(
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
