package com.projectboated.backend.web.ganttchart.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class GetMyGanttChartResponse {

    private List<ProjectResponse> projects;

    public GetMyGanttChartResponse(List<ProjectResponse> projectResponses) {
        this.projects = projectResponses;
    }

    @Getter
    public static class ProjectResponse {

        private Long id;
        private String name;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        private List<TaskResponse> assignedTasks;

        @Builder
        public ProjectResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime deadline, List<TaskResponse> assignedTasks) {
            this.id = id;
            this.name = name;
            this.createdDate = createdDate;
            this.deadline = deadline;
            this.assignedTasks = assignedTasks;
        }
    }

    @Getter
    public static class TaskResponse {
        private Long id;
        private String name;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime createdDate;

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        private LocalDateTime deadline;

        @Builder
        public TaskResponse(Long id, String name, LocalDateTime createdDate, LocalDateTime deadline) {
            this.id = id;
            this.name = name;
            this.createdDate = createdDate;
            this.deadline = deadline;
        }
    }

}
