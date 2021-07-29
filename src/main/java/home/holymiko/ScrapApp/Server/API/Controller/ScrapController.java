package home.holymiko.ScrapApp.Server.API.Controller;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.TickerState;
import home.holymiko.ScrapApp.Server.Scraps.ScrapBessergold;
import home.holymiko.ScrapApp.Server.Scraps.ScrapSerenity;
import home.holymiko.ScrapApp.Server.Scraps.ScrapZlataky;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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

    @Autowired
    public ScrapController(ScrapBessergold scrapBessergold, ScrapZlataky scrapZlataky, ScrapSerenity scrapSerenity) {
        this.scrapBessergold = scrapBessergold;
        this.scrapZlataky = scrapZlataky;
        this.scrapSerenity = scrapSerenity;
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

        System.out.println("ScrapMetal all");

        System.out.println("ScrapMetal all bessergold links");
        this.scrapBessergold.sAllLinks();
        System.out.println("ScrapMetal all zlataky links");
        this.scrapZlataky.sAllLinks();
        lastAllLinks = LocalDateTime.now();

        System.out.println("ScrapMetal all bessergold products");
        this.scrapBessergold.allProducts();
        System.out.println("ScrapMetal all zlataky products");
        this.scrapZlataky.allProducts();
        lastAll = LocalDateTime.now();

//        this.portfolioService.saveInitPortfolios();

        isRunning = false;
    }

    //////// Products

    @RequestMapping({"/dealer/{dealer}", "/dealer/{dealer}/"})
    public void scrapProductsByDealer(@PathVariable String dealer) {
        if( isRunning ) {
            System.out.println("Too Many Requests");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another ScrapMetal running");
        }
//        if( compareDates(lastAll) ) {
//            System.out.println("Bad request");
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All products were scraped less then "+MINUTES_DELAY+" ago");
//        }
        isRunning = true;

        System.out.println("Trying to scrap "+dealer+" products");
        switch (dealer.toLowerCase(Locale.ROOT)) {
            case "bessergold" -> this.scrapBessergold.productsByDealer(Dealer.BESSERGOLD);
            case "zlataky" -> this.scrapZlataky.productsByDealer(Dealer.ZLATAKY);
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
                lastGold = LocalDateTime.now();
            }
            case "silver" -> {
                if (compareDates(lastSilver)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.SILVER);
                lastSilver = LocalDateTime.now();
            }
            case "platinum" -> {
                if (compareDates(lastPlatinum)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.PLATINUM);
                lastPlatinum = LocalDateTime.now();
            }
            case "palladium" -> {
                if (compareDates(lastPalladium)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.pricesByMetal(Metal.PALLADIUM);
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
            this.scrapBessergold.pricesByProductIds(productIds);
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

        System.out.println("ScrapMetal all links");
        this.scrapBessergold.sAllLinks();
        lastAllLinks = LocalDateTime.now();

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
                this.scrapBessergold.sGoldLinks();
                lastGoldLinks = LocalDateTime.now();
            }
            case "silver" -> {
                if (compareDates(lastSilverLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.sSilverLinks();
                lastSilverLinks = LocalDateTime.now();
            }
            case "platinum" -> {
                if (compareDates(lastPlatinumLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.sPlatinumLinks();
                lastPlatinumLinks = LocalDateTime.now();
            }
            case "palladium" -> {
                if (compareDates(lastPalladiumLinks)) {
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then " + MINUTES_DELAY + " ago");
                }
                this.scrapBessergold.sPalladiumLinks();
                lastPalladiumLinks = LocalDateTime.now();
            }
            case "bessergold" -> this.scrapBessergold.sAllLinks();
            case "zlataky" -> this.scrapZlataky.sAllLinks();
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

}
