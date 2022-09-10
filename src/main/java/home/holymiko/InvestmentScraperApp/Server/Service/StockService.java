package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Stock;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    ////// FIND

    public Optional<Stock> findById(int id) {
        return this.stockRepository.findById(id);
    }

    public List<Stock> findAll() {
        return this.stockRepository.findAll();
    }

    public Optional<Stock> findByTicker(Ticker ticker) {
        return this.stockRepository.findByTicker(ticker);
    }

    public Set<Stock> findByTicker(Set<Ticker> tickers) {
        Set<Stock> stocks = new HashSet<>();

        for (Ticker ticker : tickers) {
            stockRepository.findByTicker(ticker).ifPresent(
                    stocks::add
            );
        }

        return stocks;
    }

    /**
     * Null parameters are ignored in the query
     * TODO Test this method
     * @return Stocks corresponding with all given parameters
     */
    public List<Stock> findStocksByParams(
            @Nullable GrahamGrade grahamGrade,
            @Nullable Double intrinsicValue,
            @Nullable Double ratingScore,
            @Nullable String currency) {
        return this.stockRepository.findByParams(grahamGrade, intrinsicValue, ratingScore, currency);
    }


    ////// SAVE

    @Transactional
    public void save(Stock stock) {
        this.stockRepository.deleteByTicker(stock.getTicker());
        this.stockRepository.save(stock);
    }

    @Transactional
    public void deleteByTicker(Ticker ticker) {
        this.stockRepository.deleteByTicker(ticker);
    }

}
