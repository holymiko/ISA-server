package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.Core.annotation.ResourceNotFound;
import home.holymiko.InvestmentScraperApp.Server.Service.StockService;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.advanced.PortfolioDTO_ProductDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.DTO.simple.PortfolioDTO;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Stock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v2/stock")
public class StockController {

    private final StockService stockService;

    @Autowired
    public StockController(StockService stockService) {
        this.stockService = stockService;
    }

    @GetMapping({"/", ""})
    public List<Stock> all() {
        // TODO Logging
        System.out.println("Get all stocks");
        return stockService.findAll();
    }

    @ResourceNotFound
    @GetMapping("/id/{id}")
    public Optional<Stock> byId(@PathVariable int id) {
        // TODO Logging
        System.out.println("Get by Id");
        return stockService.findById(id);
    }
}
