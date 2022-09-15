package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.ExchangeRateService;
import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Utils.InvestmentInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

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
    private final LinkService linkService;
    private final MetalScraper metalScraper;

    @Autowired
    public Run(ScrapController scrapController, InvestmentInit investmentInit, TickerService tickerService, ExchangeRateService exchangeRateService, CNBScraper cnbScraper, LinkService linkService, MetalScraper metalScraper) {
        this.scrapController = scrapController;
        this.investmentInit = investmentInit;
        this.tickerService = tickerService;
        this.exchangeRateService = exchangeRateService;
        this.cnbScraper = cnbScraper;
        this.linkService = linkService;
        this.metalScraper = metalScraper;
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
//        metalScraper.linksByDealerScrap(Dealer.SILVERUM);
//        metalScraper.generalScrapAndSleep(
//                linkService.findByDealer(Dealer.SILVERUM)
//        );

        cnbScraper.scrapExchangeRate();
        scrapController.allLinks();
        scrapController.allProducts();

//        exchangeRateService.printExchangeRates();
//        scrapController.serenity();
    }



    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
