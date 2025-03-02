package home.holymiko.investment.scraper.app.server.api.repository;


import home.holymiko.investment.scraper.app.server.type.entity.InvestmentStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentStockRepository extends JpaRepository<InvestmentStock, Long> {
}
