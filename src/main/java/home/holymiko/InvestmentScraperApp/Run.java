package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.API.TextPort.Import;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.BessergoldAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.BessergoldDeAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.SilverumAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.ZlatakyAdapter;
import home.holymiko.InvestmentScraperApp.Server.Service.CurrencyService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
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
    private final TickerService tickerService;
    private final CurrencyService currencyService;
    private final CNBScraper cnbScraper;
    private final MetalScraper metalScraper;
    private final Import anImport;

    @Autowired
    public Run(ScrapController scrapController, TickerService tickerService, CurrencyService currencyService, CNBScraper cnbScraper, MetalScraper metalScraper, Import anImport) {
        this.scrapController = scrapController;
        this.tickerService = tickerService;
        this.currencyService = currencyService;
        this.cnbScraper = cnbScraper;
        this.metalScraper = metalScraper;
        this.anImport = anImport;
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

    /**
     * Initialize instances dealer interfaces which are locally used in MetalScraper.java
     * Instances can be initialized in constructor, because exchange rate has to be scraped and injected at first
     * (for foreign website scrapers)
     * Exchange rates are scraped in Run.java by EventListener with @Order(0)
     */
    @Order(1)
    @EventListener(ApplicationStartedEvent.class)
    public void initializeAdapterMap() {
        metalScraper.addAdapter(Dealer.BESSERGOLD_CZ, new BessergoldAdapter());
        metalScraper.addAdapter(Dealer.SILVERUM, new SilverumAdapter());
        metalScraper.addAdapter(Dealer.ZLATAKY, new ZlatakyAdapter());
        metalScraper.addAdapter(
                Dealer.BESSERGOLD_DE,
                new BessergoldDeAdapter(
                        // Insert currency exchange rate for conversion to CZK
                        currencyService.findExchangeRate("EUR").getExchangeRate()
                )
        );
    }

    @Order(2)
    @EventListener(ApplicationStartedEvent.class)
    public void runFrontEnd() throws IOException {
        // Run FrontEnd NodeJS Application
        process = new ProcessBuilder(isWindows() ? "npm.cmd" : "yarn", "start")
                .directory( new File("../ISA-client"))
                .start();
    }

    @Order(3) // Last Order index
    @EventListener(ApplicationStartedEvent.class)
    public void runImportTickers() throws IOException {
        if(tickerService.findAll().isEmpty()) {
            System.out.println("3) Import Tickers");
            anImport.importExportedTickers();
            System.out.println("3) Import finished");
        } else {
            // TODO Logging
            System.out.println("3) SKIP");
        }
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
