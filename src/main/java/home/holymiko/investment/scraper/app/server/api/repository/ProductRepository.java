package home.holymiko.investment.scraper.app.server.api.repository;

import home.holymiko.investment.scraper.app.server.type.enums.Dealer;
import home.holymiko.investment.scraper.app.server.type.enums.Form;
import home.holymiko.investment.scraper.app.server.type.enums.Producer;
import home.holymiko.investment.scraper.app.server.type.enums.Metal;
import home.holymiko.investment.scraper.app.server.type.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer dealer, Producer producer, Metal metal, Form form, double grams, int year);

    @Query("select m from Product m where " +
            "(:dealer is null or m.id IN (SELECT l.productId FROM Link l WHERE l.dealer = :dealer )) " +
            "and " +
            "(:producer is null or m.producer = :producer) " +
            "and " +
            "(:metal is null or m.metal = :metal) " +
            "and " +
            "(:form is null or m.form = :form) " +
            "and " +
            "(:grams is null or m.grams = :grams) " +
            "and " +
            "(:year is null or m.year = :year) " +
            "and " +
            "(:hidden is null or m.isHidden = :hidden) " +
            "and " +
            "(:isTopProduct is null or m.isTopProduct = :isTopProduct) "
    )
    Page<Product> findByParams(
            @Param("dealer") Dealer dealer,
            @Param("producer") Producer producer,
            @Param("metal") Metal metal,
            @Param("form") Form form,
            @Param("grams") Double grams,
            @Param("year") Integer year,
            @Param("hidden") Boolean hidden,
            @Param("isTopProduct") Boolean isTopProduct,
            Pageable pageable
    );

    @Query("select count(m) from Product m where " +
            "(:dealer is null or m.id IN (SELECT l.productId FROM Link l WHERE l.dealer = :dealer )) " +
            "and " +
            "(:producer is null or m.producer = :producer) " +
            "and " +
            "(:metal is null or m.metal = :metal) " +
            "and " +
            "(:form is null or m.form = :form) " +
            "and " +
            "(:grams is null or m.grams = :grams) " +
            "and " +
            "(:year is null or m.year = :year) " +
            "and " +
            "(:hidden is null or m.isHidden = :hidden) " +
            "and " +
            "(:isTopProduct is null or m.isTopProduct = :isTopProduct) "
    )
    Long countByParams(
            @Param("dealer") Dealer dealer,
            @Param("producer") Producer producer,
            @Param("metal") Metal metal,
            @Param("form") Form form,
            @Param("grams") Double grams,
            @Param("year") Integer year,
            @Param("hidden") Boolean hidden,
            @Param("isTopProduct") Boolean isTopProduct
    );
}
