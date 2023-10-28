package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.RateService;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.ExchangeRate;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/v2/rate")
@AllArgsConstructor
public class RateController extends BaseController {
    private final RateService rateService;

    /////// GET

    // TODO Return DTO
    // TODO Add metal rates

    @GetMapping("/currency")
    public List<ExchangeRate> getCommonExchangeRates() {
        return Arrays.asList(
                this.rateService.findExchangeRate("EUR"),
                this.rateService.findExchangeRate("USD")
        );
    }

}
