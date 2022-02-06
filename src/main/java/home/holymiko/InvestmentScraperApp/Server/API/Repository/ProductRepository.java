package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataFormat.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByLinks(Link link);

    Optional<Product> findByLinks_Url(String link);

    List<Product> findProductsByMetal(Metal metal);

    Optional<Product> findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer dealer, Producer producer, Metal metal, Form form, double grams, int year);

    @Query("select m from Product m where " +
            "(:dealer is null or m.id IN (SELECT l.product.id FROM Link l WHERE l.dealer = :dealer )) " +
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
            "(:isSpecial is null or m.isSpecial = :isSpecial) "
    )
    List<Product> findByParams(
            @Param("dealer") Dealer dealer,
            @Param("producer") Producer producer,
            @Param("metal") Metal metal,
            @Param("form") Form form,
            @Param("grams") Double grams,
            @Param("year") Integer year,
            @Param("isSpecial") Boolean isSpecial
    );

}
