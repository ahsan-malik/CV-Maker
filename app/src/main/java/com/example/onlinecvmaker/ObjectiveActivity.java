package com.example.onlinecvmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class ObjectiveActivity extends AppCompatActivity {

    MyCV personCV;
    EditText objectiveView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_objective);

        personCV = (MyCV) getApplication();
        objectiveView = findViewById(R.id.objective);
        if(MyCV.objectiveSaved){
            loadSavedObjective();
        }
    }

    //save button click method
    public void saveObjective(View view){
        String objective = objectiveView.getText().toString();
        personCV.getPerson().setObjective(objective);
        MyCV.objectiveSaved = true;
    }

    private void loadSavedObjective(){
        objectiveView.setText(personCV.getPerson().getObjective());
    }
}
