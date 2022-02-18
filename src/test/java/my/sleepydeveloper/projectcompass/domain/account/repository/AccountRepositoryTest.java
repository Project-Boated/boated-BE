package my.sleepydeveloper.projectcompass.domain.account.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import javax.persistence.EntityNotFoundException;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    private final String username = "username";
    private final String nickname = "nickname";
    private final String password = "password";

    @Test
    @DisplayName("findByUsername_정상")
    void findByUsername_정상() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Account를 찾을 수 없습니다."));

        // Then
        assertThat(account.getUsername()).isEqualTo(username);
        assertThat(account.getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("findByUsername_실패")
    void findByUsername_실패() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        Optional<Account> account = accountRepository.findByUsername("fail");

        // Then
        assertThatThrownBy(() -> account.get()).isInstanceOf(NoSuchElementException.class);
    }

    @Test
    @DisplayName("유저이름으로_Account_존재확인_없음")
    void existsByUsername_true() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        boolean existsByUsername = accountRepository.existsByUsername("fail");

        // Then
        assertThat(existsByUsername).isFalse();
    }

    @Test
    @DisplayName("유저이름으로_Account_존재확인_실패")
    void existsByUsername_false() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        boolean existsByUsername = accountRepository.existsByUsername(username);

        // Then
        assertThat(existsByUsername).isTrue();
    }

    @Test
    @DisplayName("Username으로 Account 하나 조회")
    void findByUsername() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        Optional<Account> account = accountRepository.findByUsername(username);

        // Then
        assertThat(account).isPresent();
    }

    private Account createAccount(String username, String password, String nickname) {
        return accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
    }

}