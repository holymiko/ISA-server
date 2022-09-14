package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.ExchangeRateService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Utils.InvestmentInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * Class used to call methods during the building process
 */
@Component
public class Run {

    Process process;
    private final ScrapController scrapController;
    private final InvestmentInit investmentInit;
    private final TickerService tickerService;
    private final ExchangeRateService exchangeRateService;
    private final CNBScraper cnbScraper;

    @Autowired
    public Run(ScrapController scrapController, InvestmentInit investmentInit, TickerService tickerService, ExchangeRateService exchangeRateService, CNBScraper cnbScraper) {
        this.scrapController = scrapController;
        this.investmentInit = investmentInit;
        this.tickerService = tickerService;
        this.exchangeRateService = exchangeRateService;
        this.cnbScraper = cnbScraper;
    }


    @EventListener(ApplicationStartedEvent.class)
    public void run() throws IOException {
        System.out.println("App has started up");

        // Run FrontEnd NodeJS Application
//        process = new ProcessBuilder(isWindows() ? "npm.cmd" : "npm", "start")
//                .directory( new File("../InvestmentScraperApp_client"))
//                .start();

        exchangeRateService.printExchangeRates();
//        tickerService.printTickerStatus();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scrap() throws IOException {
//        scrapController.allLinks();
//        scrapController.allProducts();
//        scrapController.scrapProductsByParam(null, Dealer.ZLATAKY, null, null);
//        scrapController.scrapEverything();
//        scrapController.serenity();
//        investmentInit.saveInitPortfolios();
    }



    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
