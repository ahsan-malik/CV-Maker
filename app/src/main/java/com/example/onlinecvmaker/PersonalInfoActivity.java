package com.example.onlinecvmaker;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.IOException;
import java.util.Calendar;

public class PersonalInfoActivity extends AppCompatActivity {
    //Person person;
    MyCV personCV;
    private EditText nameView, fatherNameView, dobView, nationalityView, languageView;
    private RadioGroup radioGenderGroup, radioStatusGroup;

    private ImageView imageView;
    private static final int PICK_IMAGE = 1;

    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);

        nameView = (EditText) findViewById(R.id.name);
        fatherNameView = (EditText) findViewById(R.id.fatherName);
        dobView = (EditText) findViewById(R.id.dob);
        nationalityView = (EditText) findViewById(R.id.nationality);
        languageView = (EditText) findViewById(R.id.languages);
        imageView = findViewById(R.id.image);

        radioGenderGroup = (RadioGroup) findViewById(R.id.radioGender);
        radioStatusGroup = (RadioGroup) findViewById(R.id.radioMarital);

        personCV = (MyCV) getApplication();
        if(MyCV.personInfoSaved){
            loadLastSave();
        }

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;
                String date = day + "/" + month + "/" + year;
                dobView.setText(date);
            }
        };
    }

    //onclick save button
    public void saveInfo(View view){
        RadioButton radioGender = findViewById(radioGenderGroup.getCheckedRadioButtonId());
        RadioButton radioStatus = findViewById(radioStatusGroup.getCheckedRadioButtonId());
        try {
            String name = nameView.getText().toString();
            String fatherName = fatherNameView.getText().toString();
            String dob = dobView.getText().toString();
            String nationality = nationalityView.getText().toString();
            String languages = languageView.getText().toString();
            String gender = radioGender.getText().toString();
            String maritalStatus = radioStatus.getText().toString();


            //person = new Person(name, fatherName, dob, nationality, languages, gender, maritalStatus);
            //personCV.setPerson(person);
            personCV.getPerson().setName(name);
            personCV.getPerson().setFather_name(fatherName);
            personCV.getPerson().setDob(dob);
            personCV.getPerson().setNationality(nationality);
            personCV.getPerson().setLanguages(languages);
            personCV.getPerson().setGender(gender);
            personCV.getPerson().setMarital_status(maritalStatus);
            Toast.makeText(getApplicationContext(), personCV.getPerson().getName() + personCV.getPerson().getGender(), Toast.LENGTH_SHORT).show();

            MyCV.personInfoSaved = true;
            //saveToDB();
        }
        catch (Exception ex){
            Toast.makeText(getApplicationContext(), "something wrong"+ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    //clickable
    public void getImageFromGellary(View view){
        Intent gallery = new Intent();
        gallery.setType("image/*");
        gallery.setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(gallery, "Select Photo"), PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK && data != null){
            Uri imageUri = data.getData();
            try {
                Bitmap image = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                personCV.getPerson().setImage(image);
                imageView.setImageBitmap(image);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //datepicker button
    public void datePicker(View view){

        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.Theme_MaterialComponents_Dialog_MinWidth, onDateSetListener, year, month, day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        datePickerDialog.show();

    }

    private void loadLastSave(){
        nameView.setText(personCV.getPerson().getName());
        fatherNameView.setText(personCV.getPerson().getFather_name());
        dobView.setText(personCV.getPerson().getDob());
        nationalityView.setText(personCV.getPerson().getNationality());
        languageView.setText(personCV.getPerson().getLanguages());
        if(personCV.getPerson().getImage() != null) {
            imageView.setImageBitmap(personCV.getPerson().getImage());
        }
        if(personCV.getPerson().getGender().equals("Female")){
            //radioGenderGroup.clearCheck();
            radioGenderGroup.check(R.id.radioFemale);
        }
        if(personCV.getPerson().getMarital_status().equals("Married")){
            radioStatusGroup.check(R.id.radioMarried);
        }

    }
}
