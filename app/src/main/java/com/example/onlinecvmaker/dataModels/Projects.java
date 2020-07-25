package com.example.onlinecvmaker.dataModels;

public class Projects {
    private String title, description, duration;

    public  Projects(){
        title = "";
        description = "";
        duration = "";
    }
    public  Projects(String title, String description, String duration){
        this.title = title;
        this.description = description;
        this.duration = duration;
    }
    public Projects(Projects project){
        this.duration = project.duration;
        this.title = project.title;
        this.description = project.description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDuration() {
        return duration;
    }
}
