package com.anchor.erp.myfuelapp.Activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Adapters.ConsumptionsAdapter;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.example.nbs.myfuelapp.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Consumption extends AppCompatActivity {

    private ListView listView;
    private SessionPreferences sessionPreferences;
    private AppDB appDB;
    private List<FuelCar> usages = new ArrayList<>();
    private String TAG = "Consumption";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_consumption);
        sessionPreferences = new SessionPreferences(Consumption.this);
        Integer [] id = {sessionPreferences.getLoggedInUser().getId()};
        listView = findViewById(R.id.consumptions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        usages = sessionPreferences.getUsages(sessionPreferences.getNumUsages());

        Set<String> regs = new HashSet<>();
        for (FuelCar f:usages){
            regs.add(f.getVehicle().getRegno());
        }

        List<String> values = new ArrayList<>();
        List<String> reglist = new ArrayList<>();
        double total = 0;
        for (String s: regs){
            for (FuelCar f:usages){
                if (f.getVehicle().getRegno().equals(s)){
                    total += f.getAmountOfFuel();
                }
            }
            values.add(String.valueOf(total));
            reglist.add(s);
            Log.e(TAG, "onCreate: "+s+" total "+String.valueOf(total) );
        }

        ConsumptionsAdapter adapter = new ConsumptionsAdapter(Consumption.this,reglist,values);
        listView.setAdapter(adapter);
    }

}
