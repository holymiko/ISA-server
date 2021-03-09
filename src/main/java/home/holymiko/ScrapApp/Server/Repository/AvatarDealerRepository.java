package home.holymiko.ScrapApp.Server.Repository;


import home.holymiko.ScrapApp.Server.Entity.AvatarDealer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarDealerRepository extends JpaRepository<AvatarDealer, Long> {


}
