package com.projectboated.backend.project.project.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GetCrewsResponse {

    public List<CrewResponse> crews;
}
