package home.holymiko.investment.scraper.app.server.service;

import home.holymiko.investment.scraper.app.server.api.repository.StockGrahamHistoryRepository;
import home.holymiko.investment.scraper.app.server.core.exception.ResourceNotFoundException;
import home.holymiko.investment.scraper.app.server.mapper.StockGrahamMapper;
import home.holymiko.investment.scraper.app.server.type.entity.StockGraham;
import home.holymiko.investment.scraper.app.server.type.enums.GrahamGrade;
import home.holymiko.investment.scraper.app.server.type.entity.Ticker;
import home.holymiko.investment.scraper.app.server.api.repository.StockGrahamRepository;
import lombok.AllArgsConstructor;
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

    public Long countByParams() {
        return this.stockGrahamRepository.countByParams(null, null, null, null);
    }

    // TODO Return DTO
    public StockGraham findById(Long id) {
        Optional<StockGraham> optional = this.stockGrahamRepository.findById(id);
        if(optional.isEmpty()) {
            throw new ResourceNotFoundException("StockGraham with ID "+id+" was not found");
        }
        return optional.get();
    }

    public List<StockGraham> findAll() {
        return this.stockGrahamRepository.findAll();
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
