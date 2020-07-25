package com.example.onlinecvmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class OtherActivity extends AppCompatActivity {

    MyCV personCV;
    EditText skillsView, interestView, hobbiesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other);

        personCV = (MyCV) getApplication();

        skillsView = findViewById(R.id.skill);
        interestView = findViewById(R.id.interest);
        hobbiesView = findViewById(R.id.hobby);

        if(MyCV.otherInfoSaved){
            loadSavedInfo();
        }
    }

    public void saveInfo(View view){
        String skills = skillsView.getText().toString();
        String interest = interestView.getText().toString();
        String hobbies = hobbiesView.getText().toString();

        personCV.getPerson().setSkills(skills);
        personCV.getPerson().setInterests(interest);
        personCV.getPerson().setHobbies(hobbies);

        MyCV.otherInfoSaved = true;
    }

    private void loadSavedInfo(){
        skillsView.setText(personCV.getPerson().getSkills());
        interestView.setText(personCV.getPerson().getInterests());
        hobbiesView.setText(personCV.getPerson().getHobbies());
    }
}
