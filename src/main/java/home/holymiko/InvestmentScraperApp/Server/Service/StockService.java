package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Stock;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
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

    public Optional<Stock> findByTickerState(Ticker ticker) {
        return this.stockRepository.findByTicker(ticker);
    }

    public Set<Stock> findByTickerState(Set<Ticker> tickers) {
        Set<Stock> stocks = new HashSet<>();

        for (Ticker ticker : tickers) {
            stockRepository.findByTicker(ticker).ifPresent(
                    stocks::add
            );
        }

        return stocks;
    }

    public List<Stock> findByGrahamGrade(GrahamGrade grahamGrade) {
        return this.stockRepository.findByGrahamGrade(grahamGrade);
    }

    public List<Stock> findByIntrinsicValue(double x) {
        return this.stockRepository.findByIntrinsicValue(x);
    }

    public List<Stock> findAll() {
        return this.stockRepository.findAll();
    }

    public List<Stock> findByRating(Double x) {
        return this.stockRepository.findByRatingScore(x);
    }

    public List<Stock> findByCurrency(String x) {
        return this.stockRepository.findByCurrency(x);
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
