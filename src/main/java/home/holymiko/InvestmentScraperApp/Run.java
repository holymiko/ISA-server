package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.ExchangeRateService;
import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Utils.InvestmentInit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
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

    @Order(0)
    @EventListener(ApplicationStartedEvent.class)
    public void run() throws IOException {
        System.out.println("App has started up");
        try {
            cnbScraper.scrapExchangeRate();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        exchangeRateService.printExchangeRates();
    }

    // @Order(1) is in MetalScraper

    @Order(2) // Last Order index
    @EventListener(ApplicationStartedEvent.class)
    public void runFrontEnd() throws IOException {
        // Run FrontEnd NodeJS Application
        process = new ProcessBuilder(isWindows() ? "npm.cmd" : "yarn", "start")
                .directory( new File("../ISA-client"))
                .start();
    }

    // TODO @Order(3) if Ticker empty then import tickers from txt/export/tickers/#latest

    @EventListener(ApplicationReadyEvent.class)
    public void scrap() throws IOException {
//        scrapController.allLinks();
//        scrapController.allProductsInSync();
//        metalScraper.linksByDealerScrap(Dealer.SILVERUM);
//        metalScraper.generalScrapAndSleep(
//                linkService.findByDealer(Dealer.SILVERUM)
//        );
//        metalScraper.generalScrapAndSleep(
//                linkService.findByProductId(null)
//        );
//        scrapController.serenity();
    }



    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
