package home.holymiko.ScrapApp.Server.Service;

import home.holymiko.ScrapApp.Server.Entity.Enum.GrahamGrade;
import home.holymiko.ScrapApp.Server.Entity.Enum.TickerState;
import home.holymiko.ScrapApp.Server.Entity.Stock;
import home.holymiko.ScrapApp.Server.Entity.Ticker;
import home.holymiko.ScrapApp.Server.Repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class StockService {

    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }


    /////////// FIND

    public Optional<Stock> findById(int id) {
        return this.stockRepository.findById(id);
    }

    public Optional<Stock> findByTicker(Ticker ticker) {
        return this.stockRepository.findByTicker(ticker);
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


    /////////// SAVE

    @Transactional
    public void save(Stock stock) {
        if( this.stockRepository.findByTicker(stock.getTicker()).isPresent() ) {
            if( !(stock.getTicker().getTickerState().equals(TickerState.GOOD)) ) {
                System.out.println("\nWARNING: ");
                System.out.println("Stock: "+stock.getName()+"  and Ticker: "+stock.getTicker().getTicker()+" are no synchronized\n");
            }
            return;
        }
        this.stockRepository.save(stock);
    }

}
