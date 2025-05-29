package com.penszzip.headStarter.utils;

import com.penszzip.headStarter.dto.ProjectDTO;
import com.penszzip.headStarter.model.Project;

public class ProjectMapper {
    public static void mapProjectDTOtoProject(Project project, ProjectDTO projectDTO) {
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setAuthor(projectDTO.getAuthor());
        project.setFundingGoal(projectDTO.getFundingGoalAsInt());
        project.setCurrentFunding(projectDTO.getCurrentFundingAsInt());
        project.setDeadline(projectDTO.getDeadlineAsDate());
    }
}
