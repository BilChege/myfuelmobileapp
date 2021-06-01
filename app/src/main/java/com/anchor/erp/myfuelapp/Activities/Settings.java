package com.anchor.erp.myfuelapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundAllMakes;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Fragments.DeactivateCars;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.example.nbs.myfuelapp.R;

public class Settings extends AppCompatActivity {

    private static final String TAG = "Settings";
    private CardView mycars,mypin,mypassword,activate;
    private AppDB appDB;
    private LinearLayout container;
    private MobileUser loggedIn;
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        sessionPreferences = new SessionPreferences(this);
        container = findViewById(R.id.container);
        appDB = new AppDB(Settings.this);
        mycars = findViewById(R.id.optionmycars);
        mypin = findViewById(R.id.optionmanagepin);
        activate = findViewById(R.id.activate);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mypassword = findViewById(R.id.optionmanagepassword);
        loggedIn = sessionPreferences.getLoggedInUser();

        new BackGroundAllMakes(Settings.this).execute();


        mycars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,MyCars.class));
            }
        });

        mypin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,ChangePin.class));
            }
        });

        mypassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Settings.this,ResetPassword.class));
            }
        });

        activate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment previous = fragmentManager.findFragmentByTag("dialog");
                if (previous != null){
                    fragmentTransaction.remove(previous);
                }
                DialogFragment deactivate = new DeactivateCars();
                deactivate.show(fragmentManager,"dialog");
            }
        });
        if (loggedIn.getRole().equals("Corp-User")){
            container.removeView(activate);
        }
    }

}
