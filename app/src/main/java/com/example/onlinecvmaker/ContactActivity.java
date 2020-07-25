package com.example.onlinecvmaker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.onlinecvmaker.dataModels.ContactDetails;

public class ContactActivity extends AppCompatActivity {
    MyCV personCV;
    EditText cellNoView, emailView, addressView, cityView, provinceView, countryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        cellNoView = findViewById(R.id.cellNo);
        emailView = findViewById(R.id.email);
        addressView = findViewById(R.id.address);
        cityView = findViewById(R.id.city);
        provinceView = findViewById(R.id.province);
        countryView = findViewById(R.id.country);

        personCV = (MyCV) getApplication();
        if(MyCV.contactInfoSaved){
            loadSavedInfo();
        }
    }

    public void saveInfo(View view){
        String cellNo = cellNoView.getText().toString();
        String email = emailView.getText().toString();
        String address = addressView.getText().toString();
        String city = cityView.getText().toString();
        String province = provinceView.getText().toString();
        String country = countryView.getText().toString();

        ContactDetails contactDetails = new ContactDetails(cellNo, email, address, city, province, country);
        personCV.getPerson().setContactDetails(contactDetails);
        MyCV.contactInfoSaved = true;

    }

    private void loadSavedInfo(){
        cellNoView.setText(personCV.getPerson().getContactDetails().getCellNumber());
        emailView.setText(personCV.getPerson().getContactDetails().getEmail());
        addressView.setText(personCV.getPerson().getContactDetails().getAddress());
        cityView.setText(personCV.getPerson().getContactDetails().getCity());
        provinceView.setText(personCV.getPerson().getContactDetails().getProvince());
        countryView.setText(personCV.getPerson().getContactDetails().getCountry());
    }
}
