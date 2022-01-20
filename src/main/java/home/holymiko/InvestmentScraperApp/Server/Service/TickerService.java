package home.holymiko.InvestmentScraperApp.Server.Service;

import home.holymiko.InvestmentScraperApp.Server.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.API.Repository.TickerRepository;
import home.holymiko.InvestmentScraperApp.Server.Utils.ConsolePrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class TickerService {
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

    @Transactional
    public boolean save(String name) {
        name = name.toUpperCase();
        Optional<Ticker> optionalTicker = this.tickerRepository.findById(name);

        if( optionalTicker.isEmpty() ) {
            this.tickerRepository.save( new Ticker(name, TickerState.UNKNOWN) );
            System.out.println("Ticker - New saved");
            return true;
        }
        System.out.println("Ticker - Already known");
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
    public void update(Ticker ticker, TickerState tickerState) {
        ticker.setTickerState(tickerState);
        this.tickerRepository.save(ticker);
    }

    @Transactional
    public void delete(Ticker ticker) {
        if( this.tickerRepository.findById(ticker.getTicker()).isEmpty() ) {
            System.out.println("Delete fail, 404");
            return;
        }
        if( ticker.getTickerState() != TickerState.UNKNOWN ) {
            System.out.println("Delete not allowed, wrong state");
            return;
        }
        this.tickerRepository.delete(ticker);
    }

    public void printTickerStatus() {
        ConsolePrinter.printTickerStatus(
                findByTickerState(TickerState.BAD).size(),
                findByTickerState(TickerState.GOOD).size(),
                findByTickerState(TickerState.NOTFOUND).size(),
                findByTickerState(TickerState.UNKNOWN).size()
        );
    }
}
