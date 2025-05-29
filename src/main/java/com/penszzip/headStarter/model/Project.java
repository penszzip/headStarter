package com.penszzip.headStarter.model;

import java.time.OffsetDateTime;
import java.util.List;

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
    private List<String> images;
    private String author;
    private int fundingGoal;
    private int currentFunding;
    private OffsetDateTime deadline;

    public Project() {}
}
