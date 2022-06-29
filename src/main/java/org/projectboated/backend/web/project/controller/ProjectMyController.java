package org.projectboated.backend.web.project.controller;

import lombok.RequiredArgsConstructor;
import org.projectboated.backend.domain.account.entity.Account;
import org.projectboated.backend.domain.accountproject.service.AccountProjectService;
import org.projectboated.backend.domain.project.condition.GetMyProjectsCond;
import org.projectboated.backend.domain.project.entity.Project;
import org.projectboated.backend.domain.project.service.ProjectService;
import org.projectboated.backend.domain.project.service.dto.MyProjectsDto;
import org.projectboated.backend.web.project.dto.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class ProjectMyController {

    private final ProjectService projectService;
    private final AccountProjectService accountProjectService;

    @GetMapping("/test")
    private void asdf(@RequestParam(value = "captain", required = false) List<List<String>> captain) {
        System.out.println(captain);
    }


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

    @GetMapping("/api/projects/my/captain")
    public GetMyCaptainProjectResponse getMyCaptainProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainProjectResponse.ProjectResponse> projects = projectService.findAllByCaptainNotTerminated(account).stream()
                .map((p) -> new GetMyCaptainProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCaptainProjectResponse(projects);
    }

    @GetMapping("/api/projects/my/crew")
    public GetMyCrewProjectResponse getMyCrewProject(@AuthenticationPrincipal Account account) {
        List<GetMyCrewProjectResponse.ProjectResponse> projects = projectService.findAllByCrewNotTerminated(account).stream()
                .map((p) -> new GetMyCrewProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .toList();

        return new GetMyCrewProjectResponse(projects);
    }

    @GetMapping("/api/projects/my/captain/terminated")
    public GetMyCaptainTerminatedProjectResponse getMyCaptainTerminatedProject(@AuthenticationPrincipal Account account) {
        List<GetMyCaptainTerminatedProjectResponse.ProjectResponse> projects = projectService.findAllByCaptainTerminated(account).stream()
                .map((p) -> new GetMyCaptainTerminatedProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCaptainTerminatedProjectResponse(projects);
    }

    @GetMapping("/api/projects/my/crew/terminated")
    public GetMyCrewTerminatedProjectResponse getMyCrewTerminatedProject(@AuthenticationPrincipal Account account) {
        List<GetMyCrewTerminatedProjectResponse.ProjectResponse> projects = projectService.findAllByCrewTerminated(account).stream()
                .map((p) -> new GetMyCrewTerminatedProjectResponse.ProjectResponse(p, accountProjectService.getCrews(p)))
                .collect(Collectors.toList());

        return new GetMyCrewTerminatedProjectResponse(projects);
    }

}
