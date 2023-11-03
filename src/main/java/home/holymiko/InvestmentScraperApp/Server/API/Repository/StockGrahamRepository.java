package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.StockGraham;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import home.holymiko.InvestmentScraperApp.Server.Type.Enum.TickerState;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockGrahamRepository extends JpaRepository<StockGraham, Long> {

    @Query("select count(s) from StockGraham s where " +
            "(:grahamGrade is null or s.grahamGrade = :grahamGrade) " +
            "and " +
            "(:intrinsicValue is null or s.intrinsicValue = :intrinsicValue) " +
            "and " +
            "(:ratingScore is null or s.ratingScore = :ratingScore) " +
            "and " +
            "(:currency is null or s.currency = :currency) "
    )
    Long countByParams(
            @Param("grahamGrade") GrahamGrade grahamGrade,
            @Param("intrinsicValue") Double intrinsicValue,
            @Param("ratingScore") Double ratingScore,
            @Param("currency") String currency
    );
    Optional<StockGraham> findByTicker(Ticker ticker);

    Optional<StockGraham> findByTicker_Ticker(String ticker);


    @Query("select s from StockGraham s where " +
            "(:grahamGrade is null or s.grahamGrade = :grahamGrade) " +
            "and " +
            "(:intrinsicValue is null or s.intrinsicValue = :intrinsicValue) " +
            "and " +
            "(:ratingScore is null or s.ratingScore = :ratingScore) " +
            "and " +
            "(:currency is null or s.currency = :currency) "
    )
    List<StockGraham> findByParams(
            @Param("grahamGrade") GrahamGrade grahamGrade,
            @Param("intrinsicValue") Double intrinsicValue,
            @Param("ratingScore") Double ratingScore,
            @Param("currency") String currency
    );

    void deleteByTicker(Ticker ticker);

}
