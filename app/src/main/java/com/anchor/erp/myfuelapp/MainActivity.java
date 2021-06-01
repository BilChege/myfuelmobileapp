package com.anchor.erp.myfuelapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Activities.Login;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.example.nbs.myfuelapp.R;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        SharedPreferences sharedPreferences = getSharedPreferences("AppFirstTime", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//        boolean appfirsttime = sharedPreferences.getBoolean("appfirsttime",true);
//        if (appfirsttime){
//            ((ActivityManager)getSystemService(ACTIVITY_SERVICE)).clearApplicationUserData();
//            editor.putBoolean("appfirsttime",false);
//        }
        final boolean isfirstTime = new SessionPreferences(MainActivity.this).getFirstTimeStatus();
        final boolean isloggedin = new SessionPreferences(MainActivity.this).getLoggedInStatus();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isfirstTime){
                    startActivity(new Intent(MainActivity.this, Login.class));
                    finish();
                }else if (isloggedin) {
                    startActivity(new Intent(MainActivity.this, HomePage.class));
                    finish();
                } else {
                    startActivity(new Intent(MainActivity.this,Login.class));
                    finish();
                }
            }
        },1000);
    }
}
