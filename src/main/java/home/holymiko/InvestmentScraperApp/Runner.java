package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.ExchangeRateService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Utils.InvestmentInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class used to call methods during the building process
 */
@Component
public class Runner {

    private final ScrapController scrapController;
    private final InvestmentInit investmentInit;
    private final TickerService tickerService;
    private final ExchangeRateService exchangeRateService;
    private final CNBScraper cnbScraper;

    @Autowired
    public Runner(ScrapController scrapController, InvestmentInit investmentInit, TickerService tickerService, ExchangeRateService exchangeRateService, CNBScraper cnbScraper) {
        this.scrapController = scrapController;
        this.investmentInit = investmentInit;
        this.tickerService = tickerService;
        this.exchangeRateService = exchangeRateService;
        this.cnbScraper = cnbScraper;
        run();
    }

    private void run() {
//        try {
//            cnbScraper.scrapExchangeRate();
//        } catch (ResourceNotFoundException e) {
//            e.printStackTrace();
//        }
        tickerService.printTickerStatus();
        exchangeRateService.printExchangeRates();
//        scrapController.allLinks();
//        scrapController.allProducts();
//        scrapController.scrapEverything();
//        scrapController.serenity();
//        investmentInit.saveInitPortfolios();
    }
}
