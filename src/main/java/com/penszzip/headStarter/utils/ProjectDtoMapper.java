package com.penszzip.headStarter.utils;

import com.penszzip.headStarter.dto.ProjectDTO;
import com.penszzip.headStarter.model.Project;

public class ProjectDtoMapper {
    public static void mapProjectDTOtoProject(Project project, ProjectDTO projectDTO) {
        if (projectDTO.getName() != null) {
            project.setName(projectDTO.getName());
        }
        if (projectDTO.getDescription() != null) {
            project.setDescription(projectDTO.getDescription());
        }
        if (projectDTO.getFundingGoal() != null) {
            project.setFundingGoal(projectDTO.getFundingGoal());
        }
        if (projectDTO.getCurrentFunding() != null) {
            project.setCurrentFunding(projectDTO.getCurrentFunding());
        }
        if (projectDTO.getDeadline() != null) {
            project.setDeadline(projectDTO.getDeadlineAsDate());
        }
    }
}
