package com.example.onlinecvmaker.dataModels;

public class Reference {
    private String refName, refDesignation, refOrgnization, refMail, refPhone;

    //constructor
    public Reference(){
        refName = "not set";
        refDesignation = "";
        refOrgnization = "";
        refMail = "";
        refPhone = "";
    }
    //parameterized constructor
    public Reference(String name, String refDesignation, String refOrgnization, String refMail, String refPhone){
        this.refName = name;
        this.refDesignation = refDesignation;
        this.refOrgnization = refOrgnization;
        this.refMail = refMail;
        this.refPhone = refPhone;
    }
    //copy constructor
    public Reference(Reference reference){
        this.refName = reference.refName;
        this.refDesignation = reference.refDesignation;
        this.refOrgnization = reference.refOrgnization;
        this.refMail = reference.refMail;
        this.refPhone = reference.refPhone;
    }

    //getters
    public String getRefName() { return refName; }
    public String getRefDesignation() { return refDesignation; }
    public String getRefOrgnization() { return refOrgnization; }
    public String getRefMail() { return refMail; }
    public String getRefPhone() { return refPhone; }
}
