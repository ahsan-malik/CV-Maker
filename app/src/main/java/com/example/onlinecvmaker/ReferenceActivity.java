package com.example.onlinecvmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.example.onlinecvmaker.dataModels.Reference;

public class ReferenceActivity extends AppCompatActivity {

    MyCV personCV;
    EditText nameView, designationView, orgnizationView, mailView, phoneView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reference);

        nameView = findViewById(R.id.referenceNameID);
        designationView = findViewById(R.id.refenenceDesignation);
        orgnizationView = findViewById(R.id.referenceOrgnization);
        mailView = findViewById(R.id.referenceEmail);
        phoneView = findViewById(R.id.referencePhone);

        personCV = (MyCV) getApplication();
        if(MyCV.referenceInfoSaved){
            loadSavedInfo();
        }


    }

    public void saveInfo(View view){
        String name = nameView.getText().toString();
        String designation = designationView.getText().toString();
        String orgnization = orgnizationView.getText().toString();
        String email = mailView.getText().toString();
        String phone = phoneView.getText().toString();

        Reference reference = new Reference(name, designation, orgnization, email, phone);
        personCV.getPerson().setReference(reference);
        MyCV.referenceInfoSaved = true;

    }

    private void loadSavedInfo(){
        Reference reference = personCV.getPerson().getReference();
        nameView.setText(reference.getRefName());
        designationView.setText(reference.getRefDesignation());
        orgnizationView.setText(reference.getRefOrgnization());
        mailView.setText(reference.getRefMail());
        phoneView.setText(reference.getRefPhone());
    }
}
