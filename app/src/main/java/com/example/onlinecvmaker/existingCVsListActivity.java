package com.example.onlinecvmaker;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinecvmaker.DBRelatedCode.DBHelper;

import java.io.File;

public class existingCVsListActivity extends AppCompatActivity {
    DBHelper dbHelper;
    SQLiteDatabase db;

    String[] files;

    MyCV personCV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_existing_cvs_list);
        personCV = (MyCV) getApplication();

        loadCVList();
    }

    private void loadCVList(){
        try {
            files = DBHelper.getFileList(this);
            if(files.length == 0){
                TextView listBoxTitleText = findViewById(R.id.listBoxTitle);
                listBoxTitleText.setText("No Record Found");
                return;
            }
            dbHelper = new DBHelper(this);
            db = dbHelper.getReadableDatabase();
        }catch (Exception e){
            showToast("error in loadlist "+ e.toString());
        }

        inflateItem();
    }
    private void inflateItem(){
        try {
            LinearLayout parentView = findViewById(R.id.listBox);
            for (int i = 0; i < files.length; i++) {
                View newItem = getLayoutInflater().inflate(R.layout.inflator_cvlist, null);

                newItem.setId(i);
                TextView cvName = newItem.findViewById(R.id.cvNameText);
                cvName.setText(files[i]);

                parentView.addView(newItem);
            }
        }catch (Exception e){
            showToast("Error in inflate "+e.toString());
            Log.d("inflate_error", e.toString());
        }

    }

    //edit cv btn functionality
    public void editCV(View view){
        try {
            View parenView = (View) view.getParent();
            TextView fileNameView = parenView.findViewById(R.id.cvNameText);
            String fileName = fileNameView.getText().toString();

            dbHelper.getCVInfoFromDB(db, fileName, personCV, this);
            MyCV.CVname = fileName;
            setFlags(true);
            startActivity(new Intent(this, CVCreatingActivity.class));
        }catch (Exception e){
            showToast("error edit cv"+e.toString());
            Log.d("edit", e.toString());
        }
    }
    private void setFlags(boolean trueORfalse){

        MyCV.personInfoSaved = trueORfalse;
        MyCV.objectiveSaved = trueORfalse;
        MyCV.contactInfoSaved = trueORfalse;
        MyCV.referenceInfoSaved = trueORfalse;
        MyCV.otherInfoSaved = trueORfalse;
        MyCV.savedToDB = trueORfalse;
    }

    //delete cv Btn functionality
    public void deleteCVBtn(View view){
        final View btnView = view;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Do You Want to Delete this File?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCV(btnView);
            }
        });

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private void deleteCV(View view){
        View parenView = (View) view.getParent();
        TextView fileNameView = parenView.findViewById(R.id.cvNameText);
        String fileName = fileNameView.getText().toString();

        dbHelper.deleteCVfromDB(db, fileName);

        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/"+fileName+".pdf");
        if(file.exists()){
            file.delete();
        }

        LinearLayout listBox = findViewById(R.id.listBox);
        listBox.removeView(parenView);

        if(MyCV.CVname.equalsIgnoreCase(fileName)){
            MyCV.reset();
        }

    }

    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
