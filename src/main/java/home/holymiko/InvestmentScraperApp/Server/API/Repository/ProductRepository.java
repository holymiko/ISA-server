package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Dealer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Form;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Producer;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Link;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Enum.Metal;
import home.holymiko.InvestmentScraperApp.Server.DataRepresentation.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByLinks(Link link);

    Optional<Product> findByLinks_Link(String link);

    List<Product> findProductsByMetal(Metal metal);

    Optional<Product> findProductByLinks_DealerAndProducerAndMetalAndFormAndGramsAndYear(Dealer dealer, Producer producer, Metal metal, Form form, double grams, int year);

    List<Product> findProductByProducerAndMetalAndFormAndGramsAndYear(Producer producer, Metal metal, Form form, double grams, int year);

//    List<Product> findProductsByMetalAndLatestPrice_PriceIsLessThanOrderByGrams(Metal metal, double maxPrice);

//    List<Product> findProductsByMetalAndLatestPrice_PriceIsLessThanOrderByLatestPrice_Price(Metal metal, double maxPrice);

}
