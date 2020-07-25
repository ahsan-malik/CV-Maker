package com.example.onlinecvmaker;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //getHashkey();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        },4000);
    }

    /*void getHashkey(){
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.example.onlinecvmaker", PackageManager.GET_SIGNATURES);
            for(Signature signature : info.signatures){
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("keyhash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }*/
}
