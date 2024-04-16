package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.*;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

@RestController
@RequestMapping("/api/v2/info")
@AllArgsConstructor
public class AppInfoController extends BaseController {

    private static final String APP_CONFIG_PATH = "target/classes/application.properties";


    private final RateService rateService;
    private final LinkService linkService;
    private final ProductService productService;
    private final PriceService priceService;
    private final TickerService tickerService;
    private final StockGrahamService stockGrahamService;

    @GetMapping("/stats")
    public String getDatabaseStatistics() {
        return "rates: " + rateService.count() +
                "\nlinks: " + linkService.countByParams(null) +
                "\nproducts: " + productService.countByParams(null, null, null, null, null, null, null) +
                "\nprice pairs: " + priceService.countPricePairs() +
                "\nprice pairs history: " + priceService.countPricePairsHistory() +
                "\ntickers: " + tickerService.countByParams(null) +
                "\nstocks: " + stockGrahamService.countByParams();
    }

    @GetMapping("/version")
    public String getProjectVersion() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(APP_CONFIG_PATH));

        return appProps.getProperty("project.version");
    }

}
