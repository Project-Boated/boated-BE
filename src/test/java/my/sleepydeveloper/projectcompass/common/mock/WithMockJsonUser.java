package my.sleepydeveloper.projectcompass.common.mock;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@WithSecurityContext(factory = WithMockJsonUserSecurityContextFactory.class)
public @interface WithMockJsonUser {

    String username() default "username";
    String password() default "password";
    String nickname() default "nickname";

    String role() default "ROLE_USER";
}
