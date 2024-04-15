package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePairHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PricePairHistoryRepository extends JpaRepository<PricePairHistory, Long> {

}
