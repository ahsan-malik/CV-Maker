package com.example.onlinecvmaker;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.onlinecvmaker.DBRelatedCode.DBHelper;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(!MyCV.CVname.isEmpty()){
            findViewById(R.id.resumeCurrentBtn).setVisibility(View.VISIBLE);
        }
    }

    public void editExistingCV(View view){
        findViewById(R.id.resumeCurrentBtn).setVisibility(View.VISIBLE);
        startActivity(new Intent(MainActivity.this, existingCVsListActivity.class));
    }

    public void createNewOne(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Enter CV Name");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String nameText = input.getText().toString();
                if(!isFileExist(nameText)) {
                    if (!nameText.isEmpty()) {
                        if (!MyCV.CVname.isEmpty()) {
                            MyCV.reset();
                        }
                        findViewById(R.id.resumeCurrentBtn).setVisibility(View.VISIBLE);
                        MyCV.CVname = nameText;
                        startActivity(new Intent(MainActivity.this, CVCreatingActivity.class));
                    } else {
                        showToast("Write Name First");
                    }
                }else{
                    showToast("File already exist!");
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
    private boolean isFileExist(String fileName){
        String[] files = DBHelper.getFileList(MainActivity.this);
        for (String file : files) {
            if (fileName.equalsIgnoreCase(file)) {
                return true;
            }
        }
        return false;
    }

    public void resumeCurrentCV(View view){
        if(!MyCV.CVname.isEmpty()) {
            startActivity(new Intent(this, CVCreatingActivity.class));
        }else{
            showToast("File was deleted");
        }
    }


    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}
