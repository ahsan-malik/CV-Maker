package com.example.onlinecvmaker.dataModels;

public class Experience {
    private String orgnization, designation, dateFrom, dateTo, role;

    //constructor
    public Experience(){
        orgnization = "org not set";
        designation = " ";
        dateFrom = "";
        dateTo = "";
        role = "";
    }

    //parameterized consturctor
    public  Experience(String orgnization, String designation, String dateFrom, String dateTo, String role){
        this.orgnization = orgnization;
        this.designation = designation;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.role = role;
    }

    //copy costructor
    public Experience(Experience experience){
        this.orgnization = experience.getOrgnization();
        this.designation = experience.getDesignation();
        this.dateFrom = experience.getDateFrom();
        this.dateTo = experience.getDateTo();
        this.role = experience.getRole();
    }

    //getters

    public String getOrgnization() {
        return orgnization;
    }

    public String getDesignation() {
        return designation;
    }

    public String getDateFrom() {
        return dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public String getRole() {
        return role;
    }
}
