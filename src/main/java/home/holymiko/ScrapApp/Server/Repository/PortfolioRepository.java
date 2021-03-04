package home.holymiko.ScrapApp.Server.Repository;

import home.holymiko.ScrapApp.Server.Entity.Portfolio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    Optional<Portfolio> findByOwner(String owner);
}
