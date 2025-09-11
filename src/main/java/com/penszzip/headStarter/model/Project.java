package com.penszzip.headStarter.model;

import java.time.OffsetDateTime;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user; // author of the project

    private Double fundingGoal;
    private Double currentFunding;
    private OffsetDateTime deadline;

    public Project() {}
}
