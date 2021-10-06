package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer.BessergoldScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.SerenityScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.metalDealer.ZlatakyScraper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/scrap")
public class ScrapController {
    private static final long MINUTES_DELAY = 15;
    private static final long YEAR_DELAY = 1000L;

    private LocalDateTime lastAllProducts = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastGold = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastSilver = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPlatinum = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPalladium = LocalDateTime.now().minusYears(YEAR_DELAY);

    private LocalDateTime lastAllLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastGoldLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastSilverLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPlatinumLinks = LocalDateTime.now().minusYears(YEAR_DELAY);
    private LocalDateTime lastPalladiumLinks = LocalDateTime.now().minusYears(YEAR_DELAY);

    private boolean isRunning = false;

    private final BessergoldScraper scrapBessergold;
    private final ZlatakyScraper scrapZlataky;
    private final SerenityScraper scrapSerenity;

    // Used for Polymorphic calling
    private final List<MetalScraper> scrapMetals = new ArrayList<>();

    @Autowired
    public ScrapController(BessergoldScraper scrapBessergold, ZlatakyScraper scrapZlataky, SerenityScraper scrapSerenity) {
        this.scrapBessergold = scrapBessergold;
        this.scrapZlataky = scrapZlataky;
        this.scrapSerenity = scrapSerenity;
        this.scrapMetals.add(scrapBessergold);
        this.scrapMetals.add(scrapZlataky);
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
        dateTimeCheck(lastAllProducts);

        isRunning = true;

        scrapAllProductsOrPrices();

        isRunning = false;
    }

    @RequestMapping({"/dealer/{dealer}", "/dealer/{dealer}/"})
    public void scrapProductsByDealer(@PathVariable String dealer) {
        isRunningCheck();
        dateTimeCheck(lastAllProducts);

        isRunning = true;

        System.out.println("Trying to scrap "+dealer+" products");

        switch (dealer.toLowerCase(Locale.ROOT)) {
            case "bessergold" -> this.scrapBessergold.byDealerScrap();
            case "zlataky" -> this.scrapZlataky.byDealerScrap();
            default -> {
                isRunning = false;
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }

        isRunning = false;
    }

    @RequestMapping({"/metal/{metal}", "/metal/{metal}/"})
    public void byMetal(@PathVariable String metal) {
        isRunningCheck();
        dateTimeCheck(lastAllProducts);

        isRunning = true;

        System.out.println("Trying to scrap "+metal+" prices");

        switch (metal.toLowerCase(Locale.ROOT)) {
            case "gold" -> {
                dateTimeCheck(lastGold);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(Metal.GOLD)
                );
                lastGold = LocalDateTime.now();
            }
            case "silver" -> {
                dateTimeCheck(lastSilver);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(Metal.SILVER)
                );
                lastSilver = LocalDateTime.now();
            }
            case "platinum" -> {
                dateTimeCheck(lastPlatinum);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(Metal.PLATINUM)
                );
                lastPlatinum = LocalDateTime.now();
            }
            case "palladium" -> {
                dateTimeCheck(lastPalladium);

                // Scraps prices from all dealers
                this.scrapMetals.forEach(
                        scrapMetal -> scrapMetal.pricesByMetal(Metal.PALLADIUM)
                );
                lastPalladium = LocalDateTime.now();
            }
            default -> {
                isRunning = false;
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        }
        isRunning = false;
    }

    @RequestMapping({"/portfolio/{id}", "/portfolio/{id}/"})
    public void byPortfolio(@PathVariable long id) {
        isRunningCheck();

        isRunning = true;

        try {
            // Scraps products from all dealers
            this.scrapMetals.forEach(
                    scrapMetal -> scrapMetal.productsByPortfolio(id)
            );
        } catch (ResponseStatusException e){
            isRunning = false;
            throw e;
        }
        isRunning = false;
        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
    }

    @RequestMapping({"/productsIds", "/productsIds/"})              // Wasn't tested
    public void byProductIds(@RequestBody List<Long> productIds) {
        isRunningCheck();

        isRunning = true;

        try {
            // Scraps from all dealers
            this.scrapMetals.forEach(
                    scrapMetal -> scrapMetal.pricesByProducts(productIds)
            );
        } catch (ResponseStatusException e){
            isRunning = false;
            throw e;
        }
        isRunning = false;
        throw new ResponseStatusException(HttpStatus.OK, "MetalScraper done");
    }

    @RequestMapping({"/serenity", "/serenity/"})
    public void serenity() {
        this.scrapSerenity.tickersScrap(TickerState.GOOD);
    }

    //////// Links

    @RequestMapping({"/links", "/links/"})
    public void allLinks() {
        isRunningCheck();
        dateTimeCheck(lastAllLinks);

        isRunning = true;

        scrapAllLinks();

        isRunning = false;
    }

    @RequestMapping({"/links/{string}", "/links/{string}/"})
    public void linksBy(@PathVariable String string) {
        isRunningCheck();
        dateTimeCheck(lastAllLinks);

        isRunning = true;

        System.out.println("Trying to scrap "+string+" links");

        switch (string.toLowerCase(Locale.ROOT)) {
            case "gold" -> {
                dateTimeCheck(lastGoldLinks);

                // Scraps from all dealers
                this.scrapMetals.forEach(
                        MetalScraper::goldLinksScrap
                );

                lastGoldLinks = LocalDateTime.now();
            }
            case "silver" -> {
                dateTimeCheck(lastSilverLinks);

                // Scraps from all dealers
                this.scrapMetals.forEach(
                        MetalScraper::silverLinksScrap
                );

                lastSilverLinks = LocalDateTime.now();
            }
            case "platinum" -> {
                dateTimeCheck(lastPlatinumLinks);

                // Scraps from all dealers
                this.scrapMetals.forEach(
                        MetalScraper::platinumLinksScrap
                );

                lastPlatinumLinks = LocalDateTime.now();
            }
            case "palladium" -> {
                dateTimeCheck(lastPalladiumLinks);

                // Scraps from all dealers
                this.scrapMetals.forEach(
                        MetalScraper::platinumLinksScrap
                );

                lastPalladiumLinks = LocalDateTime.now();
            }
            case "bessergold" -> this.scrapBessergold.byDealerScrap();
            case "zlataky" -> this.scrapZlataky.byDealerScrap();

            default -> {
                isRunning = false;
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
            }
        }
        isRunning = false;
    }


    //////// PRIVATE

    private void dateTimeCheck(LocalDateTime localDateTime) throws ResponseStatusException{
        if ( LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(localDateTime) ) {
            isRunning = false;
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " minutes ago");
        }
    }

    private void isRunningCheck() throws ResponseStatusException {
        if( isRunning ) {
            System.out.println("ScrapController - TOO MANY REQUESTS");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another MetalScraper is running right now");
        }
    }

    private void scrapAllProductsOrPrices() {
        System.out.println("Scraper ALL products");

        // Scraps from all dealers
        this.scrapMetals.forEach(
                MetalScraper::byDealerScrap
        );

        lastAllProducts = LocalDateTime.now();
    }

    private void scrapAllLinks() {
        System.out.println("Scraper ALL links");

        // Scraps from all dealers
        this.scrapMetals.forEach(
                MetalScraper::allLinksScrap
        );

        lastAllLinks = LocalDateTime.now();
    }

}
