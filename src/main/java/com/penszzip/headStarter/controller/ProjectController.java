package com.penszzip.headStarter.controller;

import java.io.IOException;
import java.util.ArrayList;
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
import com.penszzip.headStarter.utils.ProjectMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.penszzip.headStarter.dto.ProjectDTO;
import com.penszzip.headStarter.model.Project;

import org.springframework.web.bind.annotation.PostMapping;
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
    public ResponseEntity<Project> createProject(@ModelAttribute ProjectDTO newProject,
        @RequestPart("images") List<MultipartFile> images) {
        try {
            List<String> urls = s3Service.uploadFile(images, newProject.getName());

            Project project = new Project();
            ProjectMapper.mapProjectDTOtoProject(project, newProject);
            project.setImages(urls);

            Project savedProject = projectRepository.save(project);

            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    
    @GetMapping("/projects/{id}")
    public ResponseEntity<Project> getProject(@PathVariable long id) {
        Optional<Project> foundProject = projectRepository.findById(id);
        
        return foundProject.map(project -> new ResponseEntity<>(project, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/projects/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable long id, 
        @ModelAttribute ProjectDTO projectUpdateFormData,
        @RequestPart("images") List<MultipartFile> images) {

        Optional<Project> foundProject = projectRepository.findById(id);

        try {
            List<String> urls = s3Service.uploadFile(images, projectUpdateFormData.getName());

            return foundProject.map(project -> {
                ProjectMapper.mapProjectDTOtoProject(project, projectUpdateFormData);

                List<String> newUrlList = project.getImages() == null ? new ArrayList<>() : project.getImages();
                for (String url : urls) {
                    newUrlList.add(url);
                }
                project.setImages(newUrlList);

                Project savedProject = projectRepository.save(project);
                return new ResponseEntity<>(savedProject, HttpStatus.OK);
        }).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException exception) {
            System.out.println(exception.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }    

    @DeleteMapping("/projects/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {
        projectRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
