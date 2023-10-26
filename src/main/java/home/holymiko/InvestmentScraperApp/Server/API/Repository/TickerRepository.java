package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface TickerRepository extends JpaRepository<Ticker, String> {

    Set<Ticker> findByTickerState(TickerState tickerState);

    @Query("select count(m) from Ticker m where " +
            "(:tickerState is null or m.tickerState = :tickerState) "
    )
    Long countByParams(@Param("tickerState") TickerState tickerState);

}
