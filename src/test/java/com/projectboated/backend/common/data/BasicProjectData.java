package com.projectboated.backend.common.data;

import java.time.LocalDateTime;

public abstract class BasicProjectData {

    public final static Long PROJECT_ID = 120L;
    public final static String PROJECT_NAME = "name";
    public final static String PROJECT_DESCRIPTION = "description";
    public final static LocalDateTime PROJECT_DEADLINE = LocalDateTime.now();
}
