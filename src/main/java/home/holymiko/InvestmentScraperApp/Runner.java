package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class used to call methods during the building process
 */
@Component
public class Runner {

    private final TickerService tickerService;
    private final CNBScraper cnbScraper;

    @Autowired
    public Runner(TickerService tickerService, CNBScraper cnbScraper) {
        this.tickerService = tickerService;
        this.cnbScraper = cnbScraper;
        run();
    }

    private void run() {
        try {
            cnbScraper.scrapExchangeRate();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        tickerService.printTickerStatus();
    }
}
