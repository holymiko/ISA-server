package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Utils.InvestmentInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class used to call methods during the building process
 */
@Component
public class Runner {

    private final InvestmentInit investmentInit;
    private final TickerService tickerService;
    private final CNBScraper cnbScraper;

    @Autowired
    public Runner(InvestmentInit investmentInit, TickerService tickerService, CNBScraper cnbScraper) {
        this.investmentInit = investmentInit;
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
//        investmentInit.saveInitPortfolios();
    }
}
