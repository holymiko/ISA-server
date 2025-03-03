package home.holymiko.investment.scraper.app.server.api.repository;

import home.holymiko.investment.scraper.app.server.type.entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    void deleteByCodeAndDate(String x, Date y);
    ExchangeRate findFirstByCodeOrderByDateDesc(String x);
}
