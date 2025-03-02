package home.holymiko.investment.scraper.app.server.api.repository;

import home.holymiko.investment.scraper.app.server.type.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PriceRepository extends JpaRepository<Price, LocalDateTime> {
}
