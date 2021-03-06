package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByUrl(String link);

//    List<Link> findByMetal(Metal metal);

    List<Link> findByDealer(Dealer dealer);

//    List<Link> findByProducer(Producer producer);

    Optional<Link> findByDealerAndProduct_Id(Dealer dealer, long product);
}
