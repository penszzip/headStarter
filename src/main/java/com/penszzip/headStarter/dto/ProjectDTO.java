package com.penszzip.headStarter.dto;

import java.time.OffsetDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectDTO {
    private long id;
    private String name;
    private String description;
    private String author;
    private int fundingGoal;
    private int currentFunding;
    private OffsetDateTime deadline;
}
