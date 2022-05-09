package my.sleepydeveloper.projectcompass.domain.profileimage.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity @Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "image_type")
public abstract class ProfileImage {

    @Id @GeneratedValue
    private Long id;
}
