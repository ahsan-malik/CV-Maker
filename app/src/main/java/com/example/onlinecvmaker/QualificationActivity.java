package com.example.onlinecvmaker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecvmaker.dataModels.Qualification;

public class QualificationActivity extends AppCompatActivity {
    MyCV personCV;
    EditText courseView, instituteView, gradeView, yearView;

    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qualification);

        personCV = (MyCV) getApplication();
        if(!personCV.getPerson().getQualificationsList().isEmpty()){
            loadSavedInfo();
        }
    }

    public void saveInfo(View view){
        try {
            personCV.getPerson().getQualificationsList().clear();
            getAllChildViews(findViewById(R.id.infoBox));
            for(int i = 0; i < counter; i++) {
                if (i != 0){
                    View inflatedView = findViewById(i);
                    getAllChildViews(inflatedView);
                }
                String course = courseView.getText().toString();
                String institute = instituteView.getText().toString();
                String grade = gradeView.getText().toString();
                String year = yearView.getText().toString();

                Qualification qualification = new Qualification(course, institute, grade, year);
                personCV.getPerson().getQualificationsList().add(qualification);
            }
            //MyCV.qualificationInfoSaved = true;
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "something wrong in save info" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSavedInfo(){
        getAllChildViews(findViewById(R.id.infoBox));
        try {
            counter = personCV.getPerson().getQualificationsList().size();
            for (int i = 0; i < counter; i++) {
                Qualification qualification = personCV.getPerson().getQualificationsList().get(i);
                if (i != 0) {
                    inflateView(i, true);
                }

                courseView.setText(qualification.getCourse());
                instituteView.setText(qualification.getInstitute());
                gradeView.setText(qualification.getGrade());
                yearView.setText(qualification.getYear());

            }
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "error loadsaved info "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    //Addmore btn functionality
    public void addMoreQualification(View view){
        try {
            inflateView(counter, false);
            counter++;

            //Toast.makeText(getApplicationContext(), Integer.toString(newFields.getId()), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "something wrong "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void getAllChildViews(View parent){
        courseView = (EditText) parent.findViewById(R.id.course);
        instituteView = (EditText) parent.findViewById(R.id.institute);
        gradeView = (EditText) parent.findViewById(R.id.grade);
        yearView = (EditText) parent.findViewById(R.id.year);
    }

    private void inflateView(int setIntegerId, boolean getNewViewChild){
        View newFields = getLayoutInflater().inflate(R.layout.qualification_inflator, null);
        newFields.setId(setIntegerId);

        TextView textView = newFields.findViewById(R.id.textView);
        int n = newFields.getId();
        String number = Integer.toString(n+1);
        textView.setText("Qualification "+number);

        if(getNewViewChild){
            getAllChildViews(newFields);
        }

        LinearLayout originalInfoBox = (LinearLayout) findViewById(R.id.infoBox);
        originalInfoBox.addView(newFields);
    }
}