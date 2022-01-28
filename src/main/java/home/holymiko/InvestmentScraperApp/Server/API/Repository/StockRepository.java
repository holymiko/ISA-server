package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Stock;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    List<Stock> findByGrahamGrade(GrahamGrade grahamGrade);

    List<Stock> findByIntrinsicValue(Double x);

    Optional<Stock> findByTicker(Ticker ticker);

    Optional<Stock> findByTicker_Ticker(String ticker);

    List<Stock>  findByRatingScore(Double x);

    List<Stock> findByCurrency(String x);

    void deleteByTicker(Ticker ticker);

}
