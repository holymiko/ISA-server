package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/stats")
@AllArgsConstructor
public class StatsController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StatsController.class);

    private final RateService rateService;
    private final LinkService linkService;
    private final ProductService productService;
    private final PriceService priceService;
    private final TickerService tickerService;

    @GetMapping
    @Operation(description = "Returns stats")
    public String getStats() {
        return "rates: " + rateService.count() +
                "\nlinks: " + linkService.countByParams(null) +
                "\nproducts: " + productService.countByParams(null, null, null, null, null, null, null) +
                "\nprice pairs: " + priceService.countPricePairs() +
                "\ntickers: " + tickerService.countByParams(null);
    }

}
