package com.penszzip.headStarter.utils;

import com.penszzip.headStarter.dto.ProjectDTO;
import com.penszzip.headStarter.model.Project;

public class ProjectMapper {
    public static void mapProjectDTOtoProject(Project project, ProjectDTO projectDTO) {
        if (projectDTO.getName() != null) {
            project.setName(projectDTO.getName());
        }
        if (projectDTO.getDescription() != null) {
            project.setDescription(projectDTO.getDescription());
        }
        if (projectDTO.getFundingGoal() != null) {
            project.setFundingGoal(projectDTO.getFundingGoalAsInt());
        }
        if (projectDTO.getCurrentFunding() != null) {
            project.setCurrentFunding(projectDTO.getCurrentFundingAsInt());
        }
        if (projectDTO.getDeadline() != null) {
            project.setDeadline(projectDTO.getDeadlineAsDate());
        }
    }
}
