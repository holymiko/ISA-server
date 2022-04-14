package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {

    Optional<Link> findByUrl(String link);

    List<Link> findByProduct_Id(Long productId);
//    List<Link> findByMetal(Metal metal);

    List<Link> findByDealer(Dealer dealer);

    // TODO Check functioning
    @Query("select m from Link m where " +
            "(:dealer is null or m.dealer = :dealer) " +
            "and " +
            "(:metal is null or m.product is null or m.product.metal = :metal) " +
            "and " +
            "(:form is null or m.product is null or m.product.form = :form) " +
            "and " +
            "(:url is null or m.url = :url) "
    )
    List<Link> findByParams(
            @Param("dealer") Dealer dealer,
            @Param("metal") Metal metal,
            @Param("form") Form form,
            @Param("url") String url
    );

//    List<Link> findByProducer(Producer producer);

    Optional<Link> findByDealerAndProduct_Id(Dealer dealer, long product);
}
