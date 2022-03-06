package my.sleepydeveloper.projectcompass.domain.project.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByNameAndCaptain(String name, Account captain);

    @Query("select count(p) from Project p where p.captain=:captain and p.name=:name and not p=:project")
    Long countByNameAndCaptainAndNotProject(@Param("name") String name, @Param("captain") Account captain, @Param("project") Project project);

    List<Project> findAllByCaptain(Account captain);
}
