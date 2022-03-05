package my.sleepydeveloper.projectcompass.domain.project.repository;

import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    boolean existsByNameAndCaptain(String name,  Account captain);

    List<Project> findAllByCaptain(Account captain);
}
