package my.sleepydeveloper.projectcompass.domain.account.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByNickname(String nickname);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);
}
