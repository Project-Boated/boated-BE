package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.common.basetest.ControllerTest;
import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.dto.MyProjectsDto;
import com.projectboated.backend.web.config.WithMockAccount;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID;
import static com.projectboated.backend.common.data.BasicDataAccount.ACCOUNT_ID2;
import static com.projectboated.backend.common.data.BasicDataProject.PROJECT_ID;
import static com.projectboated.backend.web.project.controller.document.ProjectMyDocument.documentProjectMyRetrieve;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("Project My : Controller 단위테스트")
class ProjectMyControllerTest extends ControllerTest {

    @Test
    @WithMockAccount
    void terminateProject_프로젝트를종료시킴_정상() throws Exception {
        // Given
        Account account = createAccount(ACCOUNT_ID);
        Project project = createProject(PROJECT_ID, account);
        Account crew = createAccount(ACCOUNT_ID2);

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.add("captain", "term,not-term");
        map.add("crew", "term,not-term");
        map.add("page", "0");
        map.add("size", "1");
        map.add("sort", "name,desc");
        map.add("sort", "deadline,asc");
        map.add("sort", "createdDate,asc");

        when(projectService.getMyProjects(any(), any())).thenReturn(new MyProjectsDto(0, 1, true, List.of(project)));
        when(projectCrewService.findAllCrews(anyLong())).thenReturn(List.of(crew));

        // When
        // Then
        mockMvc.perform(get("/api/projects/my").params(map))
                .andExpect(status().isOk())
                .andDo(documentProjectMyRetrieve());
    }

}