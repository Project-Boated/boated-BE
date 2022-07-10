package com.projectboated.backend.common.data;

import java.time.LocalDateTime;

public abstract class BasicDataTask {

    public static final String TASK_NAME="taskName";

    public static final String TASK_DESCRIPTION="taskDescription";

    public static final LocalDateTime TASK_DEADLINE=LocalDateTime.now();
}
