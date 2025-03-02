package home.holymiko.investment.scraper.app.server.api.repository;

import home.holymiko.investment.scraper.app.server.type.enums.TickerState;
import home.holymiko.investment.scraper.app.server.type.entity.Ticker;
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
