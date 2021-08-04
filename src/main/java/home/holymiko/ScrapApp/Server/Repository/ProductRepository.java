package home.holymiko.ScrapApp.Server.Repository;

import home.holymiko.ScrapApp.Server.Enum.Dealer;
import home.holymiko.ScrapApp.Server.Enum.Form;
import home.holymiko.ScrapApp.Server.Enum.Producer;
import home.holymiko.ScrapApp.Server.Entity.Link;
import home.holymiko.ScrapApp.Server.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Year;
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
