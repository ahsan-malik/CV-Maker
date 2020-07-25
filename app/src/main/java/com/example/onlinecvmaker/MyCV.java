package com.example.onlinecvmaker;

import android.app.Application;

import com.example.onlinecvmaker.dataModels.Person;

public class MyCV extends Application {
    public static String CVname = "";

    public static boolean personInfoSaved = false;
    public static boolean contactInfoSaved = false;
    public static boolean referenceInfoSaved = false;
    public static boolean objectiveSaved = false;
    public static boolean otherInfoSaved = false;
    public static boolean savedToDB = false;

    private static Person person;

    public Person getPerson(){
        if(person == null) {
            person = new Person();
        }
        return person;
    }

    public void clearPersonObj(){
        person = null;
    }

    public static void reset(){
        CVname = "";

        personInfoSaved = false;
        contactInfoSaved = false;
        referenceInfoSaved = false;
        objectiveSaved = false;
        otherInfoSaved = false;
        savedToDB = false;

        person = null;
    }
}
