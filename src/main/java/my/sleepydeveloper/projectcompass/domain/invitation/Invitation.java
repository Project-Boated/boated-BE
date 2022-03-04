package my.sleepydeveloper.projectcompass.domain.invitation;

import lombok.Getter;
import lombok.NoArgsConstructor;
import my.sleepydeveloper.projectcompass.domain.account.entity.Account;
import my.sleepydeveloper.projectcompass.domain.common.entity.BaseEntity;
import my.sleepydeveloper.projectcompass.domain.project.entity.Project;

import javax.persistence.*;

@Entity
@NoArgsConstructor @Getter
public class Invitation extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invitation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id")
    private Account account;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public Invitation(Account account, Project project) {
        this.account = account;
        this.project = project;
    }
}
