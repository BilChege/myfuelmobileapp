package com.anchor.erp.myfuelapp.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Fragments.AllOffers;
import com.anchor.erp.myfuelapp.Fragments.RedeemOffer;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;

public class Offers extends AppCompatActivity implements Config.OffersListener {

    private static final String TAG = "Offers";
    private RecyclerView recyclerView;
    private SessionPreferences sessionPreferences;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offers);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment allOffers = new AllOffers();
        fragmentTransaction.add(R.id.container,allOffers);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void offerSelected(OffersForMobile offer) {
        sessionPreferences = new SessionPreferences(this);
        sessionPreferences.setSelected_promotion(offer);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment redeemOffer = new RedeemOffer();
        fragmentTransaction.replace(R.id.container,redeemOffer);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.container);
        if (fragment instanceof AllOffers){
            finish();
        } else {
            fragmentManager.popBackStack();
        }
    }
}
