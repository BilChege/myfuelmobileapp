package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class Backgroundalloffers extends AsyncTask<String,String,String> {

    private Context context;
    private AppCompatActivity appCompatActivity;
    private ProgressDialog progressDialog;
    private SessionPreferences sessionPreferences;

    public Backgroundalloffers(Context context) {
        this.context = context;
        appCompatActivity = (AppCompatActivity) context;
        progressDialog = new ProgressDialog(this.context);
        sessionPreferences = new SessionPreferences(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        progressDialog.setMessage("Fetching Promotions ... ");
        progressDialog.setCancelable(false);
//        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... strings) {

        String message = null;
        NetClient netClient = Config.getNetClient();
        Call<List<OffersForMobile>> call = netClient.alloffers();
        try {
            Response<List<OffersForMobile>> response = call.execute();
            if (response.isSuccessful()){
                message = "Success";
                List<OffersForMobile> offers = response.body();
                sessionPreferences.setNumOffers(sessionPreferences.createSessionOffers(offers));
            } else {
                message = "Could not update Offers. You may be offline";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressDialog.dismiss();
        if (s != null){
            if (!s.equals("Success")){
                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server is Unreachable",Toast.LENGTH_SHORT).show();
        }
    }
}
