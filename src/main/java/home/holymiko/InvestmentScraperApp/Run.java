package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.Core.LogBuilder;
import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.API.FilePort.Import;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Service.RateService;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.BessergoldAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.BessergoldDeAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.SilverumAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.metalAdapter.ZlatakyAdapter;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import org.slf4j.Logger;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

/**
 * Class used to call methods during the building process
 */
@Component
public class Run {

    private static final Logger LOGGER = LoggerFactory.getLogger(Run.class);

    Process process;
    private final ScrapController scrapController;
    private final TickerService tickerService;
    private final RateService rateService;
    private final CNBScraper cnbScraper;
    private final MetalScraper metalScraper;
    private final Import anImport;

    @Autowired
    public Run(ScrapController scrapController, TickerService tickerService, RateService rateService, CNBScraper cnbScraper, MetalScraper metalScraper, Import anImport) {
        this.scrapController = scrapController;
        this.tickerService = tickerService;
        this.rateService = rateService;
        this.cnbScraper = cnbScraper;
        this.metalScraper = metalScraper;
        this.anImport = anImport;
    }

    @Order(0)
    @EventListener(ApplicationStartedEvent.class)
    public void run() throws IOException {
        LOGGER.info("App has started UP");
        try {
            cnbScraper.scrapExchangeRate();
        } catch (ResourceNotFoundException e) {
            e.printStackTrace();
        }
        LogBuilder.printExchangeRates(
                Arrays.asList(
                        rateService.findExchangeRate("EUR"),
                        rateService.findExchangeRate("USD")
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
        try {
            metalScraper.addAdapter(
                    Dealer.BESSERGOLD_DE,
                    new BessergoldDeAdapter(
                            // Insert currency exchange rate for conversion to CZK
                            rateService.findExchangeRate("EUR").getExchangeRate()
                    )
            );
        } catch (NullPointerException e) {
            LOGGER.warn("WebClient OFF - BessergoldDeAdapter - EUR exchange rate is missing");
        }
    }

//    @Order(2)                       //  TODO activate before release
//    @EventListener(ApplicationStartedEvent.class)
    public void runFrontEnd() throws IOException {
        // Run FrontEnd NodeJS Application
        LOGGER.info("Attempt START frontend");
        process = new ProcessBuilder(isWindows() ? "npm.cmd" : "yarn", "start")
                .directory( new File("../ISA-client"))
                .start();
    }

    @Order(3) // Last Order index
    @EventListener(ApplicationStartedEvent.class)
    public void runImportTickers() throws IOException {
        if(tickerService.findAll().isEmpty()) {
            LOGGER.info("3) Import Tickers");
            anImport.importExportedTickers();
            LOGGER.info("3) Import finished");
        } else {
            LOGGER.info("3) SKIP");
        }
    }

//    @EventListener(ApplicationReadyEvent.class)
    public void scrap() throws IOException {
        scrapController.allLinks();
        scrapController.allProductsInSync();
//        scrapController.serenity();
    }

    /**
     * Runs every day at 9:30 and 17:30
     */
    @Scheduled(cron = "0 30 9,17 * * ?")
    public void scheduleScrap() {
        scrapController.allProductsInSync();
    }

    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
