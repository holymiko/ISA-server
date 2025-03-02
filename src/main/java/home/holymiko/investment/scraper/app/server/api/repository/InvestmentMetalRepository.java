package home.holymiko.investment.scraper.app.server.api.repository;


import home.holymiko.investment.scraper.app.server.type.entity.InvestmentMetal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentMetalRepository extends JpaRepository<InvestmentMetal, Long> {
}
