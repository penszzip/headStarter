package com.penszzip.headStarter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.penszzip.headStarter.repository.ProjectRepository;

import lombok.AllArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@AllArgsConstructor
@RestController
public class ProjectController {

    private final ProjectRepository projectRepository;

    @GetMapping("/projects")
    public ResponseEntity getProjects() {
        return new ResponseEntity(HttpStatus.OK);
    }
    

}
