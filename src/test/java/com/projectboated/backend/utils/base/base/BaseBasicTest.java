package com.projectboated.backend.utils.base.base;

import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
public class BaseBasicTest {

    static {
        System.setProperty("com.amazonaws.sdk.disableEc2Metadata", "true");
    }

}
