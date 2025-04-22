package com.penszzip.headStarter.utils;

import com.penszzip.headStarter.dto.ProjectDTO;
import com.penszzip.headStarter.model.Project;

public class ProjectMapper {
    public static Project toEntity(ProjectDTO projectDTO) {
        Project project = new Project();
        project.setId(projectDTO.getId());
        project.setName(projectDTO.getName());
        project.setDescription(projectDTO.getDescription());
        project.setAuthor(projectDTO.getAuthor());
        project.setFundingGoal(projectDTO.getFundingGoal());
        project.setCurrentFunding(projectDTO.getCurrentFunding());
        project.setDeadline(projectDTO.getDeadline());
        return project;
    }

    public static ProjectDTO toDTO(Project project) {
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(project.getId());
        projectDTO.setName(project.getName());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setAuthor(project.getAuthor());
        projectDTO.setFundingGoal(project.getFundingGoal());
        projectDTO.setCurrentFunding(project.getCurrentFunding());
        projectDTO.setDeadline(project.getDeadline());
        return projectDTO;
    }
}