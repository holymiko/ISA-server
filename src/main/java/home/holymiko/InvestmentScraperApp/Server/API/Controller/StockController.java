package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Service.GrahamStockService;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/stock")
public class StockController {

    private final GrahamStockService grahamStockService;

    @Autowired
    public StockController(GrahamStockService grahamStockService) {
        this.grahamStockService = grahamStockService;
    }

    @GetMapping({"/", ""})
    public List<GrahamStock> all() {
        // TODO Logging
        System.out.println("Get all stocks");
        return grahamStockService.findAll();
    }

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<GrahamStock> byId(@PathVariable int id) {
        // TODO Logging
        System.out.println("Get by Id");
        return grahamStockService.findById(id);
    }
}
