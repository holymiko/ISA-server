package home.holymiko.ScrapApp.Server.API.Controller;

import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.TickerState;
import home.holymiko.ScrapApp.Server.Entity.Product;
import home.holymiko.ScrapApp.Server.Scraps.ScrapBessergold;
import home.holymiko.ScrapApp.Server.Scraps.ScrapSerenity;
import home.holymiko.ScrapApp.Server.Scraps.ScrapZlataky;
import home.holymiko.ScrapApp.Server.Service.ProductService;
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
@RequestMapping("/api/v1/scrap")
public class ScrapController {
    private static final long MINUTES_DELAY = 15;
    private static final long YEAR_DELAY = 1000L;
    private LocalDateTime lastAll = LocalDateTime.now().minusYears(YEAR_DELAY);
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

    private final ScrapBessergold scrapBessergold;
    private final ScrapZlataky scrapZlataky;
    private final ScrapSerenity scrapSerenity;
    private final ProductService productService;

    @Autowired
    public ScrapController(ScrapBessergold scrapBessergold, ScrapZlataky scrapZlataky, ScrapSerenity scrapSerenity, ProductService productService) {
        this.scrapBessergold = scrapBessergold;
        this.scrapZlataky = scrapZlataky;
        this.scrapSerenity = scrapSerenity;
        this.productService = productService;
    }

    @RequestMapping({"/all", "/all/"})
    public void all() {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        if( compareDates(lastAll) ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
        }

        isRunning = true;

        System.out.println("SCRAP ALL");
        scrapAllLinks();
        scrapAllProductsOrPrices();

        isRunning = false;
//        this.portfolioService.saveInitPortfolios();
    }

    //////// Products

    @RequestMapping({"/products", "/products/"})
    public void allProducts() {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        if( compareDates(lastAll) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All products were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        scrapAllProductsOrPrices();

        isRunning = false;
    }

    @RequestMapping({"/dealer/{dealer}", "/dealer/{dealer}/"})
    public void scrapProductsByDealer(@PathVariable String dealer) {
        if( isRunning ) {
            System.out.println("Too Many Requests");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
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
        if( isRunning ) {
            System.out.println("lastAll Too Many Requests");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        if( compareDates(lastAll) ) {
            System.out.println("lastAll Bad request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All products were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        System.out.println("Trying to scrap "+metal+" prices");
        switch (metal.toLowerCase(Locale.ROOT)) {
            case "gold" -> {
                if (compareDates(lastGold)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.GOLD);
                this.scrapZlataky.pricesByMetal(Metal.GOLD);
                lastGold = LocalDateTime.now();
            }
            case "silver" -> {
                if (compareDates(lastSilver)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.SILVER);
                this.scrapZlataky.pricesByMetal(Metal.SILVER);
                lastSilver = LocalDateTime.now();
            }
            case "platinum" -> {
                if (compareDates(lastPlatinum)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.PLATINUM);
                this.scrapZlataky.pricesByMetal(Metal.PLATINUM);
                lastPlatinum = LocalDateTime.now();
            }
            case "palladium" -> {
                if (compareDates(lastPalladium)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.PALLADIUM);
                this.scrapZlataky.pricesByMetal(Metal.PALLADIUM);
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
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        isRunning = true;

        try {
            this.scrapBessergold.productsByPortfolio(id);
            this.scrapZlataky.productsByPortfolio(id);
        } catch (ResponseStatusException e){
            isRunning = false;
            throw e;
        }
        isRunning = false;
        throw new ResponseStatusException(HttpStatus.OK, "ScrapMetal done");
    }

    @RequestMapping({"/productsIds", "/productsIds/"})              // Wasn't tested
    public void byProductIds(@RequestBody List<Long> productIds) {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        isRunning = true;

        try {
            this.scrapBessergold.pricesByProducts(productIds);
            this.scrapZlataky.pricesByProducts(productIds);
        } catch (ResponseStatusException e){
            isRunning = false;
            throw e;
        }
        isRunning = false;
        throw new ResponseStatusException(HttpStatus.OK, "ScrapMetal done");
    }

    @RequestMapping({"/serenity", "/serenity/"})
    public void serenity() {
        this.scrapSerenity.tickersScrap(TickerState.GOOD);
    }

    //////// Links

    @RequestMapping({"/links", "/links/"})
    public void allLinks() {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        if( compareDates(lastAllLinks) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All links were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        scrapAllLinks();

        isRunning = false;
    }

    @RequestMapping({"/links/{string}", "/links/{string}/"})
    public void linksBy(@PathVariable String string) {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
        if( compareDates(lastAllLinks) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All links were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        System.out.println("Trying to scrap "+string+" links");
        switch (string.toLowerCase(Locale.ROOT)) {
            case "gold" -> {
                if (compareDates(lastGoldLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.goldLinksScrap();
                lastGoldLinks = LocalDateTime.now();
            }
            case "silver" -> {
                if (compareDates(lastSilverLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.silverLinksScrap();
                lastSilverLinks = LocalDateTime.now();
            }
            case "platinum" -> {
                if (compareDates(lastPlatinumLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.platinumLinksScrap();
                lastPlatinumLinks = LocalDateTime.now();
            }
            case "palladium" -> {
                if (compareDates(lastPalladiumLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.palladiumLinksScrap();
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

    //////// Utils

    private boolean compareDates(LocalDateTime x) {
        return LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(x);
    }

    private void scrapAllProductsOrPrices() {
        System.out.println("Scrap ALL products");

        System.out.println("Scrap all bessergold products");
        this.scrapBessergold.byDealerScrap();

        System.out.println("Scrap all zlataky products");
        this.scrapZlataky.byDealerScrap();

        lastAll = LocalDateTime.now();
    }

    private void scrapAllLinks() {
        System.out.println("Scrap all bessergold links");
        this.scrapBessergold.allLinksScrap();

        System.out.println("Scrap all zlataky links");
        this.scrapZlataky.allLinksScrap();

        lastAllLinks = LocalDateTime.now();
    }

}
