package my.sleepydeveloper.projectcompass.domain.account.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DataJpaTest
@Transactional
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    EntityManager em;

    private final String username = "username";
    private final String nickname = "nickname";
    private final String password = "password";

    @Test
    @DisplayName("findByUsername_정상")
    void findByUsername_정상() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        Optional<Account> account = accountRepository.findByUsername(username);

        // Then
        assertThat(account).isPresent();
        assertThat(account.get().getUsername()).isEqualTo(username);
        assertThat(account.get().getPassword()).isEqualTo(password);
    }

    @Test
    @DisplayName("findByUsername_실패")
    void findByUsername_실패() throws Exception {
        // Given
        createAccount(username, password, nickname);

        // When
        Optional<Account> account = accountRepository.findByUsername("fail");

        // Then
        assertThat(account).isEmpty();
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
    @DisplayName("id로_Account_존재확인_성공")
    void existsById_true() throws Exception {
        // Given
        Account account = createAccount(username, password, nickname);

        // When
        boolean existsById = accountRepository.existsById(account.getId());

        // Then
        assertThat(existsById).isTrue();
    }

    @Test
    @DisplayName("id로_Account_존재확인_실패")
    void existsById_false() throws Exception {
        // Given
        // When
        boolean existsById = accountRepository.existsById(123L);

        // Then
        assertThat(existsById).isFalse();
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

    @Test
    @DisplayName("id로 account 하나 조회_정상")
    void findById_정상() throws Exception {

        // Given
        Account account = createAccount(username, password, nickname);

        // When
        Optional<Account> findAccount = accountRepository.findById(account.getId());

        // Then
        assertThat(findAccount).isPresent();
        assertThat(findAccount.get().getId()).isEqualTo(account.getId());
        assertThat(findAccount.get().getUsername()).isEqualTo(account.getUsername());
        assertThat(findAccount.get().getPassword()).isEqualTo(account.getPassword());
        assertThat(findAccount.get().getNickname()).isEqualTo(account.getNickname());
        assertThat(findAccount.get().getRole()).isEqualTo(account.getRole());
    }

    @Test
    @DisplayName("id로 account 하나 조회실패")
    void findById_실패() throws Exception {

        // Given
        Account account = createAccount(username, password, nickname);

        // When
        Optional<Account> findAccount = accountRepository.findById(1000L);

        // Then
        assertThat(findAccount).isEmpty();
    }

    @Test
    @DisplayName("변경감지 잘 작동하는지 확인")
    void 변경감지_확인() throws Exception {
        // Given
        Account account = createAccount(username, password, nickname);

        // When
        String changeNickname = "changeNickname";
        String changePassword = "changePassword";
        account.updateProfile(changeNickname, changePassword);

        em.flush();
        em.clear();

        Optional<Account> findAccount = accountRepository.findById(account.getId());

        // Then
        assertThat(findAccount).isPresent();
        assertThat(findAccount.get().getPassword()).isEqualTo(changePassword);
        assertThat(findAccount.get().getNickname()).isEqualTo(changeNickname);
    }

    @Test
    @DisplayName("delete가 잘 되는지 확인")
    void delete_확인() throws Exception {
        // Given
        Account account = createAccount(username, password, nickname);

        // When
        accountRepository.delete(account);

        // Then
        assertThat(accountRepository.findById(account.getId())).isEmpty();
    }

    private Account createAccount(String username, String password, String nickname) {
        return accountRepository.save(new Account(username, password, nickname, "ROLE_USER"));
    }

}