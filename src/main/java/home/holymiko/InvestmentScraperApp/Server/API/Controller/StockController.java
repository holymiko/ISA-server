package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Service.StockGrahamService;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGraham;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/v2/stock")
@AllArgsConstructor
public class StockController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    private final StockGrahamService stockGrahamService;

    @GetMapping
    public List<StockGraham> all() {
        LOGGER.info("Get all stocks");
        return stockGrahamService.findAll();
    }

    @GetMapping("/{id}")
    public StockGraham byId(@PathVariable Long id) {
        LOGGER.info("Get by Id");
        return stockGrahamService.findById(id);
    }
}


