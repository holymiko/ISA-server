package home.holymiko.InvestmentScraperApp.Server.API.Repository;

import home.holymiko.InvestmentScraperApp.Server.Type.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    void deleteByUsername(String username);

//    void changePasswordById(long id, String password);

//    void changePasswordByUsername(String username, String password);

    String findByUsernameAndPassword(String username, String password);

    Optional<Account> findByUsername(String username);
}