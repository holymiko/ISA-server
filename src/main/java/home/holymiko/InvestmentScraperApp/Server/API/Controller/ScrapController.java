package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.exception.ScrapRefusedException;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.dataHandeling.Convert;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.SerenityScraper;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

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

    @RequestMapping({"/dealer/{dealer}", "/dealer/{dealer}/"})
    public void scrapProductsByDealer(@PathVariable String dealer) {
        ScrapHistory.frequencyHandlingAll(false);
        ScrapHistory.isRunning();

        Dealer dealerType;

        System.out.println("Trying to scrap "+dealer+" products");

        try {
            // Try convert string to Dealer
            dealerType = Convert.dealerConvert(dealer);
        } catch (IllegalArgumentException ignored) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        ScrapHistory.startRunning();

        scrapMetal.scrapByParam(dealerType, null, null, null);
        scrapHistory.timeUpdate(dealerType);
        ScrapHistory.stopRunning();
    }

    @RequestMapping({"/metal/{metal}", "/metal/{metal}/"})
    public void byMetal(@PathVariable String string) {
        ScrapHistory.frequencyHandlingAll(false);
        ScrapHistory.isRunning();

        Metal metal = extractAndCheckFrequency(string);

        ScrapHistory.startRunning();

        // Scraps prices from all dealers
        System.out.println("Trying to scrap "+string+" prices");
        System.out.println("MetalScraper pricesByMetal");
        scrapMetal.scrapByParam(null, metal, null, null);
        System.out.println(metal+" prices scraped");
        ConsolePrinter.printTimeStamp();

        scrapHistory.timeUpdate(metal);
        ScrapHistory.stopRunning();
    }

    @RequestMapping({"/portfolio/{id}", "/portfolio/{id}/"})
    public void byPortfolio(@PathVariable long id) {
        ScrapHistory.startRunning();
//      TODO
//        try {
//            // Scraps products from all dealers
//            this.scrapMetals.values().forEach(
//                    scrapMetal -> scrapMetal.productByPortfolio(id)
//            );
//        } catch (ResponseStatusException e){
//            ScrapHistory.stopRunning();
//            throw e;
//        }
        ScrapHistory.stopRunning();
        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
    }

    @RequestMapping({"/productsIds", "/productsIds/"})              // Wasn't tested
    public void byProductIds(@RequestBody List<Long> productIds) {
        ScrapHistory.startRunning();
//      TODO
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
        ScrapHistory.stopRunning();
        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
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

    @RequestMapping({"/links/{string}", "/links/{string}/"})
    public void linksBy(@PathVariable String string) {
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

}
