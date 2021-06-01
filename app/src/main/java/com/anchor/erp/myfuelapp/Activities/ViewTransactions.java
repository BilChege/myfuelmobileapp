package com.anchor.erp.myfuelapp.Activities;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Fragments.TransactionDetails;
import com.anchor.erp.myfuelapp.Fragments.Transactions;
import com.anchor.erp.myfuelapp.Models.DealerRating;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Models.MobileDealer;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;

import java.text.ParseException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewTransactions extends AppCompatActivity implements Config.TransactionsListener {

    private static final String TAG = "ViewTransactions";
    private SessionPreferences sessionPreferences;
    private AppDB appDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_transactions);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appDB = new AppDB(this);
        sessionPreferences = new SessionPreferences(this);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment transactions = new Transactions();
        fragmentTransaction.add(R.id.container,transactions);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onBackPressed() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
        if (current instanceof Transactions){
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    @Override
    public void transactionSelected(FuelCar fuelCar) {
        sessionPreferences = new SessionPreferences(this);
        sessionPreferences.setSelected_transaction(fuelCar);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        Fragment transactionDetails = new TransactionDetails();
        fragmentTransaction.replace(R.id.container,transactionDetails);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
        if (current != null && current instanceof TransactionDetails){
            getMenuInflater().inflate(R.menu.rate_fuelstation,menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_rate_station : {
                FuelCar fuelCar = null;
                try {
                    fuelCar = sessionPreferences.getSelectedTransaction();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                final Dialog dialog = new Dialog(this);
                dialog.setContentView(R.layout.rate_app);
                dialog.setCancelable(true);
                TextView textView = dialog.findViewById(R.id.text);
                final RatingBar ratingBar = dialog.findViewById(R.id.rating);
                Button cancel = dialog.findViewById(R.id.cancel);
                Button confirm = dialog.findViewById(R.id.confirm);
                MobileDealer dealer = appDB.getDealerId(fuelCar.getStationid().split("\\s")[0]);
                Log.i(TAG, "onOptionsItemSelected: @@@@@@@@@@@@  STATION ID : "+fuelCar.getStationid().split("\\s")[0]);
                if (dealer.getUserrating() > 0){
                    ratingBar.setRating((float) dealer.getUserrating());
                    Log.i(TAG, "onOptionsItemSelected: @@@@@@@@@@@@@@@@@ STATION RATING : "+dealer.getUserrating());
                    textView.setText("("+dealer.getName()+"): You may change your rating from the previous one");
                } else {
                    textView.setText("("+dealer.getName()+"):Please give your rating of this Station");
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialog.dismiss();
                        }
                    });
                final FuelCar finalFuelCar = fuelCar;
                confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        dialog.dismiss();
                        DealerRating dealerRating = new DealerRating();
                        String [] names = finalFuelCar.getStationid().split("\\s");
                        final MobileDealer mobileDealer = appDB.getDealerId(names[0]);
                        MobileUser loggedInUser = sessionPreferences.getLoggedInUser();
                        dealerRating.setDealer(mobileDealer.getId());
                        dealerRating.setUser(loggedInUser.getId());
                        dealerRating.setRating(ratingBar.getRating());
                        Call<DealerRating> call = Config.getNetClient().doRating(dealerRating);
                        call.enqueue(new Callback<DealerRating>() {
                            @Override
                            public void onResponse(Call<DealerRating> call, Response<DealerRating> response) {
                                if (response.isSuccessful()){
                                    DealerRating rating = response.body();
                                    mobileDealer.setUserrating(rating.getRating());
                                        appDB.updateDealer(mobileDealer);
                                        Toast.makeText(ViewTransactions.this,"Thanks for your feedback",Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(ViewTransactions.this,"Error "+response.code()+" occured while submitting feedback",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                @Override
                                public void onFailure(Call<DealerRating> call, Throwable t) {
                                    dialog.dismiss();
                                    Toast.makeText(ViewTransactions.this,"Server is currently unreachable. Try again later",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                    dialog.show();

            }
            case R.id.homeAsUp : {
                Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
                if (current instanceof Transactions){
                    finish();
                } else {
                    getSupportFragmentManager().popBackStack();
                }
            }
        }
        return true;
    }
}
