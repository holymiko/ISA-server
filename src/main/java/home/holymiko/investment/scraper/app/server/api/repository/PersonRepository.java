package home.holymiko.investment.scraper.app.server.api.repository;

import home.holymiko.investment.scraper.app.server.type.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}