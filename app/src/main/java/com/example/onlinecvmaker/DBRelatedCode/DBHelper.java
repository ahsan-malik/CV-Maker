package com.example.onlinecvmaker.DBRelatedCode;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.example.onlinecvmaker.MyCV;
import com.example.onlinecvmaker.dataModels.ContactDetails;
import com.example.onlinecvmaker.dataModels.Experience;
import com.example.onlinecvmaker.dataModels.Projects;
import com.example.onlinecvmaker.dataModels.Qualification;
import com.example.onlinecvmaker.dataModels.Reference;

import java.io.ByteArrayOutputStream;

public class DBHelper extends SQLiteOpenHelper {

    private static final String db_name = "user_database";
    private static final int version = 1;

    //private static final String foreign_key = "person_id INTEGER, FOREIGN KEY(person_id) REFERENCES Person(_id)";
    private static final String foreign_key = "CV_Name TEXT, FOREIGN KEY(CV_Name) REFERENCES Person(File_Name)";

    //Default costructor
    public DBHelper(Context context){
        super(context, db_name, null, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String personTable = "CREATE TABLE Person(_id INTEGER, Name TEXT, Father_Name TEXT, DOB TEXT, Nationality TEXT, Languages TEXT, Gender TEXT, Marital_Status TEXT, File_Name TEXT PRIMARY KEY)";
        String objectiveTable = "CREATE TABLE ObjectiveTable(_id INTEGER PRIMARY KEY AUTOINCREMENT, Objective TEXT, CV_Name TEXT, FOREIGN KEY(CV_Name) REFERENCES Person(File_Name))";
        String qualificationTable = "CREATE TABLE Qualification(_id INTEGER PRIMARY KEY AUTOINCREMENT, Course TEXT, Institute TEXT, Grade TEXT, Year TEXT," + foreign_key + ")";
        String experienceTable = "CREATE TABLE Experience(_id INTEGER PRIMARY KEY AUTOINCREMENT, Orgnization TEXT, Designation TEXT, Date_From TEXT, Date_To TEXT, Role TEXT," + foreign_key + ")";
        String contactTable = "CREATE TABLE Contact_Details(_id INTEGER PRIMARY KEY AUTOINCREMENT, Phone TEXT, Email TEXT, Address TEXT, City TEXT, Province TEXT, Country TEXT," + foreign_key + ")";
        String referenceTable = "CREATE TABLE Reference(_id INTEGER PRIMARY KEY AUTOINCREMENT, Ref_Name TEXT, Ref_Designation TEXT, Ref_Orgnization TEXT, Ref_Mail TEXT, Ref_Phone TEXT," + foreign_key + ")";
        String projectTable = "CREATE TABLE Projects(_id INTEGER PRIMARY KEY AUTOINCREMENT, Title TEXT, Description TEXT, Duration TEXT," + foreign_key + ")";
        String otherTable = "CREATE TABLE Others(_id INTEGER PRIMARY KEY AUTOINCREMENT, Skills TEXT, Interests TEXT, Hobbies TEXT," + foreign_key + ")";
        String imagesTable = "CREATE TABLE Images(_id INTEGER PRIMARY KEY AUTOINCREMENT, Image BLOB," + foreign_key + ")";

        db.execSQL(personTable);
        db.execSQL(objectiveTable);
        db.execSQL(qualificationTable);
        db.execSQL(experienceTable);
        db.execSQL(contactTable);
        db.execSQL(referenceTable);
        db.execSQL(projectTable);
        db.execSQL(otherTable);
        db.execSQL(imagesTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void saveCVtoDB(SQLiteDatabase db, MyCV personCV, Context context){
        try {
            //DBHelper dbHelper = new DBHelper(this);
            //db = dbHelper.getWritableDatabase();

            String fileName = MyCV.CVname;

            ContentValues values = new ContentValues();
            values.put("Name", personCV.getPerson().getName());
            values.put("Father_Name", personCV.getPerson().getFather_name());
            values.put("DOB", personCV.getPerson().getDob());
            values.put("Nationality", personCV.getPerson().getNationality());
            values.put("Languages", personCV.getPerson().getLanguages());
            values.put("Gender", personCV.getPerson().getGender());
            values.put("Marital_Status", personCV.getPerson().getMarital_status());
            values.put("File_Name", fileName);
            if(!MyCV.savedToDB){
                db.insert("Person", null, values);
            }
            else{
                //count = DatabaseUtils.queryNumEntries(db, "Person");
                //db.update("Person", values, "_id = ?", new String[]{Long.toString(count)});
                db.update("Person", values, "File_Name = ?", new String[]{fileName});
                clearChildTableRecorrd(db);
            }
            values.clear();

            //count = DatabaseUtils.queryNumEntries(db, "Person");

            //insetting img to db as byteArray;
            if(personCV.getPerson().getImage() != null) {
                Bitmap orignal = personCV.getPerson().getImage();
                Bitmap bmp = orignal.copy(orignal.getConfig(), true);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                //bmp.recycle();
                stream.close();

                values.put("Image", byteArray);
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("Images", null, values);
                values.clear();
            }

            //inserting objective
            if(personCV.getPerson().getObjective() != null) {
                values.put("Objective", personCV.getPerson().getObjective());
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("ObjectiveTable", null, values);
                values.clear();
            }

            //inserting Qualification
            for (int i = 0; i < personCV.getPerson().getQualificationsList().size(); i++) {
                Qualification qualification = personCV.getPerson().getQualificationsList().get(i);
                values.put("Course", qualification.getCourse());
                values.put("Institute", qualification.getInstitute());
                values.put("Grade", qualification.getGrade());
                values.put("Year", qualification.getYear());
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("Qualification", null, values);
                values.clear();
            }


            //inserting experience
            for(int i = 0; i<personCV.getPerson().getExperiencesList().size(); i++){
                Experience experience = personCV.getPerson().getExperiencesList().get(i);
                values.put("Orgnization", experience.getOrgnization());
                values.put("Designation", experience.getDesignation());
                values.put("Date_From", experience.getDateFrom());
                values.put("Date_To", experience.getDateTo());
                values.put("Role", experience.getRole());
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("Experience", null, values);
                values.clear();
            }

            //inserting contact details
            if(personCV.getPerson().getContactDetails() != null) {
                ContactDetails contactDetails = personCV.getPerson().getContactDetails();
                values.put("Phone", contactDetails.getCellNumber());
                values.put("Email", contactDetails.getEmail());
                values.put("Address", contactDetails.getAddress());
                values.put("City", contactDetails.getCity());
                values.put("Province", contactDetails.getProvince());
                values.put("Country", contactDetails.getCountry());
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("Contact_Details", null, values);
                values.clear();
            }

            //inserting reference
            if(personCV.getPerson().getReference() != null) {
                Reference reference = personCV.getPerson().getReference();
                values.put("Ref_Name", reference.getRefName());
                values.put("Ref_Designation", reference.getRefDesignation());
                values.put("Ref_Orgnization", reference.getRefOrgnization());
                values.put("Ref_Mail", reference.getRefMail());
                values.put("Ref_Phone", reference.getRefPhone());
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("Reference", null, values);
                values.clear();
            }

            //inserting projects
            for(int i = 0; i<personCV.getPerson().getProjectsList().size(); i++){
                Projects projects = personCV.getPerson().getProjectsList().get(i);
                values.put("Title", projects.getTitle());
                values.put("Description", projects.getDescription());
                values.put("Duration", projects.getDuration());
                //values.put("person_id", count);
                values.put("CV_Name", fileName);
                db.insert("Projects", null, values);
                values.clear();
            }

            //inserting others
            values.put("Skills", personCV.getPerson().getSkills());
            values.put("Interests", personCV.getPerson().getInterests());
            values.put("Hobbies", personCV.getPerson().getHobbies());
            //values.put("person_id", count);
            values.put("CV_Name", fileName);
            db.insert("Others", null, values);
            values.clear();

            db.close();
            MyCV.savedToDB = true;

            //Toast.makeText(getApplicationContext(), "saveCV succeed", Toast.LENGTH_LONG).show();
            showToast("cv saved", context);
        }
        catch (Exception e){
            //Toast.makeText(getApplicationContext(), "error saveCV "+e.toString(), Toast.LENGTH_LONG).show();
            showToast("error cv not save "+e.toString(), context);
        }
    }
    private void clearChildTableRecorrd(SQLiteDatabase db){
        //first delete child tables reccord
        //db.delete("Images", "person_id = ?", new String[]{Long.toString(count)});
        db.delete("Images", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("ObjectiveTable", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("Qualification", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("Experience", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("Contact_Details", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("Reference", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("Projects", "CV_Name = ?", new String[]{MyCV.CVname});
        db.delete("Others", "CV_Name = ?", new String[]{MyCV.CVname});
    }


    public void getCVInfoFromDB(SQLiteDatabase db, String fileName, MyCV personCV, Context context){
        personCV.clearPersonObj();
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Person WHERE File_Name = ?", new String[]{fileName});
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    personCV.getPerson().setName(cursor.getString(1));
                    personCV.getPerson().setFather_name(cursor.getString(2));
                    personCV.getPerson().setDob(cursor.getString(3));
                    personCV.getPerson().setNationality(cursor.getString(4));
                    personCV.getPerson().setLanguages(cursor.getString(5));
                    personCV.getPerson().setGender(cursor.getString(6));
                    personCV.getPerson().setMarital_status(cursor.getString(7));

                } while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            showToast("error in gettign record"+ e.toString(), context);
            Log.d("getcv", e.toString());
        }

        //getting objective
        try {
            Cursor cursor = db.rawQuery("SELECT Objective FROM ObjectiveTable WHERE CV_Name = ?", new String[]{fileName});
            if(cursor != null && cursor.moveToFirst()){
                personCV.getPerson().setObjective(cursor.getString(0));
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("obj", e.toString());
        }

        //getting image
        try {
            Cursor cursor = db.rawQuery("SELECT Image FROM Images WHERE CV_Name = ?", new String[]{fileName});
            if(cursor != null && cursor.moveToFirst()){
                //personCV.getPerson().setObjective(cursor.getString(0));
                Bitmap imageBitmap = BitmapFactory.decodeByteArray(cursor.getBlob(0), 0, cursor.getBlob(0).length);
                personCV.getPerson().setImage(imageBitmap);
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("img", e.toString());
        }

        //get contact details
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Contact_Details WHERE CV_Name = ?", new String[]{fileName});

            if(cursor != null && cursor.moveToFirst()){
                ContactDetails contactDetails = new ContactDetails(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5),
                        cursor.getString(6)
                );
                personCV.getPerson().setContactDetails(contactDetails);
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("contact", e.toString());
        }

        //get reference
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Reference WHERE CV_Name = ?", new String[]{fileName});

            if(cursor != null && cursor.moveToFirst()){
                Reference reference = new Reference(
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getString(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                personCV.getPerson().setReference(reference);
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("contact", e.toString());
        }

        //get other skills hobbby etc
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Others WHERE CV_Name = ?", new String[]{fileName});

            if(cursor != null && cursor.moveToFirst()){
                personCV.getPerson().setSkills(cursor.getString(1));
                personCV.getPerson().setInterests(cursor.getString(2));
                personCV.getPerson().setHobbies(cursor.getString(3));
                cursor.close();
            }

        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("others", e.toString());
        }

        //get Experience
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Experience WHERE CV_Name = ?", new String[]{fileName});

            if(cursor != null && cursor.moveToFirst()){
                do {
                    Experience experience = new Experience(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4),
                            cursor.getString(5)
                    );
                    personCV.getPerson().getExperiencesList().add(experience);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("Experience", e.toString());
        }

        //get Projects
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Projects WHERE CV_Name = ?", new String[]{fileName});

            if(cursor != null && cursor.moveToFirst()){
                do {
                    Projects projects = new Projects(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3)
                    );
                    personCV.getPerson().getProjectsList().add(projects);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("Project", e.toString());
        }

        //get Qualification
        try {
            Cursor cursor = db.rawQuery("SELECT * FROM Qualification WHERE CV_Name = ?", new String[]{fileName});

            if(cursor != null && cursor.moveToFirst()){
                do {
                    Qualification qualification = new Qualification(
                            cursor.getString(1),
                            cursor.getString(2),
                            cursor.getString(3),
                            cursor.getString(4)
                    );
                    personCV.getPerson().getQualificationsList().add(qualification);
                }while (cursor.moveToNext());
                cursor.close();
            }
        }catch (Exception e){
            showToast(e.toString(), context);
            Log.d("qualifictaion", e.toString());
        }

    }


    public void deleteCVfromDB(SQLiteDatabase db, String fileName){
        db.delete("Images", "CV_Name = ?", new String[]{fileName});
        db.delete("ObjectiveTable", "CV_Name = ?", new String[]{fileName});
        db.delete("Qualification", "CV_Name = ?", new String[]{fileName});
        db.delete("Experience", "CV_Name = ?", new String[]{fileName});
        db.delete("Contact_Details", "CV_Name = ?", new String[]{fileName});
        db.delete("Reference", "CV_Name = ?", new String[]{fileName});
        db.delete("Projects", "CV_Name = ?", new String[]{fileName});
        db.delete("Others", "CV_Name = ?", new String[]{fileName});
        db.delete("Person", "File_Name = ?", new String[]{fileName});
    }


    public static String[] getFileList(Context context){
        DBHelper dbHelper = new DBHelper(context);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] filesName = new String[(int)DatabaseUtils.queryNumEntries(db, "Person")];

        Cursor cursor = db.rawQuery("SELECT File_Name FROM Person", new String[]{});

        if(cursor != null && cursor.moveToFirst())
        {
            int i = 0;
            do
            {
                filesName[i] = cursor.getString(0);
                i++;

            }while (cursor.moveToNext());

            cursor.close();

        }
        db.close();

        return filesName;
    }


    private void showToast(String message, Context context){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
