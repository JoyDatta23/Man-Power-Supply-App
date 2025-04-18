package com.example.manpowersupply;

public class Job {
    private String jobId;
    private String title;
    private String description;
    private String empNeed;
    private String skills;
    private String minSalary;
    private String maxSalary;
    private String deadline;
    private String postedUser;

    public Job(String jobId, String title, String description,String empNeed, String skills, String minSalary,
               String maxSalary, String deadline,String user) {
        this.jobId = jobId;
        this.title = title;
        this.description = description;
        this.empNeed = empNeed;
        this.skills = skills;
        this.minSalary = minSalary;
        this.maxSalary = maxSalary;
        this.deadline = deadline;
        this.postedUser = user;
    }


    public String getJobId() { return jobId; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getEmpNeed(){
        return empNeed;
    }

    public String getSkills() {
        return skills;
    }

    public String getMinSalary() {
        return minSalary;
    }

    public String getMaxSalary() {
        return maxSalary;
    }

    public String getDeadline() { return deadline; }

    public String getPostedUser() {
        return postedUser;
    }
}
