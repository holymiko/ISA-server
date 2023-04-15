package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.StockGrahamHistoryRepository;
import home.holymiko.InvestmentScraperApp.Server.Mapper.StockGrahamMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGraham;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.StockGrahamRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@AllArgsConstructor
public class StockGrahamService {

    private final StockGrahamRepository stockGrahamRepository;
    private final StockGrahamMapper stockGrahamMapper;
    private final StockGrahamHistoryRepository stockGrahamHistoryRepository;

    ////// FIND

    public Optional<StockGraham> findById(int id) {
        return this.stockGrahamRepository.findById(id);
    }

    public List<StockGraham> findAll() {
        return this.stockGrahamRepository.findAll();
    }

    public Optional<StockGraham> findByTicker(Ticker ticker) {
        return this.stockGrahamRepository.findByTicker(ticker);
    }

    public Set<StockGraham> findByTicker(Set<Ticker> tickers) {
        Set<StockGraham> stockGrahams = new HashSet<>();

        for (Ticker ticker : tickers) {
            stockGrahamRepository.findByTicker(ticker).ifPresent(
                    stockGrahams::add
            );
        }

        return stockGrahams;
    }

    /**
     * Null parameters are ignored in the query
     * TODO Test this method
     * @return Stocks corresponding with all given parameters
     */
    public List<StockGraham> findStocksByParams(
            @Nullable GrahamGrade grahamGrade,
            @Nullable Double intrinsicValue,
            @Nullable Double ratingScore,
            @Nullable String currency) {
        return this.stockGrahamRepository.findByParams(grahamGrade, intrinsicValue, ratingScore, currency);
    }


    ////// SAVE

    @Transactional
    public void save(StockGraham stockGraham) {
        this.stockGrahamRepository.deleteByTicker(stockGraham.getTicker());
        this.stockGrahamRepository.save(stockGraham);
        // Parallel save to history table
        this.stockGrahamHistoryRepository.save(
                stockGrahamMapper.toGrahamStockHistory(stockGraham)
        );
    }

    @Transactional
    public void deleteByTicker(Ticker ticker) {
        this.stockGrahamRepository.deleteByTicker(ticker);
    }


}
