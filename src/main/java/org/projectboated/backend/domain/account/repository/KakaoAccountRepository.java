package org.projectboated.backend.domain.account.repository;

import org.projectboated.backend.domain.account.entity.KakaoAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface KakaoAccountRepository extends JpaRepository<KakaoAccount, Long> {

	@Query("select ka from KakaoAccount ka left join fetch ka.roles")
	Optional<KakaoAccount> findByKakaoId(Long kakaoId);
}
