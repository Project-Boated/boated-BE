package com.projectboated.backend.domain.project.entity;

import com.projectboated.backend.account.account.entity.Account;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.projectboated.backend.common.data.BasicDataAccount.*;
import static com.projectboated.backend.common.data.BasicDataProject.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("AccountProject : Entity 단위 테스트")
class AccountProjectTest {

    @Test
    void 생성자_AccountProject생성_return_생성된AccountProject() {
        // Given
        Project project = createProject(createCaptain());
        Account crew = createCrew();

        // When
        AccountProject accountProject = new AccountProject(ACCOUNT_PROJECT_ID, crew, project);

        // Then
        assertThat(accountProject.getAccount()).isEqualTo(crew);
        assertThat(accountProject.getProject()).isEqualTo(project);
    }

    private Project createProject(Account captain) {
        return Project.builder()
                .captain(captain)
                .name(PROJECT_NAME)
                .description(PROJECT_DESCRIPTION)
                .deadline(PROJECT_DEADLINE)
                .build();
    }

    private Account createCaptain() {
        return Account.builder()
                .username(USERNAME)
                .password(PASSWORD)
                .nickname(NICKNAME)
                .profileImageFile(URL_PROFILE_IMAGE)
                .roles(ROLES)
                .build();
    }

    private Account createCrew() {
        return Account.builder()
                .username(USERNAME2)
                .password(PASSWORD2)
                .nickname(NICKNAME2)
                .profileImageFile(URL_PROFILE_IMAGE2)
                .roles(ROLES)
                .build();
    }

}