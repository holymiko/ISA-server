package home.holymiko.InvestmentScraperApp.Server.Repository;

import home.holymiko.InvestmentScraperApp.Server.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Entity.Stock;
import home.holymiko.InvestmentScraperApp.Server.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.Enum.TickerState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByGrahamGrade(GrahamGrade grahamGrade);

    List<Stock> findByIntrinsicValue(Double x);

    Optional<Stock> findByTicker(Ticker ticker);

    List<Stock>  findByRatingScore(Double x);

    List<Stock> findByCurrency(String x);

    void deleteByTicker(Ticker ticker);

}
