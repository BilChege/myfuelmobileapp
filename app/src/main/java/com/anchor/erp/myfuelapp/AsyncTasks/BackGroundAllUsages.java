package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundAllUsages extends AsyncTask<Integer,String,String> {

    private Context context;
    private SessionPreferences sessionPreferences;
    private ProgressDialog dialog;
    private AppCompatActivity appCompatActivity;

    public BackGroundAllUsages(Context context) {
        this.context = context;
        appCompatActivity = (AppCompatActivity) context;
        sessionPreferences = new SessionPreferences(this.context);
        dialog = new ProgressDialog(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Please wait ... ");
        dialog.setCancelable(false);
//        dialog.show();
    }

    @Override
    protected String doInBackground(Integer... integers) {
        Integer[] ids = integers;
        int userid = ids[0];
        String message = null;

        NetClient client = Config.getNetClient();
        Call<List<FuelCar>> call = client.usagesforuser(userid);
        try {
            Response<List<FuelCar>> response = call.execute();
            if (response.isSuccessful()){
                message = "Success";
                List<FuelCar> usages = response.body();
                sessionPreferences.setNumUsages(sessionPreferences.addSessionUsages(usages));
            } else {
//                message = "No Usages Found";
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
            if (!s.equals("Success")){
//                Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server Currently unreachable",Toast.LENGTH_SHORT).show();
        }
    }
}
