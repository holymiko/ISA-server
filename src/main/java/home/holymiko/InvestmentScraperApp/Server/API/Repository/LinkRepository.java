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

    Optional<Link> findByUri(String link);

    List<Link> findByProductId(Long productId);

    @Query("select m from Link m, Product p where " +
            "(m.productId = p.id) " +
            "and " +
            "(:metal is null or p.metal = :metal) " +
            "and " +
            "(:form is null or p.form = :form) "
    )
    List<Link> findByProductParams(
            @Param("metal") Metal metal,
            @Param("form") Form form
    );

    @Query("select m from Link m where " +
            "(:productId is null or m.productId = :productId) " +
            "and " +
            "(:dealer is null or m.dealer = :dealer) "
    )
    List<Link> findByParams(
            @Param("productId") Long productId,
            @Param("dealer") Dealer dealer
    );

    @Query("select count(m) from Link m where " +
            "(:dealer is null or m.dealer = :dealer) "
    )
    Long countByParams(
            @Param("dealer") Dealer dealer
    );
}
