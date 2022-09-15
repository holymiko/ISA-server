package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.LinkService;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Scraper.MetalScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.CNBScraper;
import home.holymiko.InvestmentScraperApp.Server.Scraper.sources.SerenityScraper;
import home.holymiko.InvestmentScraperApp.Server.API.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/scrap")
public class ScrapController {

    private final MetalScraper metalScraper;
    private final SerenityScraper serenityScraper;
    private final CNBScraper cnbScraper;
    private final ScrapHistory scrapHistory;
    private final LinkService linkService;

    // TODO Endpoint for cnbScraper
    // TODO Endpoint for scrap stock by ticker
    // TODO method byPortfolio should include stock scraping

    @Autowired
    public ScrapController(MetalScraper metalScraper, SerenityScraper serenityScraper, CNBScraper cnbScraper, ScrapHistory scrapHistory, LinkService linkService) {
        this.metalScraper = metalScraper;
        this.serenityScraper = serenityScraper;
        this.cnbScraper = cnbScraper;
        this.scrapHistory = scrapHistory;
        this.linkService = linkService;
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

        // TODO Logging
        System.out.println("Client ALL products");
        metalScraper.generalScrapAndSleep(
                linkService.findAll()
        );
        ConsolePrinter.printTimeStamp();
        System.out.println("All products scraped");

        ScrapHistory.timeUpdate(false, true);
        ScrapHistory.stopRunning();
    }

    @RequestMapping({"/product/{id}", "/product/{id}/"})
    public void scrapProductById(@PathVariable long id) {
        ScrapHistory.startRunning();

        // TODO Logging
        System.out.println("Client Product ID "+id);
        metalScraper.generalScrapAndSleep(
                linkService.findByProductId(id)
        );
        ConsolePrinter.printTimeStamp();
        System.out.println("Products with ID "+id+" scraped");

        ScrapHistory.stopRunning();
    }

    /**
     * Method is scraping by Links of Products.
     * New Links and other Links without corresponding Products will NOT BE SCRAPED!
     */
    @GetMapping(value ="/param", headers = "Accept=application/json;charset=UTF-8")
    public void scrapProductsByParam(
        @RequestParam(required = false)
        Metal metal,
        @RequestParam(required = false)
        Form form
    ) {
        scrapHistory.frequencyHandling(metal);
        // Lock guard
        ScrapHistory.startRunning();

        System.out.println("By param scrap products");
        metalScraper.generalScrapAndSleep(
                linkService.findByProductParams(metal, form)
        );

        // Unlock guard
        scrapHistory.timeUpdate(metal);
        ScrapHistory.stopRunning();
    }

    /**
     * Testing endpoint
     */
    @GetMapping(value ="/dealer", headers = "Accept=application/json;charset=UTF-8")
    public void scrapProductsByDealer(@RequestParam Dealer dealer) {
        scrapHistory.frequencyHandling(dealer);
        // Lock guard
        ScrapHistory.startRunning();

        System.out.println("Scrap products by Dealer."+dealer);
        metalScraper.generalScrapAndSleep(
                linkService.findByDealer(dealer)
        );

        // Unlock guard
        scrapHistory.timeUpdate(dealer);
        ScrapHistory.stopRunning();
    }

    @RequestMapping({"/serenity", "/serenity/"})
    public void serenity() {
        this.serenityScraper.tickersScrap(TickerState.GOOD);
    }

    //////// Links

    /**
     * Scraps Links from all Dealers
     */
    @RequestMapping({"/links", "/links/"})
    public void allLinks() {
        ScrapHistory.frequencyHandlingAll(true);
        ScrapHistory.startRunning();

        // TODO Logging
        System.out.println("Scrap ALL links");
        this.metalScraper.allLinksScrap();
        ConsolePrinter.printTimeStamp();

        ScrapHistory.timeUpdate(true, false);
        ScrapHistory.stopRunning();
    }

    /**
     * Testing endpoint
     */
    @GetMapping({"/links/param", "/links/param/"})
    public void scrapLinksByDealer(@RequestParam Dealer dealer) {
        ScrapHistory.frequencyHandlingAll(true);
        ScrapHistory.startRunning();

        // TODO Logging
        System.out.println("Scrap Links by Dealer."+dealer);
        metalScraper.linksScrap(dealer);
        ConsolePrinter.printTimeStamp();

        // scrapHistory.timeUpdateLink(dealer);
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
