package com.studyapp.models;

public class SubjectModel {
    private long id;
    private String name;
    private String goal;
    private boolean isSynced;

    public SubjectModel() {
    }

    public SubjectModel(long id, String name, String goal, boolean isSynced) {
        this.id = id;
        this.name = name;
        this.goal = goal;
        this.isSynced = isSynced;
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

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
