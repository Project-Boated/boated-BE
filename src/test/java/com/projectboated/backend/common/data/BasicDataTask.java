package com.projectboated.backend.common.data;

import java.time.LocalDateTime;

public abstract class BasicDataTask {

    public static final String TASK_NAME="taskName";
    public static final String TASK_DESCRIPTION="taskDescription";
    public static final LocalDateTime TASK_DEADLINE=LocalDateTime.now();

    public static final String TASK_NAME2="taskName2";
    public static final String TASK_DESCRIPTION2="taskDescription2";
    public static final LocalDateTime TASK_DEADLINE2=LocalDateTime.now();
}
