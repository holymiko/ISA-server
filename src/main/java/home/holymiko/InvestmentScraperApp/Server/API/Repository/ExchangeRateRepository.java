package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

    void deleteByCodeAndDate(String x, Date y);

    ExchangeRate getByDateAndCode(Date x, String y);

    ExchangeRate findFirstByCodeOrderByDateDesc(String x);
}
