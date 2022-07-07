package com.projectboated.backend.web.project.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetCrewsResponse {

    public List<CrewResponse> crews;
}
