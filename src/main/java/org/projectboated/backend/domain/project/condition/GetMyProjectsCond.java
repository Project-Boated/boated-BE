package org.projectboated.backend.domain.project.condition;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Getter
@Builder
public class GetMyProjectsCond {

    boolean captainNotTerm;
    boolean captainTerm;

    boolean crewNotTerm;
    boolean crewTerm;

    Pageable pageable;
}
