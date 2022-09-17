package com.projectboated.backend.utils.data;

import com.projectboated.backend.domain.project.entity.Project;

import java.time.LocalDateTime;

import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT;
import static com.projectboated.backend.utils.data.BasicDataAccount.ACCOUNT2;

public abstract class BasicDataProject {

    public final static Long PROJECT_ID = 120L;
    public final static String PROJECT_NAME = "name";
    public final static String PROJECT_DESCRIPTION = "description";
    public final static LocalDateTime PROJECT_DEADLINE = LocalDateTime.now();
    public final static Project PROJECT = Project.builder()
            .captain(ACCOUNT)
            .name(PROJECT_NAME)
            .description(PROJECT_DESCRIPTION)
            .deadline(PROJECT_DEADLINE)
            .build();

    public final static Long PROJECT_ID2 = 120L;
    public final static String PROJECT_NAME2 = "name2";
    public final static String PROJECT_DESCRIPTION2 = "description2";
    public final static LocalDateTime PROJECT_DEADLINE2 = LocalDateTime.now();
    public final static Project PROJECT2 = Project.builder()
            .captain(ACCOUNT2)
            .name(PROJECT_NAME2)
            .description(PROJECT_DESCRIPTION2)
            .deadline(PROJECT_DEADLINE2)
            .build();

    public final static Long PROJECT_ID3 = 120L;

    public final static Long ACCOUNT_PROJECT_ID = 12924L;

}
