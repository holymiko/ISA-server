package home.holymiko.InvestmentScraperApp.Server.API.Repository;


import home.holymiko.InvestmentScraperApp.Server.Type.Entity.InvestmentMetal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentMetalRepository extends JpaRepository<InvestmentMetal, Long> {
}
