package home.holymiko.ScrapApp.Server.Repository;


import home.holymiko.ScrapApp.Server.Entity.AvatarProducer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarProducerRepository extends JpaRepository<AvatarProducer, Long> {


}
