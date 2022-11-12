package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.CurrencyService;
import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Class used to call methods during the building process
 */
@Component
public class Run {

    Process process;
    private final ScrapController scrapController;
    private final CurrencyService currencyService;
    private final CNBScraper cnbScraper;
    private final LinkService linkService;
    private final MetalScraper metalScraper;

    @Autowired
    public Run(ScrapController scrapController, CurrencyService currencyService, CNBScraper cnbScraper, LinkService linkService, MetalScraper metalScraper) {
        this.scrapController = scrapController;
        this.currencyService = currencyService;
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
        ConsolePrinter.printExchangeRates(
                Arrays.asList(
                        currencyService.findExchangeRate("EUR"),
                        currencyService.findExchangeRate("USD")
                )
        );
    }

    // @Order(1) is in MetalScraper

    @Order(2)
    @EventListener(ApplicationStartedEvent.class)
    public void runFrontEnd() throws IOException {
        // Run FrontEnd NodeJS Application
        process = new ProcessBuilder(isWindows() ? "npm.cmd" : "yarn", "start")
                .directory( new File("../ISA-client"))
                .start();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scrap() throws IOException {
        scrapController.allLinks();
        scrapController.allProductsInSync();
//        scrapController.serenity();
    }

    @EventListener(ApplicationReadyEvent.class)
    public void scrapDev() throws IOException {
//        metalScraper.linksByDealerScrap(Dealer.SILVERUM);
//        metalScraper.generalScrapAndSleep(
//                linkService.findByDealer(Dealer.SILVERUM)
//        );
//        metalScraper.generalScrapAndSleep(
//                linkService.findByProductId(null)
//        );
    }


    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
