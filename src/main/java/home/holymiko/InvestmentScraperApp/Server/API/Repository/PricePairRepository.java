package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.PricePair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PricePairRepository extends JpaRepository<PricePair, Long> {


    /**
     * Method used for resolving issue of Product.latestPrices vs. Product.prices
     * @param linkId
     * @return Latest PricePair for each Dealer
     */
    @Query(value = "SELECT x.* " +
            "FROM (" +
                    "SELECT MAX(p.date_time) AS First " +
                    "FROM price_pair pp, price p " +
                    "WHERE pp.link_id = :linkId and pp.sell_price_id = p.id " +
                    "GROUP BY pp.link_id " +
            ") foo " +
            "JOIN price y ON foo.First = y.date_time " +
            "JOIN price_pair x ON x.link_id = :linkId and x.sell_price_id = y.id",
            nativeQuery = true
    )
    PricePair findLatestPricePairByLinkId(
            @Param("linkId") Long linkId
    );

}
