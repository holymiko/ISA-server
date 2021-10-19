package home.holymiko.InvestmentScraperApp.Server.Repository;

import home.holymiko.InvestmentScraperApp.Server.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, String> {

    Set<Ticker> findByTickerState(TickerState tickerState);

}
