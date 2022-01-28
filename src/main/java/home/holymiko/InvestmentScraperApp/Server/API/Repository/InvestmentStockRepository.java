package home.holymiko.InvestmentScraperApp.Server.API.Repository;


import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.InvestmentStock;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentStockRepository extends JpaRepository<InvestmentStock, Long> {
}
