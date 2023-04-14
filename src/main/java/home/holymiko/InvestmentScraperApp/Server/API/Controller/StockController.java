package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Service.GrahamStockService;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStock;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    private final GrahamStockService grahamStockService;

    @GetMapping({"/", ""})
    public List<GrahamStock> all() {
        LOGGER.info("Get all stocks");
        return grahamStockService.findAll();
    }

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<GrahamStock> byId(@PathVariable int id) {
        LOGGER.info("Get by Id");
        return grahamStockService.findById(id);
    }
}


