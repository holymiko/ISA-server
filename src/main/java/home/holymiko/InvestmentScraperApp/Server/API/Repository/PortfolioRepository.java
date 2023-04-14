package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {

}
