package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Enum.GrahamGrade;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Stock;
import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Ticker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<Stock, Integer> {

    Optional<Stock> findByTicker(Ticker ticker);

    Optional<Stock> findByTicker_Ticker(String ticker);


    @Query("select s from Stock s where " +
            "(:grahamGrade is null or s.grahamGrade = :grahamGrade) " +
            "and " +
            "(:intrinsicValue is null or s.intrinsicValue = :intrinsicValue) " +
            "and " +
            "(:ratingScore is null or s.ratingScore = :ratingScore) " +
            "and " +
            "(:currency is null or s.currency = :currency) "
    )
    List<Stock> findByParams(
            @Param("grahamGrade") GrahamGrade grahamGrade,
            @Param("intrinsicValue") Double intrinsicValue,
            @Param("ratingScore") Double ratingScore,
            @Param("currency") String currency
    );

    void deleteByTicker(Ticker ticker);

}
