package com.projectboated.backend.domain.account.repository;

import com.projectboated.backend.common.data.BasicAccountData;
import com.projectboated.backend.domain.account.entity.Account;
import com.projectboated.backend.common.basetest.BaseTest;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Disabled
class AccountRepositoryTest extends BaseTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    void findByUsername_가입된유저찾기_성공() throws Exception {
        // Given
        accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        Optional<Account> account = accountRepository.findByUsername(BasicAccountData.USERNAME);

        // Then
        assertThat(account).isPresent();
        AssertionsForClassTypes.assertThat(account.get().getUsername()).isEqualTo(BasicAccountData.USERNAME);
        AssertionsForClassTypes.assertThat(account.get().getPassword()).isEqualTo(BasicAccountData.PASSWORD);
        AssertionsForClassTypes.assertThat(account.get().getNickname()).isEqualTo(BasicAccountData.NICKNAME);
        AssertionsForClassTypes.assertThat(account.get().getProfileImage()).isEqualTo(BasicAccountData.PROFILE_IMAGE_FILE);
        AssertionsForClassTypes.assertThat(account.get().getRoles()).isEqualTo(BasicAccountData.ROLES);
    }

    @Test
    void findByUsername_없는유저찾기_Empty() throws Exception {
        // Given
        // When
        Optional<Account> account = accountRepository.findByUsername(BasicAccountData.USERNAME);

        // Then
        assertThat(account).isEmpty();
    }

    @Test
    void existsByUsername_가입된유저찾기_true() throws Exception {
        // Given
        accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        boolean existsByUsername = accountRepository.existsByUsername(BasicAccountData.USERNAME);

        // Then
        assertThat(existsByUsername).isTrue();
    }

    @Test
    void existsByUsername_가입되지않은유저찾기_false() throws Exception {
        // Given
        // When
        boolean existsByUsername = accountRepository.existsByUsername(BasicAccountData.USERNAME);

        // Then
        assertThat(existsByUsername).isFalse();
    }


    @Test
    void existsById_가입된유저찾기_true() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        boolean result = accountRepository.existsById(account.getId());

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsById_가입되지않은유저확인_false() throws Exception {
        // Given
        // When
        boolean existsById = accountRepository.existsById(1000L);

        // Then
        assertThat(existsById).isFalse();
    }

    @Test
    void findById_가입된유저찾기_조회성공() throws Exception {
        // Given
        Account account = accountRepository.save(new Account(BasicAccountData.USERNAME, BasicAccountData.PASSWORD, BasicAccountData.NICKNAME, BasicAccountData.PROFILE_IMAGE_FILE, BasicAccountData.ROLES));

        // When
        Optional<Account> findAccount = accountRepository.findById(account.getId());

        // Then
        assertThat(findAccount).isPresent();
        assertThat(findAccount.get().getId()).isEqualTo(account.getId());
        assertThat(findAccount.get().getUsername()).isEqualTo(account.getUsername());
        assertThat(findAccount.get().getPassword()).isEqualTo(account.getPassword());
        assertThat(findAccount.get().getNickname()).isEqualTo(account.getNickname());
        assertThat(findAccount.get().getRoles()).isEqualTo(account.getRoles());
    }

    @Test
    void findById_가입되지않은유저찾기_Empty() throws Exception {
        // Given
        // When
        Optional<Account> findAccount = accountRepository.findById(1000L);

        // Then
        assertThat(findAccount).isEmpty();
    }
}