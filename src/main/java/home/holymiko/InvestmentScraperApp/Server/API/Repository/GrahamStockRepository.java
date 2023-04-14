package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.GrahamStock;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GrahamStockRepository extends JpaRepository<GrahamStock, Integer> {

    Optional<GrahamStock> findByTicker(Ticker ticker);

    Optional<GrahamStock> findByTicker_Ticker(String ticker);


    @Query("select s from GrahamStock s where " +
            "(:grahamGrade is null or s.grahamGrade = :grahamGrade) " +
            "and " +
            "(:intrinsicValue is null or s.intrinsicValue = :intrinsicValue) " +
            "and " +
            "(:ratingScore is null or s.ratingScore = :ratingScore) " +
            "and " +
            "(:currency is null or s.currency = :currency) "
    )
    List<GrahamStock> findByParams(
            @Param("grahamGrade") GrahamGrade grahamGrade,
            @Param("intrinsicValue") Double intrinsicValue,
            @Param("ratingScore") Double ratingScore,
            @Param("currency") String currency
    );

    void deleteByTicker(Ticker ticker);

}
