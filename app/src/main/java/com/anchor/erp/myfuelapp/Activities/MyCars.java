package com.anchor.erp.myfuelapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Adapters.MycarsAdapter;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MyCars extends AppCompatActivity {

    private static final String TAG = "MyCars";
    private FloatingActionButton btn,btnfrmempty;
    private List<Vehicle> vehicles = new ArrayList<>();
    private AppDB appDB;
    private SessionPreferences preferences;
    private ListView mycarslist;
    private TextView defaultText;
    private RelativeLayout mycarscontainer,container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_cars);
        appDB = new AppDB(MyCars.this);
        preferences = new SessionPreferences(MyCars.this);
        MobileUser loggedIn = preferences.getLoggedInUser();
        vehicles = appDB.getUserVehicles(loggedIn.getId(),false);
        mycarscontainer = findViewById(R.id.mycarscontainer);
        container = findViewById(R.id.container);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        if (vehicles != null && !vehicles.isEmpty()){
            MycarsAdapter adapter = new MycarsAdapter(vehicles,MyCars.this);
            mycarslist = new ListView(this);
            RelativeLayout.LayoutParams forlistview = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,RelativeLayout.LayoutParams.MATCH_PARENT);
            mycarslist.setLayoutParams(forlistview);
            mycarslist.setAdapter(adapter);
            mycarscontainer.addView(mycarslist);
        } else {
            defaultText = new TextView(this);
            if (loggedIn.getRole().equals("Corp-User")){
                defaultText.setText("You have not been allocated any cars yet. Try to refresh data at the home page");
            } else {
                defaultText.setText(R.string.defaultcarstext);
            }
            RelativeLayout.LayoutParams fordefaulttext = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
            fordefaulttext.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);
            defaultText.setLayoutParams(fordefaulttext);
            mycarscontainer.addView(defaultText);
        }
        btn = findViewById(R.id.addcarfromlist);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyCars.this,CreateVehicle.class).putExtra("function","create"));
                finish();
            }
        });
        if (loggedIn.getRole().equals("Corp-User")){
            container.removeView(btn);
        }
    }
}
