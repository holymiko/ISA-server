package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Scrap;
import home.holymiko.ScrapApp.Server.Service.PortfolioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
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
    private final Scrap scrap;

    @Autowired
    public ScrapController(Scrap scrap) {
        this.scrap = scrap;
    }



    @RequestMapping({"/all", "/all/"})
    public void all() {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        if( compareDates(lastAll) ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
        }

        isRunning = true;

        System.out.println("Scrap all");

        System.out.println("Scrap all links");
        this.scrap.scrapAllLinks();
        lastAllLinks = LocalDateTime.now();

        System.out.println("Scrap all products");
        this.scrap.scrapAllProducts();
        lastAll = LocalDateTime.now();

        isRunning = false;
    }

    //////// Products

    @RequestMapping({"/metal/{metal}", "/metal/{metal}/"})
    public void byMetal(@PathVariable String metal) {
        if( isRunning ) {
            System.out.println("lastAll Too Many Requests");
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        if( compareDates(lastAll) ) {
            System.out.println("lastAll Bad request");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All products were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        System.out.println("Trying to scrap "+metal+" prices");
        switch (metal.toLowerCase(Locale.ROOT)) {
            case "gold": {
                if( compareDates(lastGold) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapPricesByMetal(Metal.GOLD);
                lastGold = LocalDateTime.now();
                break;
            }
            case "silver": {
                if( compareDates(lastSilver) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapPricesByMetal(Metal.SILVER);
                lastSilver = LocalDateTime.now();
                break;
            }
            case "platinum": {
                if( compareDates(lastPlatinum) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapPricesByMetal(Metal.PLATINUM);
                lastPlatinum = LocalDateTime.now();
                break;
            }
            case "palladium": {
                if( compareDates(lastPalladium) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapPricesByMetal(Metal.PALLADIUM);
                lastPalladium = LocalDateTime.now();
                break;
            }
            default: {
                isRunning = false;
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        };
        isRunning = false;
    }

    @RequestMapping({"/portfolio/{id}", "/portfolio/{id}/"})
    public void byPortfolio(@PathVariable long id) {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        isRunning = true;

        try {
            this.scrap.scrapPortfolioProducts(id);
        } catch (ResponseStatusException e){
            isRunning = false;
            throw e;
        }
        isRunning = false;
        throw new ResponseStatusException(HttpStatus.OK, "Scrap done");
    }

    //////// Links

    @RequestMapping({"/links", "/links/"})
    public void allLinks() {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        if( compareDates(lastAllLinks) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All links were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        System.out.println("Scrap all links");
        this.scrap.scrapAllLinks();
        lastAllLinks = LocalDateTime.now();

        isRunning = false;
    }

    @RequestMapping({"/links/{metal}", "/links/{metal}/"})
    public void linksByMetal(@PathVariable String metal) {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        if( compareDates(lastAllLinks) ) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "All links were scraped less then "+MINUTES_DELAY+" ago");
        }
        isRunning = true;

        System.out.println("Trying to scrap "+metal+" links");
        switch (metal.toLowerCase(Locale.ROOT)) {
            case "gold": {
                if( compareDates(lastGoldLinks) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapGoldLinks();
                lastGoldLinks = LocalDateTime.now();
                break;
            }
            case "silver": {
                if( compareDates(lastSilverLinks) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapSilverLinks();
                lastSilverLinks = LocalDateTime.now();
                break;
            }
            case "platinum": {
                if( compareDates(lastPlatinumLinks) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapPlatinumLinks();
                lastPlatinumLinks = LocalDateTime.now();
                break;
            }
            case "palladium": {
                if( compareDates(lastPalladiumLinks) ){
                    isRunning = false;
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
                }
                this.scrap.scrapPalladiumLinks();
                lastPalladiumLinks = LocalDateTime.now();
                break;
            }
            default: {
                isRunning = false;
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        };
        isRunning = false;
    }

    //////// Utils

    private boolean compareDates(LocalDateTime x) {
        return LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(x);
    }

}
