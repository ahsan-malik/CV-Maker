package com.example.onlinecvmaker.dataModels;

import android.graphics.Bitmap;

import java.util.ArrayList;

public class Person {
    private String name, father_name, dob, nationality, languages, gender, marital_status;
    private Bitmap image;
    private String objective, skills, hobbies, interests;
    private Qualification qualification;
    private Experience experience;
    private ContactDetails contactDetails;
    private Projects projects;
    private Reference reference;
    private ArrayList<Qualification> qualificationsList;
    private ArrayList<Experience> experiencesList;
    private ArrayList<Projects> projectsList;

    //default constructor
    public Person(){
        name = "not set";
        father_name = "not set";
        dob = "";
        nationality = "";
        languages = "";
        gender = "";
        marital_status = "";
        //qualification = new Qualification();
    }

    //parameterized constructor
    public Person(String name, String father_name, String dob, String nationality, String languages, String gender, String marital_status /*Qualification qualification*/){
        this.name = name;
        this.father_name = father_name;
        this.dob = dob;
        this.nationality = nationality;
        this.languages = languages;
        this.gender = gender;
        this.marital_status = marital_status;
        //this.qualification = new Qualification(qualification);
    }

    //copy constructor
    public Person(Person person){
        this.name = person.getName();
        this.father_name = person.getFather_name();
        this.dob = person.getDob();
        this.nationality = person.getNationality();
        this.languages = person.getLanguages();
        this.gender = person.getGender();
        this.marital_status = person.getMarital_status();
        //this.qualification = person.qualification;       //shallow copy
        //this.qualification = new Qualification(person.qualification);    //deep copy
    }

    //setters
    public void setName(String name){this.name = name;}
    public void setFather_name(String father_name){ this.father_name = father_name;}
    public void setDob(String dob){this.dob = dob;}
    public void setNationality(String nationality){this.nationality = nationality;}
    public void setLanguages(String languages){this.languages = languages;}
    public void setGender(String gender){this.gender = gender;}
    public void setMarital_status(String marital_status){this.marital_status = marital_status;}
    public void setObjective(String objective) { this.objective = objective; }
    public void setHobbies(String hobbies) { this.hobbies = hobbies; }
    public void setInterests(String interests) { this.interests = interests; }
    public void setSkills(String skills) { this.skills = skills; }
    public void setQualification(Qualification qualification){this.qualification = new Qualification(qualification);}
    public void setExperience(Experience experience) { this.experience = new Experience(experience); }
    public void setContactDetails(ContactDetails contactDetails){this.contactDetails = new ContactDetails(contactDetails);}
    public void setReference(Reference reference) { this.reference = new Reference(reference); }
    public void setProjects(Projects projects) { this.projects = new Projects(projects); }
    public void setImage(Bitmap image) { this.image = image; }
    public void setQualificationsList(ArrayList<Qualification> qualificationsList) {
        this.qualificationsList = new ArrayList<Qualification>(qualificationsList);
    }
    public void setExperiencesList(ArrayList<Experience> experiencesList) {
        this.experiencesList = new ArrayList<Experience>(experiencesList);
    }
    public void setProjectsList(ArrayList<Projects> projectsList) {
        this.projectsList = new ArrayList<Projects>(projectsList);
    }

    //getters
    public String getName(){return this.name;}
    public String getFather_name(){return this.father_name;}
    public String getDob(){return this.dob;}
    public String getNationality(){return this.nationality;}
    public String getLanguages(){return this.languages;}
    public String getGender(){return this.gender;}
    public String getMarital_status(){return this.marital_status;}
    public String getObjective() { return objective; }
    public String getHobbies() { return hobbies; }
    public String getInterests() { return interests; }
    public String getSkills() { return skills; }
    public Qualification getQualification(){return this.qualification;}
    public Experience getExperience() { return experience; }

    public Reference getReference() {
        if(reference == null){
            reference = new Reference();
        }
        return reference;
    }

    public ContactDetails getContactDetails() {
        if(contactDetails == null){
            contactDetails = new ContactDetails();
        }
        return contactDetails; }
    public Projects getProjects() { return projects; }
    public Bitmap getImage() { return image; }

    public ArrayList<Qualification> getQualificationsList() {
        if(qualificationsList == null){
            qualificationsList = new ArrayList<Qualification>();
        }
        return qualificationsList;
    }
    public ArrayList<Experience> getExperiencesList() {
        if(experiencesList == null){
            experiencesList = new ArrayList<Experience>();
        }
        return experiencesList;
    }
    public ArrayList<Projects> getProjectsList() {
        if(projectsList == null){
            projectsList = new ArrayList<Projects>();
        }
        return projectsList;
    }
}