package home.holymiko.ScrapApp.Server.Repository;

import home.holymiko.ScrapApp.Server.Entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface PriceRepository extends JpaRepository<Price, LocalDateTime> {
}
