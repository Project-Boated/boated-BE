package com.projectboated.backend.domain.account.repository;

import com.projectboated.backend.domain.account.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> findByUsername(String username);

    @Query("select a from Account a where a.nickname=:nickname")
    Optional<Account> findByNickname(@Param("nickname") String nickname);

    boolean existsByUsername(String username);

    boolean existsByNickname(String nickname);
}
