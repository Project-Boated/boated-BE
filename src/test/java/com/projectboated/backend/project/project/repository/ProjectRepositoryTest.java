package com.projectboated.backend.project.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.utils.base.RepositoryTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static com.projectboated.backend.utils.data.BasicDataAccount.*;
import static com.projectboated.backend.utils.data.BasicDataProject.PROJECT_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Project : Persistence 단위 테스트")
class ProjectRepositoryTest extends RepositoryTest {

    @Test
    void existsByNameAndCaptain_존재하는name과captain일경우_return_true() {
        // Given
        Account captain = insertAccount();
        insertProject(captain);

        // When
        boolean result = projectRepository.existsByNameAndCaptain(PROJECT_NAME, captain);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void existsByNameAndCaptain_존재하는name과존재하지않는captain일경우_return_false() {
        // Given
        Account captain = insertAccount(USERNAME, NICKNAME);
        insertProject(captain);

        Account unknown = insertAccount(USERNAME2, NICKNAME2);

        // When
        boolean result = projectRepository.existsByNameAndCaptain(PROJECT_NAME, unknown);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하지않는name과존재하는captain일경우_return_false() {
        // Given
        Account captain = insertAccount();
        insertProject(captain);

        // When
        boolean result = projectRepository.existsByNameAndCaptain("fail", captain);

        // Then
        assertThat(result).isFalse();
    }

    @Test
    void existsByNameAndCaptain_존재하지않는name과존재하지않는captain일경우_return_false() {
        // Given
        Account captain = insertAccount(USERNAME, NICKNAME);
        insertProject(captain);

        Account unknown = insertAccount(USERNAME2, NICKNAME2);

        // When
        boolean result = projectRepository.existsByNameAndCaptain("fail", unknown);

        // Then
        assertThat(result).isFalse();
    }


    @Test
    void findByCaptainAndDate_deadline이null일경우_return_empty() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now(), null);

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).isEmpty();
    }

/*    @Test
    void findByCaptainAndDate_시작은전달끝은이번달_return_1개() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now().minusMonths(1), LocalDateTime.now());

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).containsExactly(project);
    }*/

    @Test
    void findByCaptainAndDate_시작은이번달끝은이번달_return_1개() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now(), LocalDateTime.now());

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).containsExactly(project);
    }

    @Test
    void findByCaptainAndDate_시작은이번달끝은다음달_return_1개() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now(), LocalDateTime.now().plusMonths(1));

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).containsExactly(project);
    }

/*    @Test
    void findByCaptainAndDate_시작은저번달끝은다음달_return_1개() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now().minusMonths(1), LocalDateTime.now().plusMonths(1));

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).containsExactly(project);
    }

    @Test
    void findByCaptainAndDate_시작은저번달끝은저번달_return_null() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now().minusMonths(1), LocalDateTime.now().minusMonths(1));

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    void findByCaptainAndDate_시작은다음달끝은다음달_return_null() {
        // Given
        Account captain = insertAccount();
        Project project = insertProject(captain, LocalDateTime.now().plusMonths(1), LocalDateTime.now().plusMonths(1));

        // When
        LocalDateTime targetDate = LocalDateTime.now().withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0);
        LocalDateTime nextDate = targetDate.plusMonths(1);

        List<Project> result = projectRepository.findByCaptainAndDate(captain, targetDate, nextDate);

        // Then
        assertThat(result).isEmpty();
    }*/


}