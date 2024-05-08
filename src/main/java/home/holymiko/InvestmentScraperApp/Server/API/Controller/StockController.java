package home.holymiko.InvestmentScraperApp.Server.API.Controller;

import home.holymiko.InvestmentScraperApp.Server.API.FilePort.Import;
import home.holymiko.InvestmentScraperApp.Server.Service.StockGrahamService;
import home.holymiko.InvestmentScraperApp.Server.Service.TickerService;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGraham;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/api/v2/stock")
@AllArgsConstructor
public class StockController extends BaseController {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockController.class);

    private final StockGrahamService stockGrahamService;

    private final TickerService tickerService;

    private final Import anImport;

    @PostMapping("/tickers")
    public void importTickers() {
        if(tickerService.findAll().isEmpty()) {
            LOGGER.info("3.1) Import exported Tickers");
            try {
                anImport.importExportedTickers();
            } catch (FileNotFoundException e) {
                LOGGER.info("3.2) Import raw Tickers");
                anImport.importRawTickers();
                LOGGER.error(e.getMessage());
            }
            LOGGER.info("3) Import finished");
        } else {
            throw new IllegalArgumentException("Tickers are not empty");
        }
    }

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


