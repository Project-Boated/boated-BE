package com.projectboated.backend.account.account.controller.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ValidationNicknameUniqueResponse {

    private boolean isDuplicated;

    @Builder
    public ValidationNicknameUniqueResponse(boolean isDuplicated) {
        this.isDuplicated = isDuplicated;
    }

}
