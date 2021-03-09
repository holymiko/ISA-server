package home.holymiko.ScrapApp.Server.Repository;

import home.holymiko.ScrapApp.Server.Entity.Link;
import home.holymiko.ScrapApp.Server.Entity.Enum.Metal;
import home.holymiko.ScrapApp.Server.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByLink(Link link);

    List<Product> findByLink_Link(String link);

    List<Product> findProductsByMetal(Metal metal);

    List<Product> findProductsByMetalAndLatestPrice_PriceIsLessThanOrderByGrams(Metal metal, double maxPrice);

    List<Product> findProductsByMetalAndLatestPrice_PriceIsLessThanOrderByLatestPrice_Price(Metal metal, double maxPrice);

    List<Product> findProductsByMetalAndLatestPrice_PriceIsLessThanOrderByLatestPrice_Split(Metal metal, double maxPrice);

    List<Product> findProductsByMetalAndLatestPrice_PriceIsLessThanOrderByLatestPrice_PricePerGram(Metal metal, double maxPrice);
}
