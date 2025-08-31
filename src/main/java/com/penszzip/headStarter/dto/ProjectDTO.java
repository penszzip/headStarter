package com.penszzip.headStarter.dto;

import java.time.OffsetDateTime;

public class ProjectDTO {
    private String name;
    private String description;
    private String fundingGoal;
    private String currentFunding;
    private String deadline;

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public String getFundingGoal() {
        return this.fundingGoal;
    }

    public String getCurrentFunding() {
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

    public void setFundingGoal(String fundingGoal) {
        this.fundingGoal = fundingGoal;
    }

    public void setCurrentFunding(String currentFunding) {
        this.currentFunding = currentFunding;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public int getFundingGoalAsInt() {
        return Integer.parseInt(this.fundingGoal);
    }

    public int getCurrentFundingAsInt() {
        return Integer.parseInt(this.currentFunding);
    }

    public OffsetDateTime getDeadlineAsDate() {
        return OffsetDateTime.parse(deadline);
    }
}
