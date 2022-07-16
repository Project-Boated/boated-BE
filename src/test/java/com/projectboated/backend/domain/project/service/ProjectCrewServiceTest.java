package com.projectboated.backend.domain.project.service;

import com.projectboated.backend.common.basetest.ServiceTest;

class ProjectCrewServiceTest extends ServiceTest {

    //    @Test
//    void findAllCrew_한명의crew가있을때_한명의crew조회() throws Exception {
//        // Given
//        Account captain = createAccount();
//        Project project = new Project(captain, PROJECT_NAME, PROJECT_DESCRIPTION, PROJECT_DEADLINE);
//        projectRepository.save(project);
//
//        Account crew = new Account(ACCOUNT_ID, "crew", BasicDataAccount.PASSWORD, "crew", BasicDataAccount.URL_PROFILE_IMAGE, BasicDataAccount.ROLES);
//        accountRepository.save(crew);
//
//        AccountProject accountProject = new AccountProject(crew, project);
//        accountProjectRepository.save(accountProject);
//
//        // When
//        List<Account> crews = projectService.findAllCrews(captain, project.getId());
//
//        // Then
//        assertThat(crews.size()).isEqualTo(1);
//    }

//    @Test
//    void findAllCrew_존재하지않는ProjectId_오류발생() throws Exception {
//        // Given
//        Account captain = createAccount();
//        // When
//        // Then
//        assertThatThrownBy(() -> projectService.findAllCrews(captain, 1L))
//                .isInstanceOf(ProjectNotFoundException.class);
//    }

}