package com.projectboated.backend.project.project.aop;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.aop.exception.ProjectIdNotFoundException;
import com.projectboated.backend.project.project.entity.Project;
import com.projectboated.backend.project.project.service.ProjectService;
import com.projectboated.backend.project.project.service.exception.OnlyCaptainException;
import com.projectboated.backend.project.project.service.exception.OnlyCaptainOrCrewException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@Aspect
@RequiredArgsConstructor
public class ProjectAuthorityAop {

    private final ProjectService projectService;

    @Around("@annotation(onlyCaptain)")
    public Object onlyCaptain(ProceedingJoinPoint joinPoint, OnlyCaptain onlyCaptain) throws Throwable {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Long projectId = null;
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            if (parameterNames[i].equals("projectId")) {
                projectId = (Long) args[i];
            }
        }

        if (projectId == null) {
            throw new ProjectIdNotFoundException();
        }

        Project project = projectService.findById(projectId);
        if (!project.isCaptain(account)) {
            throw new OnlyCaptainException();
        }

        return joinPoint.proceed();
    }

    @Around("@annotation(onlyCaptainOrCrew)")
    public Object onlyCaptainOrCrew(ProceedingJoinPoint joinPoint, OnlyCaptainOrCrew onlyCaptainOrCrew) throws Throwable {
        Account account = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Long projectId = null;
        Object[] args = joinPoint.getArgs();
        String[] parameterNames = signature.getParameterNames();

        for (int i = 0; i < args.length; i++) {
            if (parameterNames[i].equals("projectId")) {
                projectId = (Long) args[i];
            }
        }

        if (projectId == null) {
            throw new ProjectIdNotFoundException();
        }

        Project project = projectService.findById(projectId);
        if (!projectService.isCaptainOrCrew(project, account)) {
            throw new OnlyCaptainOrCrewException();
        }

        return joinPoint.proceed();
    }

}
