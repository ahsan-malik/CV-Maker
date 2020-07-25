package com.example.onlinecvmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecvmaker.dataModels.Experience;

public class ExperienceActivity extends AppCompatActivity {

    MyCV personCV;
    EditText orgView, designationView, dateFromView, dateToView, roleView;

    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_experience);

        //counter = MyCV.experienceCounter;

        personCV = (MyCV) getApplication();
        if(!personCV.getPerson().getExperiencesList().isEmpty()){
            loadSavedInfo();
        }
    }

    public void saveInfo(View view){
        //MyCV.experienceCounter = counter;
        try {
            personCV.getPerson().getExperiencesList().clear();
            getAllChildViews(findViewById(R.id.infoBox));
            for(int i = 0; i<counter; i++) {
                if (i != 0){
                    View inflatedView = findViewById(i);
                    getAllChildViews(inflatedView);
                }
                String orgnization = orgView.getText().toString();
                String designation = designationView.getText().toString();
                String dateFrom = dateFromView.getText().toString();
                String dateTo = dateToView.getText().toString();
                String role = roleView.getText().toString();

                Experience experience = new Experience(orgnization, designation, dateFrom, dateTo, role);
                personCV.getPerson().getExperiencesList().add(experience);
            }
            //MyCV.experienceInfoSaved = true;
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "something wrong in save info" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSavedInfo(){
        getAllChildViews(findViewById(R.id.infoBox));
        try {
            counter = personCV.getPerson().getExperiencesList().size();
            for (int i = 0; i < counter; i++) {
                Experience experience = personCV.getPerson().getExperiencesList().get(i);
                if (i != 0) {
                    inflateView(i, true);
                }

                orgView.setText(experience.getOrgnization());
                designationView.setText(experience.getDesignation());
                dateFromView.setText(experience.getDateFrom());
                dateToView.setText(experience.getDateTo());
                roleView.setText(experience.getRole());

            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "error loadsaved info "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //Addmore btn functionality
    public void addMoreFields(View view){
        try {

            inflateView(counter, false);
            //Toast.makeText(getApplicationContext(), Integer.toString(newFields.getId()), Toast.LENGTH_LONG).show();
            counter++;
            //MyCV.experienceCounter++;
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "something wrong "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void getAllChildViews(View parent) {
        orgView = (EditText) parent.findViewById(R.id.org);
        designationView = (EditText) parent.findViewById(R.id.designation);
        dateFromView = (EditText) parent.findViewById(R.id.dateFrom);
        dateToView = (EditText) parent.findViewById(R.id.dateTo);
        roleView = (EditText) parent.findViewById(R.id.role);
    }

    private void inflateView(int setIntegerId, boolean getNewViewChild){
        View newFields = getLayoutInflater().inflate(R.layout.inflator_experience, null);
        newFields.setId(setIntegerId);

        TextView textView = newFields.findViewById(R.id.textView);
        int n = newFields.getId();
        String number = Integer.toString(n+1);
        textView.setText("Experience "+number);

        if(getNewViewChild){
            getAllChildViews(newFields);
        }

        LinearLayout originalInfoBox = (LinearLayout) findViewById(R.id.infoBox);
        originalInfoBox.addView(newFields);
    }
}
