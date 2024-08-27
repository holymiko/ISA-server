package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.LogBuilder;
import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapRefusedException;
import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.metal.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.source.SerenityScraper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v2/scrap")
@AllArgsConstructor
public class ScrapController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(ScrapController.class);

    private final MetalScraper metalScraper;
    private final SerenityScraper serenityScraper;
    private final ScrapHistory scrapHistory;
    private final LinkService linkService;

    // TODO Endpoint for cnbScraper
    // TODO Endpoint for scrap stock by ticker
    // TODO method byPortfolio should include stock scraping

    @PostMapping("/all")
    public void scrapEverything() {
        allLinks();
        productsInSync(null, null,null,null, true, null);
        linksWithoutProduct(true);
        serenity();
    }

    //////// Products

    /**
     * Scrap products for ALL Links. Including Links which doesn't have Product yet.
     * Scraping of Prices is grouped by Product. Prices from different Dealers, are for each Product,
     * scraped at the same time. Thanks to that, Prices are time synchronized.
     */
    @PostMapping("/products")
    public void productsInSync(Dealer dealer, Producer producer, Metal metal, Form form, boolean saveHistory, Boolean hidden) {
        try {
            ScrapHistory.frequencyHandlingAll(false);
            ScrapHistory.startRunning();
        } catch (ScrapRefusedException e) {
            return;
        }

        scrapLinksGroupedByProduct(dealer, producer, metal, form, saveHistory, hidden);

        ScrapHistory.timeUpdate(false, true);
        ScrapHistory.stopRunning();
    }

    public void scrapLinksGroupedByProduct(Dealer dealer, Producer producer, Metal metal, Form form, boolean saveHistory, Boolean hidden) {
        LOGGER.info("START SYNC SCRAP - Links grouped by Product {} {} {}", dealer, metal, form);
        // Scrap Links by Product, in sync, grouped by Product
        metalScraper.generalInSyncScrapAndSleep(
                linkService.findLinksGroupedByProduct(dealer, producer, metal, form, hidden),
                saveHistory
        );
        LOGGER.info("END SYNC SCRAP - Links grouped by Product {} {} {}", dealer, metal, form);
        LogBuilder.logTimeStamp();
    }

    public void scrapLinksWoutProduct(boolean saveHistory) {
        LOGGER.info("START SCRAP - Links without Product");
        // Scrap Links without Product
        metalScraper.generalScrapAndSleep(
                linkService.findByProductId(null),
                saveHistory
        );
        LOGGER.info("END SCRAP - Links without Product");
        LogBuilder.logTimeStamp();
    }

    @PostMapping("/products/missing")
    public void linksWithoutProduct(boolean saveHistory) {
        ScrapHistory.startRunning();
        scrapLinksWoutProduct(saveHistory);
        ScrapHistory.stopRunning();
    }

    /**
     * Scrap products for ALL Links. Including Links which doesn't have Product yet.
     */
    @PostMapping({"/products/async"})
    public void allProductsAsync(boolean saveHistory) {


        try {
            ScrapHistory.frequencyHandlingAll(false);
            ScrapHistory.startRunning();
        } catch (ScrapRefusedException e) {
            return;
        }

        LOGGER.info("All products SCRAP ASYNC");
        metalScraper.generalScrapAndSleep(
                linkService.findByParams(null, null),
                saveHistory
        );
        // TODO Scrap others
        LogBuilder.logTimeStamp();
        LOGGER.info("All products scraped");

        ScrapHistory.timeUpdate(false, true);
        ScrapHistory.stopRunning();
    }

    @PostMapping("/product/{id}")
    public void scrapProductById(@PathVariable long id) {
        ScrapHistory.startRunning();

        LOGGER.info("Client Product ID "+id);
        metalScraper.generalScrapAndSleep(
                linkService.findByProductId(id),
                true
        );
        LogBuilder.logTimeStamp();
        LOGGER.info("Products with ID "+id+" scraped");

        ScrapHistory.stopRunning();
    }

    /**
     * Method is scraping by Links of Products.
     * New Links and other Links without corresponding Products will NOT BE SCRAPED!
     * Scraping of Prices is grouped by Product. Prices from different Dealers, are for each Product,
     * scraped at the same time. Thanks to that, Prices are time synchronized.
     */
    @PostMapping(value ="/param", headers = "Accept=application/json;charset=UTF-8")
    public void scrapProductsInSyncByMetal(@RequestParam Metal metal) {
        try {
            scrapHistory.frequencyHandling(metal);
            // Lock guard
            ScrapHistory.startRunning();
        } catch (ScrapRefusedException e) {
            return;
        }

        scrapLinksGroupedByProduct(null, null, metal, null, true, null);

        // Unlock guard
        scrapHistory.timeUpdate(metal);
        ScrapHistory.stopRunning();
        // Log
        LogBuilder.logTimeStamp();
        LOGGER.info(metal + " products scraped");
    }

    @PostMapping("/serenity")
    public void serenity() {
        this.serenityScraper.tickersScrap(TickerState.GOOD);
    }

    //////// Links

    /**
     * Scraps Links from all Dealers
     */
    @PostMapping("/links")
    public void allLinks() {
        ScrapHistory.frequencyHandlingAll(true);
        ScrapHistory.startRunning();

        LOGGER.info("All link SCRAP ON progress...");
        long start = System.nanoTime();
        this.metalScraper.allLinksScrap();
        long finish = System.nanoTime();
        LOGGER.info("...SCRAP OFF finished in " + (finish - start)/1000000/1000.0 + " seconds");

        ScrapHistory.timeUpdate(true, false);
        ScrapHistory.stopRunning();
    }

//    TODO Test & Activate this endpoint
//    @RequestMapping({"/productsIds", "/productsIds/"})
//    public void byProductIds(@RequestBody List<Long> productIds) {
//        ScrapHistory.startRunning();
//        try {
//            // Scraps from all dealers
//            this.scrapMetals.values().forEach(
//                    scrapMetal -> {
//                        LOGGER.info("MetalScraper pricesByProducts");
//                        scrapMetal.scrapProductByIdList(productIds);
//                        LOGGER.info(">> Prices scraped");
//                        LogBuilder.printTimeStamp();
//                    }
//            );
//        } catch (ResponseStatusException e){
//            ScrapHistory.stopRunning();
//            throw e;
//        }
//        ScrapHistory.stopRunning();
//        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
//    }

//    TODO Test & Activate this endpoint
//    @RequestMapping({"/portfolio/{id}", "/portfolio/{id}/"})
//    public void scrapProductsByPortfolio(@PathVariable long id) {
//        ScrapHistory.startRunning();
//        try {
//            // Scraps products from all dealers
//            this.scrapMetals.values().forEach(
//                    scrapMetal -> scrapMetal.productByPortfolio(id)
//            );
//        } catch (ResponseStatusException e){
//            ScrapHistory.stopRunning();
//            throw e;
//        }
//        ScrapHistory.stopRunning();
//        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
//    }

}
