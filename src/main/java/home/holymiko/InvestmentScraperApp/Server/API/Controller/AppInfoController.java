package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.*;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.StatsDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;
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
    public StatsDTO getDatabaseStatistics() {
        return new StatsDTO(
                rateService.count(),
                productService.countByParams(null, null, null, null, null, null, null),
                priceService.countPricePairs(),
                priceService.countPricePairsHistory(),
                tickerService.countByParams(null),
                stockGrahamService.countByParams(),
                linkService.countByParams(null),
                linkService.countByParams(Dealer.AURUM_PRO),
                linkService.countByParams(Dealer.BESSERGOLD_CZ),
                linkService.countByParams(Dealer.BESSERGOLD_DE),
                linkService.countByParams(Dealer.CESKA_MINCOVNA),
                linkService.countByParams(Dealer.GOLD_A_SILVER),
                linkService.countByParams(Dealer.SILVERUM),
                linkService.countByParams(Dealer.ZLATAKY),
                linkService.countByParams(null, false),
                linkService.countByParams(Dealer.AURUM_PRO, false),
                linkService.countByParams(Dealer.BESSERGOLD_CZ, false),
                linkService.countByParams(Dealer.BESSERGOLD_DE, false),
                linkService.countByParams(Dealer.CESKA_MINCOVNA, false),
                linkService.countByParams(Dealer.GOLD_A_SILVER, false),
                linkService.countByParams(Dealer.SILVERUM, false),
                linkService.countByParams(Dealer.ZLATAKY, false)
        );

    }

    @GetMapping("/version")
    public String getProjectVersion() throws IOException {
        Properties appProps = new Properties();
        appProps.load(new FileInputStream(APP_CONFIG_PATH));

        return appProps.getProperty("project.version");
    }

    @GetMapping("/time")
    public String getServerTime() {
        return LocalDateTime.now().toString();
    }

}
