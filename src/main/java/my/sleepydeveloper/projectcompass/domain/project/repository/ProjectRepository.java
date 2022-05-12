package my.sleepydeveloper.projectcompass.domain.project.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByNameAndCaptain(String name, Account captain);

    boolean existsByName(String name);

    List<Project> findAllByCaptain(Account captain);

    @Query("select p from Project p where p.captain=:captain and p.isTerminated=false")
    List<Project> findAllByCaptainAndNotTerminated(@Param("captain") Account captain);

    @Query("select p from Project p where p.captain=:captain and p.isTerminated=true")
    List<Project> findAllByCaptainAndTerminated(@Param("captain")Account captain);
}
