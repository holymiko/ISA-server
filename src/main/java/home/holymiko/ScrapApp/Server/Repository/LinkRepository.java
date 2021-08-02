package home.holymiko.ScrapApp.Server.Repository;

import home.holymiko.ScrapApp.Server.Entity.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.Link;
import home.holymiko.ScrapApp.Server.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    List<Link> findByLink(String link);

//    List<Link> findByMetal(Metal metal);

    List<Link> findByDealer(Dealer dealer);

//    List<Link> findByProducer(Producer producer);

    Optional<Link> findByDealerAndProduct_Id(Dealer dealer, long product);
}
