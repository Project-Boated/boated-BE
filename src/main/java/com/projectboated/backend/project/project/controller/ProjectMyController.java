package com.projectboated.backend.project.project.controller;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.controller.dto.common.ProjectCaptainResponse;
import com.projectboated.backend.project.project.controller.dto.common.ProjectResponse;
import com.projectboated.backend.project.project.controller.dto.response.GetMyProjectsResponse;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.service.ProjectCrewService;
import com.projectboated.backend.project.project.service.ProjectService;
import com.projectboated.backend.project.project.service.condition.GetMyProjectsCond;
import com.projectboated.backend.project.project.service.dto.MyProjectsDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects/my")
public class ProjectMyController {

    private final ProjectService projectService;
    private final ProjectCrewService projectCrewService;

    @GetMapping
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

        MyProjectsDto myProjects = projectService.getMyProjects(account.getId(), cond);
        int page = myProjects.getPage();
        int size = myProjects.getSize();
        boolean hasNext = myProjects.isHasNext();
        List<Project> projects = myProjects.getProjects();

        List<ProjectResponse> projectResponses = projects.stream()
                .map(p -> new ProjectResponse(p, new ProjectCaptainResponse(p.getCaptain()), projectCrewService.findAllCrews(p.getId())))
                .toList();

        return new GetMyProjectsResponse(page, size, hasNext, projectResponses);
    }

}
