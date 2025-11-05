package com.studyapp.models;

public class SubjectModel {
    private long id;
    private String name;
    private String goal;

    public SubjectModel() {
    }

    public SubjectModel(long id, String name, String goal) {
        this.id = id;
        this.name = name;
        this.goal = goal;
    }

    // Getters and Setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGoal() {
        return goal;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }
}
