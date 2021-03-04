package home.holymiko.ScrapApp.Server.Controller;

import home.holymiko.ScrapApp.Server.Entity.Metal;
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

    private LocalDateTime lastAll = LocalDateTime.now().minusYears(1000L);
    private LocalDateTime lastGold = LocalDateTime.now().minusYears(1000L);
    private LocalDateTime lastSilver = LocalDateTime.now().minusYears(1000L);
    private LocalDateTime lastPlatinum = LocalDateTime.now().minusYears(1000L);
    private LocalDateTime lastPalladium = LocalDateTime.now().minusYears(1000L);
    private boolean isRunning = false;
    private final Scrap scrap;

    @Autowired
    public ScrapController(Scrap scrap) {
        this.scrap = scrap;
    }

    @RequestMapping({"/all", "/all/"})
    public void scrapAll() {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        if( compareDates(lastAll) ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Updated less then "+MINUTES_DELAY+" ago");
        }
        
        isRunning = true;

        System.out.println("Scrap all products");
        this.scrap.scrapAllProducts();
        lastAll = LocalDateTime.now();

        isRunning = false;
    }

    @RequestMapping({"/metal/{metal}", "/metal/{metal}/"})
    public void scrapMetal(@PathVariable String metal) {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        if( compareDates(lastAll) ) {
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
    public void scrapPortfolioProducts(@PathVariable long id) {
        if( isRunning ) {
            throw new ResponseStatusException(HttpStatus.TOO_MANY_REQUESTS, "Another Scrap running");
        }
        isRunning = true;

        this.scrap.scrapPortfolioProducts(id);
        lastAll = LocalDateTime.now();

        isRunning = false;
    }

    private boolean compareDates(LocalDateTime x) {
        return LocalDateTime.now().minusMinutes(MINUTES_DELAY).isBefore(x);
    }

}
