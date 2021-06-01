package com.anchor.erp.myfuelapp.Activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.example.nbs.myfuelapp.R;

public class Balance extends AppCompatActivity {

    private TextView bundle, account, points;
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);

//        bundle = findViewById(R.id.tvbalance);
        account = findViewById(R.id.tvaccount);
        points = findViewById(R.id.tvpoints);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sessionPreferences = new SessionPreferences(Balance.this);

        account.setText(String.format("%,.2f",sessionPreferences.getBalances().getAccount())+" Ksh");
//        bundle.setText(String.valueOf(sessionPreferences.getBalances().getBundle()));
        points.setText(String.valueOf(sessionPreferences.getBalances().getPoints()));

    }
}
