package com.projectboated.backend.project.project.repository;

import com.projectboated.backend.account.account.entity.Account;
import com.projectboated.backend.project.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long>, ProjectQueryDslRepository {

    boolean existsByNameAndCaptain(String name, Account captain);

    @Query("select p from Project p where p.captain=:captain and p.deadline is not null and p.createdDate < :nextMonth and p.deadline >= :currentMonth")
    List<Project> findByCaptainAndDate(@Param("captain") Account captain, @Param("currentMonth") LocalDateTime currentMonth, @Param("nextMonth") LocalDateTime nextMonth);
}
