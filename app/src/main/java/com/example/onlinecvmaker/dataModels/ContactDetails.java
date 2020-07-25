package com.example.onlinecvmaker.dataModels;

public class ContactDetails {
    private String cellNumber, email, address, city, province, country;

    //constructor
    public ContactDetails(){
        cellNumber = "not set";
        email = "";
        address = "";
        city = "";
        province = "";
        country = "";
    }
    //parameterized constructor
    public ContactDetails(String cellNumber, String email, String address, String city, String province, String country){
        this.cellNumber = cellNumber;
        this.email = email;
        this.address = address;
        this.city = city;
        this.province = province;
        this.country = country;
    }
    //copy constructor
    public ContactDetails(ContactDetails contactDetails){
        this.cellNumber = contactDetails.cellNumber;
        this.email = contactDetails.email;
        this.address = contactDetails.address;
        this.city = contactDetails.city;
        this.province = contactDetails.province;
        this.country = contactDetails.country;
    }

    //getters
    public String getCellNumber() { return cellNumber; }
    public String getAddress() { return address; }
    public String getEmail() { return email; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getCountry() { return country; }
}
