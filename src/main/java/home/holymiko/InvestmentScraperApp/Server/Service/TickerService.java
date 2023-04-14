package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.TickerRepository;
import home.holymiko.InvestmentScraperApp.Server.Core.LogBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class TickerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(TickerService.class);

    private final TickerRepository tickerRepository;

    @Autowired
    public TickerService(TickerRepository tickerRepository) {
        this.tickerRepository = tickerRepository;
    }


    ////// FIND

    public List<Ticker> findAll() { return this.tickerRepository.findAll(); }

    public Set<Ticker> findByTickerState(TickerState tickerState) { return tickerRepository.findByTickerState(tickerState); }

    public Ticker findById(String x) throws ResponseStatusException{
        Optional<Ticker> optionalTicker = this.tickerRepository.findById(x);
        if( optionalTicker.isEmpty() ) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return optionalTicker.get();
    }

    public Optional<Ticker> optionalFindById(String x) { return this.tickerRepository.findById(x); }



    ////// SAVE, UPDATE, DELETE

    /**
     * Saves new record of Ticker, with TickerState.NEW and UPPERCASE of param tickerName.
     * Avoids duplicities of tickerName in DB.
     * @param tickerName - Name of the ticker to be saved. Case insensitive
     * @return
     */
    @Transactional
    public boolean save(String tickerName) {
        tickerName = tickerName.toUpperCase();
        Optional<Ticker> optionalTicker = this.tickerRepository.findById(tickerName);

        if( optionalTicker.isEmpty() ) {
            this.tickerRepository.save( new Ticker(tickerName, TickerState.NEW) );
            LOGGER.info("Ticker -> New saved "+tickerName);
            return true;
        }
        LOGGER.info("Ticker -> Already known "+tickerName);
        return false;
    }

    @Transactional
    public boolean save(Ticker ticker) {
        if( this.tickerRepository.findById(ticker.getTicker()).isPresent() ) {
            return false;
        }
        this.tickerRepository.save(ticker);
        return true;
    }

    @Transactional
    public void saveAll(Set<Ticker> tickers) {
        this.tickerRepository.saveAll(tickers);
    }

    @Transactional
    public void update(Ticker ticker, TickerState tickerState) {
        ticker.setTickerState(tickerState);
        this.tickerRepository.save(ticker);
    }

    @Transactional
    public void delete(Ticker ticker) {
        // TODO Throw exceptions
        if( this.tickerRepository.findById(ticker.getTicker()).isEmpty() ) {
            LOGGER.error("Delete failed, Ticker was not found");
            return;
        }
        if( ticker.getTickerState() != TickerState.NEW ) {
            LOGGER.error("Delete not allowed, wrong state");
            return;
        }
        this.tickerRepository.delete(ticker);
    }

    public void printTickerStatus() {
        LogBuilder.printTickerStatus(
                findByTickerState(TickerState.GOOD).size(),
                findByTickerState(TickerState.BAD).size(),
                findByTickerState(TickerState.NOTFOUND).size(),
                findByTickerState(TickerState.NEW).size()
        );
    }
}
