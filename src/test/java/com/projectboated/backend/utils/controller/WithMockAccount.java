package com.projectboated.backend.utils.controller;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@WithSecurityContext(factory = WithMockAccountContextFactory.class)
public @interface WithMockAccount {

    long id() default 123;

    String username() default "USERNAME";

    String nickname() default "NICKNAME";

    String[] roles() default "ROLE_USER";

}
