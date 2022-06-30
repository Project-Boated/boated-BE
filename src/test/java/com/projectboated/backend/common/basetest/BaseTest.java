package com.projectboated.backend.common.basetest;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class BaseTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

}
