package home.holymiko.ScrapApp.Server.Repository;

import home.holymiko.ScrapApp.Server.Entity.Link;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LinkRepository extends JpaRepository<Link, Long> {
    List<Link> findByLink(String link);
}
