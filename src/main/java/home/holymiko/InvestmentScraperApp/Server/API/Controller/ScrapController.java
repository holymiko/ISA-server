package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.EnumKnown.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer.BessergoldScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.SerenityScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer.ZlatakyScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/scrap")
public class ScrapController {

    private final BessergoldScraper bessergoldScraper;
    private final ZlatakyScraper zlatakyScraper;
    private final SerenityScraper serenityScraper;
    private final CNBScraper cnbScraper;
    private final ScrapHistory scrapHistory;

    // TODO Endpoint for cnbScraper
    // TODO Endpoint for scrap stock by ticker
    // TODO method byPortfolio including stock scrap

    // Used for Polymorphic calling
    private final List<MetalScraper> scrapMetals = new ArrayList<>();

    @Autowired
    public ScrapController(BessergoldScraper bessergoldScraper, ZlatakyScraper zlatakyScraper, SerenityScraper serenityScraper, CNBScraper cnbScraper, ScrapHistory scrapHistory) {
        this.bessergoldScraper = bessergoldScraper;
        this.zlatakyScraper = zlatakyScraper;
        this.serenityScraper = serenityScraper;
        this.cnbScraper = cnbScraper;
        this.scrapHistory = scrapHistory;

        this.scrapMetals.add(bessergoldScraper);
        this.scrapMetals.add(zlatakyScraper);
    }

    @RequestMapping({"/all", "/all/"})
    public void all() {
        allLinks();
        allProducts();
    }

    //////// Products

    @RequestMapping({"/products", "/products/"})
    public void allProducts() {
        isRunningCheck();
        ScrapHistory.frequencyHandlingAll(false);

        ScrapHistory.setIsRunning(true);

        scrapAllProductsOrPrices();

        ScrapHistory.setIsRunning(false);
    }

    @RequestMapping({"/dealer/{dealer}", "/dealer/{dealer}/"})
    public void scrapProductsByDealer(@PathVariable String dealer) {
        isRunningCheck();
        ScrapHistory.frequencyHandlingAll(false);

        ScrapHistory.setIsRunning(true);

        System.out.println("Trying to scrap "+dealer+" products");

        switch (dealer.toLowerCase(Locale.ROOT)) {
            case "bessergold" -> this.bessergoldScraper.productByDealer();
            case "zlataky" -> this.zlatakyScraper.productByDealer();
            default -> {
                ScrapHistory.setIsRunning(false);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        ScrapHistory.setIsRunning(false);
    }

    @RequestMapping({"/metal/{metal}", "/metal/{metal}/"})
    public void byMetal(@PathVariable String metal) {
        isRunningCheck();
        ScrapHistory.frequencyHandlingAll(false);

        ScrapHistory.setIsRunning(true);

        System.out.println("Trying to scrap "+metal+" prices");

        // TODO Convert data type of Metal
        switch (metal.toLowerCase(Locale.ROOT)) {
            case "gold" -> {
                scrapHistory.frequencyHandling(Metal.GOLD);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal.GOLD)
                );
                scrapHistory.timeUpdate(Metal.GOLD);
            }
            case "silver" -> {
                scrapHistory.frequencyHandling(Metal.SILVER);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal.SILVER)
                );
                scrapHistory.timeUpdate(Metal.SILVER);
            }
            case "platinum" -> {
                scrapHistory.frequencyHandling(Metal.PLATINUM);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal.PLATINUM)
                );
                scrapHistory.timeUpdate(Metal.PLATINUM);
            }
            case "palladium" -> {
                scrapHistory.frequencyHandling(Metal.PALLADIUM);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal.PALLADIUM)
                );
                scrapHistory.timeUpdate(Metal.PALLADIUM);
            }
            default -> {
                ScrapHistory.setIsRunning(false);
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        ScrapHistory.setIsRunning(false);
    }

    @RequestMapping({"/portfolio/{id}", "/portfolio/{id}/"})
    public void byPortfolio(@PathVariable long id) {
        isRunningCheck();

        ScrapHistory.setIsRunning(true);

        try {
            // Scraps products from all dealers
            this.scrapMetals.forEach(
                    scrapMetal -> scrapMetal.productByPortfolio(id)
            );
        } catch (ResponseStatusException e){
            ScrapHistory.setIsRunning(false);
            throw e;
        }
        ScrapHistory.setIsRunning(false);
        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
    }

    @RequestMapping({"/productsIds", "/productsIds/"})              // Wasn't tested
    public void byProductIds(@RequestBody List<Long> productIds) {
        isRunningCheck();

        ScrapHistory.setIsRunning(true);

        try {
            // Scraps from all dealers
            this.scrapMetals.forEach(
                    scrapMetal -> scrapMetal.pricesByProducts(productIds)
            );
        } catch (ResponseStatusException e){
            ScrapHistory.setIsRunning(false);
            throw e;
        }
        ScrapHistory.setIsRunning(false);
        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
    }

    @RequestMapping({"/serenity", "/serenity/"})
    public void serenity() {
        this.serenityScraper.tickersScrap(TickerState.GOOD);
    }

    //////// Links

    @RequestMapping({"/links", "/links/"})
    public void allLinks() {
        isRunningCheck();
        ScrapHistory.frequencyHandlingAll(true);

        ScrapHistory.setIsRunning(true);

        scrapAllLinks();

        ScrapHistory.setIsRunning(false);
    }

    @RequestMapping({"/links/{string}", "/links/{string}/"})
    public void linksBy(@PathVariable String string) {
        isRunningCheck();
        ScrapHistory.frequencyHandlingAll(true);

        ScrapHistory.setIsRunning(true);

        System.out.println("Trying to scrap "+string+" links");

        // Links by Dealer
        switch (string.toLowerCase(Locale.ROOT)) {
            // BESSERGOLD
            case "bessergold" -> {
                this.bessergoldScraper.allLinksScrap();
                ScrapHistory.setIsRunning(false);
                return;
            }
            // ZLATAKY
            case "zlataky" -> {
                this.zlatakyScraper.allLinksScrap();
                ScrapHistory.setIsRunning(false);
                return;
            }
        }

        // Links by Metal
        Metal metal = switch (string.toLowerCase(Locale.ROOT)) {
            // GOLD
            case "gold" -> Metal.GOLD;
            // SILVER
            case "silver" -> Metal.SILVER;
            // PLATINUM
            case "platinum" -> Metal.PLATINUM;
            // PALLADIUM
            case "palladium" -> Metal.PALLADIUM;

            default -> {
                ScrapHistory.setIsRunning(false);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        };

        // Metal scraping. Links from all dealers by metal
        scrapHistory.frequencyHandling(metal);
        this.scrapMetals.forEach(
                metalScraper -> metalScraper.linkScrap(metal)
        );
        scrapHistory.timeUpdateLink(metal);
    }


    //////// PRIVATE

    private void isRunningCheck() throws ResponseStatusException {
        if( ScrapHistory.isRunning() ) {
            System.out.println("ScrapController - TOO MANY REQUESTS");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another MetalScraper is running right now");
        }
    }

    private void scrapAllProductsOrPrices() {
        System.out.println("Scraper ALL products");

        // Scraps from all dealers
        this.scrapMetals.forEach(
                MetalScraper::productByDealer
        );

        ScrapHistory.frequencyHandlingAll(false);
    }

    private void scrapAllLinks() {
        System.out.println("Scraper ALL links");

        // Scraps from all dealers
        this.scrapMetals.forEach(
                MetalScraper::allLinksScrap
        );

        ScrapHistory.timeUpdate(true);
    }

}
