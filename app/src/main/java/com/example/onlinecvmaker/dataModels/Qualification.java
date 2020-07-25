package com.example.onlinecvmaker.dataModels;

public class Qualification {
    private String course, institute, grade, year;

    //constructor
    public Qualification(){
        course = "";
        institute = "";
        grade = "";
        year = "";
    }

    //parameterized constructor
    public Qualification(String course, String institute, String grade, String year){
        this.course = course;
        this.institute = institute;
        this.grade = grade;
        this.year = year;
    }

    //copy Constructor
    public Qualification(Qualification qualification){
        this.course = qualification.course;
        this.institute = qualification.institute;
        this.grade = qualification.grade;
        this.year = qualification.year;
    }

    //setters
    public void setCourse(String course){this.course = course;}
    public void setInstitute(String institute){this.institute = institute;}
    public void setGrade(String grade){this.grade = grade;}
    public void setYear(String year){this.year = year;}

    //getters
    public String getCourse(){return this.course;}
    public String getInstitute(){return this.institute;}
    public String getGrade(){return this.grade;}
    public String getYear(){return this.year;}
}
