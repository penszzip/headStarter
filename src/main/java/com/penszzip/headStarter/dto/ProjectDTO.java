package com.penszzip.headStarter.dto;

import java.time.OffsetDateTime;

public class ProjectDTO {
    private String name;
    private String description;
    private Double fundingGoal;
    private Double currentFunding;
    private String deadline;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public Double getFundingGoal() {
        return this.fundingGoal;
    }

    public Double getCurrentFunding() {
        return this.currentFunding;
    }

    public String getDeadline() {
        return this.deadline;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFundingGoal(Double fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    public void setCurrentFunding(Double currentFunding) {
        this.currentFunding = currentFunding;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public OffsetDateTime getDeadlineAsDate() {
        return OffsetDateTime.parse(deadline);
    }
}
