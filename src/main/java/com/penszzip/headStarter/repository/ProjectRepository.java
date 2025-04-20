package com.penszzip.headStarter.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.penszzip.headStarter.model.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

}
