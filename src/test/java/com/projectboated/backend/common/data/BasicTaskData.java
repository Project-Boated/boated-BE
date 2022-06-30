package com.projectboated.backend.common.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BasicTaskData {

    public static final String TASK_NAME="taskName";

    public static final String TASK_DESCRIPTION="taskDescription";

    public static final LocalDateTime TASK_DEADLINE=LocalDateTime.now();
}
