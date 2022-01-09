package home.holymiko.InvestmentScraperApp.Server.Repository;

import home.holymiko.InvestmentScraperApp.Server.Entity.ExchangeRate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {


}
