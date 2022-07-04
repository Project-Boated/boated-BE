package com.projectboated.backend.web.project.controller;

import com.projectboated.backend.domain.account.account.entity.Account;
import com.projectboated.backend.domain.project.service.AccountProjectService;
import com.projectboated.backend.domain.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.domain.project.entity.Project;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.project.service.dto.MyProjectsDto;
import com.projectboated.backend.web.project.dto.response.GetMyProjectsResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectMyController {

    private final ProjectService projectService;
    private final AccountProjectService accountProjectService;

    @GetMapping("/api/projects/my")
    public GetMyProjectsResponse getMyProjects(
            @AuthenticationPrincipal Account account,
            @RequestParam(value = "captain", required = false) List<String> captain,
            @RequestParam(value = "crew", required = false) List<String> crew,
            Pageable pageable
    ) {
        GetMyProjectsCond cond = GetMyProjectsCond.builder()
                .captainNotTerm(captain != null && captain.contains("not-term"))
                .captainTerm(captain != null && captain.contains("term"))
                .crewNotTerm(crew != null && crew.contains("not-term"))
                .crewTerm(crew != null && crew.contains("term"))
                .pageable(pageable)
                .build();

        MyProjectsDto myProjects = projectService.getMyProjects(account, cond);
        int page = myProjects.getPage();
        int size = myProjects.getSize();
        boolean hasNext = myProjects.isHasNext();
        List<Project> projects = myProjects.getProjects();

        List<GetMyProjectsResponse.ProjectResponse> projectResponses = projects.stream()
                .map(p -> new GetMyProjectsResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .toList();

        return new GetMyProjectsResponse(page, size, hasNext, projectResponses);
    }

}
