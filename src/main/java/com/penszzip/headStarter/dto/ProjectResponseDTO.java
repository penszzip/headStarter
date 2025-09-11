package com.penszzip.headStarter.dto;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProjectResponseDTO {
    private long id;
    private String name;
    private String description;
    private List<String> images;
    private String user;
    private Double fundingGoal;
    private Double currentFunding;
    private OffsetDateTime deadline;
}
