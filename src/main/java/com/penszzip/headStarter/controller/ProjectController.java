package com.penszzip.headStarter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import com.penszzip.headStarter.repository.ProjectRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.penszzip.headStarter.model.Project;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@CrossOrigin(origins = "*")
public class ProjectController {

    ProjectRepository projectRepository;

    public ProjectController(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        Project newProject = projectRepository.save(project);
        return new ResponseEntity<>(newProject, HttpStatus.CREATED);
    }
    
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable long id) {
        Optional<Project> foundProject = projectRepository.findById(id);
        
        return foundProject.map(project -> new ResponseEntity<>(project, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> putMethodName(@PathVariable long id, @RequestBody Project newProject) {
        Optional<Project> foundProject = projectRepository.findById(id);

        return foundProject.map(project -> {
            project.setName(newProject.getName());
            project.setDescription(newProject.getDescription());
            project.setAuthor(newProject.getAuthor());
            project.setFundingGoal(newProject.getFundingGoal());
            project.setCurrentFunding(newProject.getCurrentFunding());
            project.setDeadline(newProject.getDeadline());

            Project savedProject = projectRepository.save(project);
            return new ResponseEntity<>(savedProject, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {
        projectRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
