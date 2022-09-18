package com.projectboated.backend.project.service.condition;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

@Getter
@Builder
public class GetMyProjectsCond {

    boolean captainNotTerm;
    boolean captainTerm;

    boolean crewNotTerm;
    boolean crewTerm;

    Pageable pageable;

    public boolean hasCond() {
        return captainNotTerm || captainTerm ||
                crewNotTerm || crewTerm;
    }
}
