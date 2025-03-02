package home.holymiko.investment.scraper.app.server.api.repository;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import home.holymiko.investment.scraper.app.server.type.entity.Link;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
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

    @Query("select count(m) from Link m where " +
            "(:dealer is null or m.dealer = :dealer) " +
            "and " +
            "(:hasProduct = false or m.productId is not null)" +
            "and " +
            "(:hasProduct = true or m.productId is null)"
    )
    Long countByParams(
            @Param("dealer") Dealer dealer,
            @Param("hasProduct") boolean hasProduct
    );
}
