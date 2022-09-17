package com.projectboated.backend.web.ganttchart;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.domain.project.service.ProjectService;
import com.projectboated.backend.domain.task.task.entity.Task;
import com.projectboated.backend.domain.task.task.service.AccountTaskService;
import com.projectboated.backend.web.ganttchart.dto.GetMyGanttChartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class GanttChartController {

    private final ProjectService projectService;
    private final AccountTaskService accountTaskService;

    @GetMapping("/api/my/gantt-chart")
    public GetMyGanttChartResponse GetMyGanttChart(
            @AuthenticationPrincipal Account account,
            @RequestParam(required = false, defaultValue = "0", name = "year") int requestYear,
            @RequestParam(required = false, defaultValue = "0", name = "month") int requestMonth
    ) {
        int year = requestYear == 0 || requestMonth == 0 ? LocalDate.now().getYear() : requestYear;
        int month = requestYear == 0 || requestMonth == 0 ? LocalDate.now().getMonthValue() : requestMonth;

        List<GetMyGanttChartResponse.ProjectResponse> projectResponses = projectService.findByAccountIdAndDate(account.getId(), LocalDateTime.of(year, month, 1, 0, 0)).stream()
                .map(p -> {
                    Long id = p.getId();
                    String name = p.getName();
                    LocalDateTime createdDate = p.getCreatedDate();
                    LocalDateTime deadline = p.getDeadline();

                    List<GetMyGanttChartResponse.TaskResponse> taskResponses = accountTaskService.findByProjectIdAndAccountIdAndBetweenDate(p.getId(), account.getId(), LocalDateTime.of(year, month, 1, 0, 0)).stream()
                            .map(at -> {
                                Task task = at.getTask();
                                return new GetMyGanttChartResponse.TaskResponse(task.getId(),
                                        task.getName(),
                                        task.getCreatedDate(),
                                        task.getDeadline());
                            }).toList();

                    return new GetMyGanttChartResponse.ProjectResponse(id, name, createdDate, deadline, taskResponses);
                }).toList();

        return new GetMyGanttChartResponse(projectResponses);
    }

}