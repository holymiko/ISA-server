package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.API.Repository.GrahamStockHistoryRepository;
import home.holymiko.InvestmentScraperApp.Server.Mapper.GrahamStockMapper;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStock;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStockHistory;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.GrahamStockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class GrahamStockService {

    private final GrahamStockRepository grahamStockRepository;
    private final GrahamStockMapper grahamStockMapper;
    private final GrahamStockHistoryRepository grahamStockHistoryRepository;

    @Autowired
    public GrahamStockService(GrahamStockRepository grahamStockRepository, GrahamStockMapper grahamStockMapper, GrahamStockHistoryRepository grahamStockHistoryRepository) {
        this.grahamStockRepository = grahamStockRepository;
        this.grahamStockMapper = grahamStockMapper;
        this.grahamStockHistoryRepository = grahamStockHistoryRepository;
    }

    ////// FIND

    public Optional<GrahamStock> findById(int id) {
        return this.grahamStockRepository.findById(id);
    }

    public List<GrahamStock> findAll() {
        return this.grahamStockRepository.findAll();
    }

    public Optional<GrahamStock> findByTicker(Ticker ticker) {
        return this.grahamStockRepository.findByTicker(ticker);
    }

    public Set<GrahamStock> findByTicker(Set<Ticker> tickers) {
        Set<GrahamStock> grahamStocks = new HashSet<>();

        for (Ticker ticker : tickers) {
            grahamStockRepository.findByTicker(ticker).ifPresent(
                    grahamStocks::add
            );
        }

        return grahamStocks;
    }

    /**
     * Null parameters are ignored in the query
     * TODO Test this method
     * @return Stocks corresponding with all given parameters
     */
    public List<GrahamStock> findStocksByParams(
            @Nullable GrahamGrade grahamGrade,
            @Nullable Double intrinsicValue,
            @Nullable Double ratingScore,
            @Nullable String currency) {
        return this.grahamStockRepository.findByParams(grahamGrade, intrinsicValue, ratingScore, currency);
    }


    ////// SAVE

    @Transactional
    public void save(GrahamStock grahamStock) {
        this.grahamStockRepository.deleteByTicker(grahamStock.getTicker());
        this.grahamStockRepository.save(grahamStock);
        // Parallel save to history table
        this.grahamStockHistoryRepository.save(
                grahamStockMapper.toGrahamStockHistory(grahamStock)
        );
    }

    @Transactional
    public void deleteByTicker(Ticker ticker) {
        this.grahamStockRepository.deleteByTicker(ticker);
    }


}
