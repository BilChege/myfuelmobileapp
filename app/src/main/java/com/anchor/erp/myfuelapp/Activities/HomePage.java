package com.anchor.erp.myfuelapp.Activities;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundAllDealers;
import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundAllUsages;
import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundVehiclesForCorporateUser;
import com.anchor.erp.myfuelapp.AsyncTasks.Backgroundalloffers;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Fragments.CorporateSetPin;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.navigation.NavigationView;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private CardView payfuel,nearestpetrolstation,sambaza, buypackage, checkbalance, consumption, offers;
    private AppDB appDB;
    private TextView username,email,bundlebalance;
    private EditText accountbalance,tvcurrentpoints;
    private SessionPreferences sessionPreferences;
    private NavigationView navigationView;
    private LinearLayout ll2,ll3;
    private View headerView;
    private String TAG = "HOME PAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        appDB = new AppDB(HomePage.this);
        sessionPreferences = new SessionPreferences(HomePage.this);
        ll2 = findViewById(R.id.ll2);
        ll3 = findViewById(R.id.ll3);
        final MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        new BackGroundAllUsages(HomePage.this).execute(sessionPreferences.getLoggedInUser().getId());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appDB.onCreate(appDB.getWritableDatabase());
        String [] params = {String.valueOf(loggedIn.getId())};
        new BackGroundAllDealers(HomePage.this).execute(params);
        sambaza = findViewById(R.id.sambaza);
        payfuel = findViewById(R.id.cardpayfuel);
        buypackage = findViewById(R.id.buypkg);
        checkbalance = findViewById(R.id.chkblnc);
        consumption = findViewById(R.id.consumption);
        offers = findViewById(R.id.offers);
        nearestpetrolstation = findViewById(R.id.nearestpetrolstation);
        navigationView = findViewById(R.id.nav_view);
        accountbalance = findViewById(R.id.etbalance);
//        bundlebalance = findViewById(R.id.tvbundlebalance);
        tvcurrentpoints = findViewById(R.id.currpts);
        headerView = navigationView.getHeaderView(0);
        SharedPreferences sharedPreferences = getSharedPreferences(Config.key_purchase_status, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
        username = headerView.findViewById(R.id.navheadusername);
        email = headerView.findViewById(R.id.navheademail);
        sessionPreferences = new SessionPreferences(HomePage.this);
        buypackage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (sessionPreferences.getoffers(sessionPreferences.getNumOffers()).size() > 0){
                        startActivity(new Intent(HomePage.this,BuyBundles.class).putExtra("status",0));
                    } else {
                        new AlertDialog.Builder(HomePage.this).setTitle("No Offers")
                                .setMessage("There are Currently no offers available. Try again another time")
                                .setPositiveButton("Ok",null).show();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });
        checkbalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,Balance.class));
            }
        });
        consumption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,Consumption.class));
            }
        });
        offers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,Offers.class));
            }
        });
        nearestpetrolstation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomePage.this,MapsActivity.class));
            }
        });
        sambaza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePage.this,Sambaza.class));
            }
        });
        new Backgroundalloffers(HomePage.this).execute();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        final MobileUser loggedInser = sessionPreferences.getLoggedInUser();

        if (!new SessionPreferences(HomePage.this).getSessionvehicles(new SessionPreferences(HomePage.this).getnumvehicles()).isEmpty() && appDB.getUserVehicles(loggedInser.getId(),false) != null){
//            Log.e(TAG, "onCreate: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ Running IF in Settings" );
            int[] ids = new int[appDB.getUserVehicles(loggedInser.getId(),false).size()];
            int counter = 0;
            for (Vehicle v:appDB.getUserVehicles(loggedInser.getId(),false)){
                ids[counter] = v.getId();
//                Log.e(TAG, "onCreate: IN ARRAY "+ids[counter] );
                counter += 1;
            }
            for (Vehicle v:new SessionPreferences(HomePage.this).getSessionvehicles(new SessionPreferences(HomePage.this).getnumvehicles())){
                if (contains(ids,v.getId())){
                    continue;
                } else {
//                    Log.e(TAG, "onCreate: ID IN SettingsESSION :"+v.getId()+" ID IN DATABASE: "+ids[i] );
                    MobileUser m = new MobileUser();
                    m.setId(new SessionPreferences(HomePage.this).getLoggedInUser().getId());
                    v.setOwner(m);
                    appDB.addVehicle(v);
                }
            }
        } else if (!new SessionPreferences(HomePage.this).getSessionvehicles(new SessionPreferences(HomePage.this).getnumvehicles()).isEmpty()){
            for (Vehicle v:new SessionPreferences(HomePage.this).getSessionvehicles(new SessionPreferences(HomePage.this).getnumvehicles())){
                MobileUser m = new MobileUser();
                m.setId(new SessionPreferences(HomePage.this).getLoggedInUser().getId());
                v.setOwner(m);
                appDB.addVehicle(v);
            }
        }
        username.setText(sessionPreferences.getLoggedInUser().getFirstName()+" "+sessionPreferences.getLoggedInUser().getLastName());
        email.setText(sessionPreferences.getLoggedInUser().getEmail());

        Balances balances = sessionPreferences.getBalances();
        accountbalance.setText(String.format("%,.2f",balances.getAccount())+" Ksh");
        accountbalance.setEnabled(false);
//        bundlebalance.setText(String.valueOf((int)balances.getBundle()));
        tvcurrentpoints.setText(String.valueOf((int)balances.getPoints()));
        tvcurrentpoints.setEnabled(false);
        payfuel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<Vehicle> vehicles = appDB.getUserVehicles(loggedInser.getId(),true);
                List<Vehicle> uservehicles = new ArrayList<>();
                if (vehicles != null){
                    for (Vehicle v:vehicles){
                        if (v.getOwner().getId() == sessionPreferences.getLoggedInUser().getId() && v.getRegno() != null){
                            uservehicles.add(v);
                        }
                    }
                    if (!uservehicles.isEmpty()){
                        startActivity(new Intent(HomePage.this,FuelCarActivity.class));
                    } else {
                        Log.e(TAG, "onClick: "+loggedIn.getRole() );
                        if (loggedIn.getRole().equals("Corp-User")){
                            new AlertDialog.Builder(HomePage.this).setTitle("No cars Found")
                                    .setMessage("You have not been assigned any vehicles yet. Contact your superiors")
                                    .setPositiveButton("ok",null).show();
                        } else {
                            new AlertDialog.Builder(HomePage.this).setTitle("No cars Found")
                                    .setMessage("Please Add a Car First. Go to Settings")
                                    .setPositiveButton("ok",null).show();
                        }
                    }
                    SearchableSpinner searchableSpinner = new SearchableSpinner(HomePage.this);
                } else {
                    if (loggedIn.getRole().equals("Corp-User")){
                        new AlertDialog.Builder(HomePage.this).setTitle("No cars Found")
                                .setMessage("You have not been assigned any vehicles yet. Contact your superiors")
                                .setPositiveButton("ok",null).show();
                    } else {
                        new AlertDialog.Builder(HomePage.this).setTitle("No cars Found")
                                .setMessage("Please Add a Car First. Go to Settings")
                                .setPositiveButton("ok",null).show();
                    }
                }
            }
        });
        if (loggedIn.getRole() != null){
            if (loggedIn.getRole().equals("Corp-User")){
                ll2.removeView(buypackage);
                ll2.setWeightSum(2);
                ll3.removeView(sambaza);
                ll3.setWeightSum(2);
                if (loggedIn.getPin() == null || loggedIn.getPin().isEmpty()){
                    CorporateSetPin corporateSetPin = new CorporateSetPin();
                    corporateSetPin.show(getSupportFragmentManager(),corporateSetPin.getTag());
                }
            }
        } else {
            sessionPreferences.setLoggedInStatus("false");
            sessionPreferences.clearpreviousbalances();
            sessionPreferences.clearsessionVehicles(sessionPreferences.getnumvehicles());
            appDB.cleartablevehicles();
            appDB.cleartabledealers();
            startActivity(new Intent(this,Login.class));
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Balances balances = sessionPreferences.getBalances();
        accountbalance.setText(String.format("%,.2f",balances.getAccount())+" Ksh");
        accountbalance.setEnabled(false);
        tvcurrentpoints.setText(String.valueOf((int)balances.getPoints()));
        tvcurrentpoints.setEnabled(false);
    }

    public boolean contains(final int[] array, final int key) {
        for (final int i : array) {
            if (i == key) {
                return true;
            }
        }
        return false;
    }

    boolean pressedonce = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(pressedonce){
            appDB.cleartabledealers();
            finishAffinity();
        } else {
            pressedonce = true;
            Toast.makeText(HomePage.this,"Hit Back again to Exit",Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedonce = false;
                }
            },2000);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(HomePage.this,Settings.class));
            return true;
        }
        if (id == R.id.action_rate_app) {
            final Dialog dialog = new Dialog(this);
            dialog.setContentView(R.layout.rate_app);
            dialog.setCancelable(true);
            TextView textView = dialog.findViewById(R.id.text);
            final RatingBar ratingBar = dialog.findViewById(R.id.rating);
            Button cancel = dialog.findViewById(R.id.cancel);
            Button confirm = dialog.findViewById(R.id.confirm);
            textView.setText("Please give your rating");
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MobileUser loggedIn = sessionPreferences.getLoggedInUser();
                    loggedIn.setRatedapp(true);
                    loggedIn.setRating(ratingBar.getNumStars());
                    Call<MobileUser> call = Config.getNetClient().updateuser(loggedIn);
                    final ProgressDialog dialog1 = new ProgressDialog(HomePage.this);
                    dialog1.setMessage("Saving your feedback..");
                    dialog1.show();
                    call.enqueue(new Callback<MobileUser>() {
                        @Override
                        public void onResponse(Call<MobileUser> call, Response<MobileUser> response) {
                            dialog1.dismiss();
                            if (response.isSuccessful()){
                                MobileUser response1 = response.body();
                                sessionPreferences.insertLoggedInUser(response1);
                                dialog.dismiss();
                                Toast.makeText(HomePage.this,"Thanks for your feedback",Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(HomePage.this,"Error "+response.code()+" occured",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MobileUser> call, Throwable t) {
                            dialog1.dismiss();
                            Toast.makeText(HomePage.this,"Server is unreachable. Check network",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            dialog.show();
        }
        if (id == R.id.refresh){
            MobileUser loggedInUser = sessionPreferences.getLoggedInUser();
            Call<Balances> call = Config.getNetClient().getbalances(loggedInUser.getId());
            final ProgressDialog dialog = new ProgressDialog(this);
            dialog.setMessage("Refreshing app data... ");
            dialog.show();
            call.enqueue(new Callback<Balances>() {
                @Override
                public void onResponse(Call<Balances> call, Response<Balances> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()){
                        Balances balances = response.body();
                        sessionPreferences.insertBalances(balances);
                        accountbalance.setText(String.format("%,.2f",balances.getAccount())+" Ksh");
                        accountbalance.setEnabled(false);
                        tvcurrentpoints.setText(String.valueOf((int)balances.getPoints()));
                        tvcurrentpoints.setEnabled(false);
                    } else {
                        Toast.makeText(HomePage.this,"Error "+response.code(),Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<Balances> call, Throwable t) {
                    dialog.dismiss();
                    Toast.makeText(HomePage.this,"Could not Refresh Balances",Toast.LENGTH_SHORT).show();
                }
            });
            String [] params = {String.valueOf(sessionPreferences.getLoggedInUser().getId())};
            new BackGroundAllDealers(this).execute(params);
            new Backgroundalloffers(this).execute();
            new BackGroundVehiclesForCorporateUser(this).execute(params);
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_logout){
            sessionPreferences.setLoggedInStatus("false");
            sessionPreferences.clearpreviousbalances();
            sessionPreferences.clearsessionVehicles(sessionPreferences.getnumvehicles());
            appDB.cleartablevehicles();
            appDB.cleartabledealers();
            startActivity(new Intent(HomePage.this,Login.class));
            finish();
        } else if(id == R.id.nav_settings){
            startActivity(new Intent(HomePage.this,Settings.class));
        } else if (id == R.id.viewtransactions){
            startActivity(new Intent(this,ViewTransactions.class));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
