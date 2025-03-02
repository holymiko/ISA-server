package home.holymiko.investment.scraper.app.server.api.controller;

import home.holymiko.investment.scraper.app.server.service.AppPropsService;
import home.holymiko.investment.scraper.app.server.service.LinkService;
import home.holymiko.investment.scraper.app.server.service.PriceService;
import home.holymiko.investment.scraper.app.server.service.ProductService;
import home.holymiko.investment.scraper.app.server.service.RateService;
import home.holymiko.investment.scraper.app.server.service.StockGrahamService;
import home.holymiko.investment.scraper.app.server.service.TickerService;
import home.holymiko.investment.scraper.app.server.type.dto.simple.StatsDTO;
import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v2/info")
@AllArgsConstructor
public class AppInfoController extends BaseController {

    private final AppPropsService appPropsService;
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
                productService.countByParams(null, null, null, null, null, null, null, null),
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
                linkService.countByParams(null, true),
                linkService.countByParams(Dealer.AURUM_PRO, true),
                linkService.countByParams(Dealer.BESSERGOLD_CZ, true),
                linkService.countByParams(Dealer.BESSERGOLD_DE, true),
                linkService.countByParams(Dealer.CESKA_MINCOVNA, true),
                linkService.countByParams(Dealer.GOLD_A_SILVER, true),
                linkService.countByParams(Dealer.SILVERUM, true),
                linkService.countByParams(Dealer.ZLATAKY, true)
        );

    }

    @GetMapping("/version")
    public String getProjectVersion() throws IOException {
        return appPropsService.getAppProperty("project.version");
    }

    @GetMapping("/time")
    public String getServerTime() {
        return LocalDateTime.now().toString();
    }

}
