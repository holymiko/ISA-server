package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.Dealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PricePairRepository extends JpaRepository<PricePair, Long> {


    /**
     * Method used for resolving issue of Product.latestPrices vs. Product.prices
     * @param productId
     * @return Latest PricePair for each Dealer
     */
    @Query(value = "SELECT x.* " +
            "FROM (" +
                    "SELECT pp.dealer, MAX(p.date_time) AS First " +
                    "FROM price_pair pp, Price p " +
                    "WHERE pp.product_id = :productId and pp.sell_price_id = p.id " +
                    "GROUP BY pp.dealer " +
                   ") foo " +
            "JOIN price_pair x ON foo.dealer = x.dealer and x.product_id = :productId " +
            "JOIN price y ON foo.First = y.date_time and x.sell_price_id = y.id ",
            nativeQuery = true
    )
    List<PricePair> findLatestPricePairsByProductId(
            @Param("productId") Long productId
    );


//    Optional<PricePair> findByProduct_NameAndDealer(String productName, Dealer dealer);
}
