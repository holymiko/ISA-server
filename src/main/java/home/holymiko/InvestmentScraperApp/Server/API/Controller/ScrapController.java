package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapRefusedException;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.SerenityScraper;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/scrap")
public class ScrapController {

    private final MetalScraper scrapMetal;
    private final SerenityScraper serenityScraper;
    private final CNBScraper cnbScraper;
    private final ScrapHistory scrapHistory;

    // TODO Endpoint for cnbScraper
    // TODO Endpoint for scrap stock by ticker
    // TODO method byPortfolio should include stock scraping


    @Autowired
    public ScrapController(MetalScraper scrapMetal, SerenityScraper serenityScraper, CNBScraper cnbScraper, ScrapHistory scrapHistory) {
        this.scrapMetal = scrapMetal;
        this.serenityScraper = serenityScraper;
        this.cnbScraper = cnbScraper;
        this.scrapHistory = scrapHistory;
    }

    @RequestMapping({"/all", "/all/"})
    public void scrapEverything() {
        allLinks();
        allProducts();
        serenity();
    }

    //////// Products

    @RequestMapping({"/products", "/products/"})
    public void allProducts() {
        ScrapHistory.frequencyHandlingAll(false);
        ScrapHistory.startRunning();

        System.out.println("Client ALL products");

        this.scrapMetal.allProducts();

        ScrapHistory.timeUpdate(false, true);
        ScrapHistory.stopRunning();
    }

    @RequestMapping({"/product/{id}", "/product/{id}/"})
    public void scrapProductById(@PathVariable long id) {
        ScrapHistory.startRunning();

        System.out.println("Client Product ID "+id);

        this.scrapMetal.scrapProductById(id);

        ScrapHistory.stopRunning();
    }

    @RequestMapping(method = RequestMethod.GET, value ="/param", headers = "Accept=application/json;charset=UTF-8")
    public void scrapProductsByParam(
        @RequestParam(required = false)
        Boolean isRedemption,
        @RequestParam(required = false)
        Dealer dealer,
        @RequestParam(required = false)
        Metal metal,
        @RequestParam(required = false)
        Form form
    ) {
        scrapHistory.frequencyHandling(metal);
        scrapHistory.frequencyHandling(dealer);
        // TODO Lock guard
        ScrapHistory.isRunning();
        System.out.println("By param scrap products");
        System.out.println("isRedemption: "+isRedemption);

        // TODO Lock guard
        ScrapHistory.startRunning();

        scrapMetal.scrapByParam(isRedemption, dealer, metal, form, null);

        // TODO Unlock
        scrapHistory.timeUpdate(dealer);
        ScrapHistory.stopRunning();
    }

    @RequestMapping({"/serenity", "/serenity/"})
    public void serenity() {
        this.serenityScraper.tickersScrap(TickerState.GOOD);
    }

    //////// Links

    @RequestMapping({"/links", "/links/"})
    public void allLinks() {
        ScrapHistory.frequencyHandlingAll(true);
        ScrapHistory.startRunning();

        System.out.println("Client ALL links");

        // Scraps from all dealers
        this.scrapMetal.allLinksScrap();
        ConsolePrinter.printTimeStamp();

        ScrapHistory.timeUpdate(true, false);
        ScrapHistory.stopRunning();
    }

    // TODO Rebuild this on scrap by param, viz scrapProductsByParam
    @Deprecated
    @RequestMapping({"/links/{string}", "/links/{string}/"})
    public void scrapLinksByParam(@PathVariable String string) {
        ScrapHistory.frequencyHandlingAll(true);
        ScrapHistory.isRunning();

        System.out.println("Trying to scrap "+string+" links");

        // 1. Try convert string to Dealer
        try {
            Dealer dealer = Convert.dealerConvert(string);
            scrapHistory.frequencyHandling(dealer);
            ScrapHistory.startRunning();
            // TODO
            scrapMetal.allLinksScrap();
            scrapHistory.timeUpdate(dealer);
            ScrapHistory.stopRunning();
            return;
        } catch (IllegalArgumentException ignored) {
        }

        // 2. Try convert string to Metal
        Metal metal = extractAndCheckFrequency(string);
        ScrapHistory.startRunning();

        // Metal scraping. Links from all dealers by metal
        // TODO
//        this.scrapMetals.values().forEach(
//                metalScraper -> metalScraper.linkScrap(metal)
//        );
        scrapHistory.timeUpdateLink(metal);
        ScrapHistory.stopRunning();
    }

    ////// PRIVATE

    private Metal extractAndCheckFrequency(String string) throws ScrapRefusedException, ResponseStatusException {
        // Extract the metal
        Metal metal;
        try{
            metal = Convert.metalConvert(string);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        scrapHistory.frequencyHandling(metal);
        return metal;
    }

//    TODO Test & Activate this endpoint
//    @RequestMapping({"/productsIds", "/productsIds/"})
//    public void byProductIds(@RequestBody List<Long> productIds) {
//        ScrapHistory.startRunning();
//        try {
//            // Scraps from all dealers
//            this.scrapMetals.values().forEach(
//                    scrapMetal -> {
//                        System.out.println("MetalScraper pricesByProducts");
//                        scrapMetal.scrapProductByIdList(productIds);
//                        System.out.println(">> Prices scraped");
//                        ConsolePrinter.printTimeStamp();
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
