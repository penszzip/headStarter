package com.penszzip.headStarter.controller;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.penszzip.headStarter.repository.ProjectRepository;
import com.penszzip.headStarter.service.S3Service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.penszzip.headStarter.model.Project;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;



@RestController
@CrossOrigin(origins = "*")
public class ProjectController {

    ProjectRepository projectRepository;
    S3Service s3Service;

    public ProjectController(ProjectRepository projectRepository, S3Service s3Service) {
        this.projectRepository = projectRepository;
        this.s3Service = s3Service;
    }

    @GetMapping("/projects")
    public ResponseEntity<List<Project>> getProjects() {
        List<Project> projects = projectRepository.findAll();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("/projects")
    public ResponseEntity<Project> createProject(@RequestBody Project project) {
        // do upload file for each file and then create 
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
    public ResponseEntity<Project> updateProject(@PathVariable long id, @RequestBody Project newProject) {
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

    @PostMapping("/projects/images/upload")
    public ResponseEntity<String> upload(@RequestPart MultipartFile image, @RequestParam String name) {
        try {
            // upload files to s3, return url for each image
            // return array of urls
            // put array of urls onto payload to post call in the frontend
            String url = s3Service.uploadFile(image, name);
            return new ResponseEntity<>(url, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            System.out.println("Couldn't handle file upload: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {
        projectRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
