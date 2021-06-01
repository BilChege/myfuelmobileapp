package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.MobileRedemptions;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class BackgroundRedeemPoints extends AsyncTask<String,String,String> {

    private MobileRedemptions redemption;
    private Context context;
    private ProgressDialog dialog;
    private AppCompatActivity activity;
    private SessionPreferences sessionPreferences;

    public BackgroundRedeemPoints(MobileRedemptions redemption, Context context) {
        this.redemption = redemption;
        this.context = context;
        sessionPreferences = new SessionPreferences(this.context);
        dialog = new ProgressDialog(this.context);
        activity = (AppCompatActivity) context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Processing Request. Please wait ... ");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {
        String message = null;
        NetClient client = Config.getNetClient();
        Call<Balances> call = client.redeempointsforpromo(redemption);
        Log.e(TAG, "doInBackground: @@@@@@@@@@@@@@@@  Offer ID: "+redemption.getId() );
        try {
            Response<Balances> response = call.execute();
            if (response.isSuccessful()){
                Balances balances = response.body();
                sessionPreferences.insertBalances(balances);
                message = "Success";
            } else {
                message = "Error "+response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        dialog.dismiss();
        if (s != null){
            if (s.equals("Success")){
                Toast.makeText(context,"You have Successfully redeemed points",Toast.LENGTH_SHORT).show();
                context.startActivity(new Intent(context, HomePage.class));
            } else {
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Sever Currently Unreachable. Check your Network connection",Toast.LENGTH_SHORT);
        }
    }
}
