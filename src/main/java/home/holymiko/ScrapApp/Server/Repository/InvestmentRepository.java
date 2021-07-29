package home.holymiko.ScrapApp.Server.Repository;


import home.holymiko.ScrapApp.Server.Entity.InvestmentMetal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvestmentRepository extends JpaRepository<InvestmentMetal, Long> {


}
