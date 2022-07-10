package com.projectboated.backend.domain.account.account.repository;

import com.projectboated.backend.domain.account.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    Optional<Account> findByNickname(String nickname);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);
}
