package home.holymiko.InvestmentScraperApp;

import home.holymiko.InvestmentScraperApp.Server.API.Controller.StockController;
import home.holymiko.InvestmentScraperApp.Server.Core.LogBuilder;
import home.holymiko.InvestmentScraperApp.Server.API.Controller.ScrapController;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ResourceNotFoundException;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.AurumBohemicaAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.AurumProAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.CeskaMincovnaAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.EkkaGoldAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.GoldASilverAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.BessergoldAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.BessergoldDeAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.GoldSafeAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.GoldenHouseAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.JednaUnceAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.SilverumAdapter;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.dealerAdapter.ZlatakyAdapter;
import home.holymiko.InvestmentScraperApp.Server.Service.RateService;
import org.slf4j.Logger;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.springframework.beans.factory.annotation.Autowired;
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
    private final StockController stockController;
    private final RateService rateService;
    private final CNBScraper cnbScraper;
    private final MetalScraper metalScraper;

    @Autowired
    public Run(ScrapController scrapController, StockController stockController, RateService rateService, CNBScraper cnbScraper, MetalScraper metalScraper) {
        this.scrapController = scrapController;
        this.stockController = stockController;
        this.rateService = rateService;
        this.cnbScraper = cnbScraper;
        this.metalScraper = metalScraper;
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
        metalScraper.addAdapter(Dealer.AURUM_BOHEMICA, new AurumBohemicaAdapter());
        metalScraper.addAdapter(Dealer.AURUM_PRO, new AurumProAdapter());
        metalScraper.addAdapter(Dealer.BESSERGOLD_CZ, new BessergoldAdapter());
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
        metalScraper.addAdapter(Dealer.CESKA_MINCOVNA, new CeskaMincovnaAdapter());
        metalScraper.addAdapter(Dealer.EKKA_GOLD, new EkkaGoldAdapter());
        metalScraper.addAdapter(Dealer.GOLD_A_SILVER, new GoldASilverAdapter());
        metalScraper.addAdapter(Dealer.GOLD_SAFE, new GoldSafeAdapter());
        metalScraper.addAdapter(Dealer.GOLDEN_HOUSE, new GoldenHouseAdapter());
        metalScraper.addAdapter(Dealer.JEDNA_UNCE, new JednaUnceAdapter());
        metalScraper.addAdapter(Dealer.SILVERUM, new SilverumAdapter());
        metalScraper.addAdapter(Dealer.ZLATAKY, new ZlatakyAdapter());
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

//    @Order(3) // Last Order index
//    @EventListener(ApplicationStartedEvent.class)
    public void runImportTickers() {
        stockController.importTickers();
    }

    /**
     * Runs every day at [9:00, 17:00]
     */
    @Scheduled(cron = "0 0 9,17 * * ?")
    public void scheduleScrap() {
        scrapController.productsInSync(null, null, null, null,false, false);
    }

    @Scheduled(cron = "0 0 13 * * ?")
    public void scheduleScrapHistory() {
        scrapController.allLinks();
        scrapController.productsInSync(null, null, null, null,true, null);
        scrapController.linksWithoutProduct(true);
    }

    static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("win");
    }
}
