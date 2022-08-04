package com.projectboated.backend.domain.project.aop;

import com.projectboated.backend.domain.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component @Aspect
@RequiredArgsConstructor
public class ProjectAuthorityAop {

    private final ProjectService projectService;

//    @Around("@annotation(onlyCaptain)")
//    public Object onlyCaptain(ProceedingJoinPoint joinPoint, OnlyCaptain onlyCaptain) throws Throwable {
//        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        projectService.isCaptainOrCrew()
//        return joinPoint.proceed();
//    }

}
