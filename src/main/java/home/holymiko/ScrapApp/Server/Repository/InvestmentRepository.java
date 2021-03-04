package home.holymiko.ScrapApp.Server.Repository;


import home.holymiko.ScrapApp.Server.Entity.Investment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<Investment, Long> {


}
