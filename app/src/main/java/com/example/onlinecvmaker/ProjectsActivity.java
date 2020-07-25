package com.example.onlinecvmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecvmaker.dataModels.Projects;

public class ProjectsActivity extends AppCompatActivity {
    MyCV personCV;
    //Projects projects;
    EditText titleView, descriptionView, durationView;

    int counter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_projects);

        personCV = (MyCV) getApplication();
        //projects = MyCV.getPerson().getProjects();
        if(!personCV.getPerson().getProjectsList().isEmpty()){
            loadSavedInfo();
        }
    }

    public void saveInfo(View view){
        try {
            personCV.getPerson().getProjectsList().clear();
            getAllChildViews(findViewById(R.id.infoBox));
            for(int i = 0; i < counter; i++) {
                if (i != 0){
                    View inflatedView = findViewById(i);
                    getAllChildViews(inflatedView);
                }
                String title = titleView.getText().toString();
                String description = descriptionView.getText().toString();
                String duration = durationView.getText().toString();

                Projects projects = new Projects(title, description, duration);
                personCV.getPerson().getProjectsList().add(projects);
            }
            //MyCV.projectInfoSaved = true;
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "something wrong in save info" + e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void loadSavedInfo(){
        getAllChildViews(findViewById(R.id.infoBox));
        try {
            counter = personCV.getPerson().getProjectsList().size();
            for (int i = 0; i < counter; i++) {
                Projects projects = personCV.getPerson().getProjectsList().get(i);
                if (i != 0) {
                    inflateView(i, true);
                }
                titleView.setText(projects.getTitle());
                descriptionView.setText(projects.getDescription());
                durationView.setText(projects.getDuration());
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
            counter++;
            //Toast.makeText(getApplicationContext(), Integer.toString(newFields.getId()), Toast.LENGTH_LONG).show();
        }
        catch (Exception e){
            Toast.makeText(getApplicationContext(), "something wrong "+e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void getAllChildViews(View parent){
        titleView = (EditText) parent.findViewById(R.id.projectTitle);
        descriptionView = (EditText) parent.findViewById(R.id.description);
        durationView = (EditText) parent.findViewById(R.id.duration);
    }

    private void inflateView(int setIntegerId, boolean getNewViewChild){
        View newFields = getLayoutInflater().inflate(R.layout.inflator_projects, null);
        newFields.setId(setIntegerId);

        TextView textView = newFields.findViewById(R.id.textView);
        int n = newFields.getId();
        String number = Integer.toString(n+1);
        textView.setText("Projects "+number);

        if(getNewViewChild){
            getAllChildViews(newFields);
        }

        LinearLayout originalInfoBox = (LinearLayout) findViewById(R.id.infoBox);
        originalInfoBox.addView(newFields);
    }
}
