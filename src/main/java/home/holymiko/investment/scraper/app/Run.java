package home.holymiko.investment.scraper.app;

import home.holymiko.investment.scraper.app.server.api.controller.StockController;
import home.holymiko.investment.scraper.app.server.core.LogBuilder;
import home.holymiko.investment.scraper.app.server.api.controller.ScrapController;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.scraper.source.CNBScraper;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.MetalScraper;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.AurumBohemicaAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.AurumProAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.CeskaMincovnaAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.EkkaGoldAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.GoldASilverAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.BessergoldAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.BessergoldDeAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.GoldSafeAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.GoldenHouseAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.JednaUnceAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.SilverumAdapter;
import home.holymiko.investment.scraper.app.server.scraper.source.metal.dealerAdapter.ZlatakyAdapter;
import home.holymiko.investment.scraper.app.server.service.RateService;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;

import org.slf4j.LoggerFactory;

/**
 * Class used to call methods during the building process
 */
@Component
public class Run {

    private static final Logger LOGGER = LoggerFactory.getLogger(Run.class);

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

    @Order(2)
    @EventListener(ApplicationStartedEvent.class)
    public void initScrapData() {
        scrapController.allLinks();
        scrapController.linksWithoutProduct(true);
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
        scrapController.productsInSync(false, null, null, null, null, false, null);
    }

    @Scheduled(cron = "0 0 13 * * ?")
    public void scheduleScrapHistory() {
        scrapController.allLinks();
        scrapController.productsInSync(true, null, null, null, null, null, null);
        scrapController.linksWithoutProduct(true);
    }

}
