package my.sleepydeveloper.projectcompass.domain.account.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import my.sleepydeveloper.projectcompass.domain.account.entity.KakaoAccount;

public interface KakaoAccountRepository extends JpaRepository<KakaoAccount, Long> {
	
	Optional<KakaoAccount> findByKakaoId(Long kakaoId);
}
