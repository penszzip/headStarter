package com.penszzip.headStarter.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.penszzip.headStarter.repository.ProjectRepository;
import com.penszzip.headStarter.repository.UserRepository;
import com.penszzip.headStarter.service.S3Service;
import com.penszzip.headStarter.utils.ProjectDtoMapper;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;

import com.penszzip.headStarter.dto.ProjectDTO;
import com.penszzip.headStarter.dto.ProjectResponseDTO;
import com.penszzip.headStarter.model.Project;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;



@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/projects")
public class ProjectController {

    ProjectRepository projectRepository;
    S3Service s3Service;
    UserRepository userRepository;

    public ProjectController(ProjectRepository projectRepository, S3Service s3Service, UserRepository userRepository) {
        this.projectRepository = projectRepository;
        this.s3Service = s3Service;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ResponseEntity<List<ProjectResponseDTO>> getProjects() {
        List<ProjectResponseDTO> projects = projectRepository.findAll().stream()
            .map(this::mapToDTO).toList();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<ProjectResponseDTO> createProject(@ModelAttribute ProjectDTO newProject,
        @RequestPart("images") List<MultipartFile> images, Principal principal) {
        try {
            List<String> urls = s3Service.uploadFile(images, newProject.getName());

            Project project = new Project();
            ProjectDtoMapper.mapProjectDTOtoProject(project, newProject);

            // Set the user
            if (principal == null) {
                return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED).build();
            }
            project.setUser(userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new UsernameNotFoundException(
                            "Username not found with username: " + principal.getName())));

            project.setImages(urls);

            ProjectResponseDTO savedProject = mapToDTO(projectRepository.save(project));
            
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProject(@PathVariable long id) {
        Optional<Project> foundProject = projectRepository.findById(id);
        
        return foundProject.map(project -> new ResponseEntity<>(project, HttpStatus.OK))
            .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable long id, 
        @ModelAttribute ProjectDTO projectUpdateFormData,
        @RequestPart("images") List<MultipartFile> images,
        Principal principal) {

        Optional<Project> foundProject = projectRepository.findById(id);

        // Check if project exists
        if (foundProject.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        if (!foundProject.get().getUser().getUsername().equals(principal.getName())) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }

        try {
            String projectName = projectUpdateFormData.getName();
            List<String> urls = s3Service.uploadFile(images, projectName);

            return foundProject.map(project -> {
                ProjectDtoMapper.mapProjectDTOtoProject(project, projectUpdateFormData);

                // Get existing image urls
                List<String> newUrlList = project.getImages() == null ? new ArrayList<>() : project.getImages();
                // Add new uploaded images urls and add to existing urls list
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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable long id) {

        Optional<Project> foundProject = projectRepository.findById(id);

        // Check if project exists
        if (foundProject.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        projectRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private ProjectResponseDTO mapToDTO(Project project) {
        ProjectResponseDTO dto = new ProjectResponseDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setDescription(project.getDescription());
        dto.setImages(project.getImages());
        if (project.getUser() != null) {
            dto.setUser(project.getUser().getUsername());
        }
        dto.setFundingGoal(project.getFundingGoal());
        dto.setCurrentFunding(project.getCurrentFunding());
        dto.setDeadline(project.getDeadline());
        return dto;
    }
}
