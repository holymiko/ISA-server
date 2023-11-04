package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
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
            "(:saveAlone is null or m.saveAlone = :saveAlone) "
    )
    List<Product> findByParams(
            @Param("dealer") Dealer dealer,
            @Param("producer") Producer producer,
            @Param("metal") Metal metal,
            @Param("form") Form form,
            @Param("grams") Double grams,
            @Param("year") Integer year,
            @Param("saveAlone") Boolean saveAlone,
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
            "(:saveAlone is null or m.saveAlone = :saveAlone) "
    )
    Long countByParams(
            @Param("dealer") Dealer dealer,
            @Param("producer") Producer producer,
            @Param("metal") Metal metal,
            @Param("form") Form form,
            @Param("grams") Double grams,
            @Param("year") Integer year,
            @Param("saveAlone") Boolean saveAlone
    );
}
