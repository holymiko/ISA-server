package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Service.StockGrahamService;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGraham;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/stock")
@AllArgsConstructor
public class StockController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    private final StockGrahamService stockGrahamService;

    @GetMapping({"/", ""})
    public List<StockGraham> all() {
        LOGGER.info("Get all stocks");
        return stockGrahamService.findAll();
    }

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<StockGraham> byId(@PathVariable int id) {
        LOGGER.info("Get by Id");
        return stockGrahamService.findById(id);
    }
}


