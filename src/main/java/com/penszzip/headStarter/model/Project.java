package com.penszzip.headStarter.model;

import java.time.OffsetDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Project {

    @Id
    @GeneratedValue
    private long id;
    private String name;
    private String description;
    private String author;
    private int fundingGoal;
    private int currentFunding;
    private OffsetDateTime deadline;

    public Project() {}

    public Project(String name, String description, String author, int fundingGoal, int currentFunding, OffsetDateTime deadline) {
        this.name = name;
        this.description = description;
        this.author = author;
        this.fundingGoal = fundingGoal;
        this.currentFunding = currentFunding;
        this.deadline = deadline;
    }
}
