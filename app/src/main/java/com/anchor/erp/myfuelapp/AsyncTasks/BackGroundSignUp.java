package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class BackGroundSignUp extends AsyncTask<MobileUser,String,String> {

    private Context context;
    private AppCompatActivity appCompatActivity;
    private ProgressDialog dialog;
    private SessionPreferences sessionPreferences;

    public BackGroundSignUp(AppCompatActivity context) {
        this.context = context;
        appCompatActivity = context;
        dialog = new ProgressDialog(this.context);
        sessionPreferences = new SessionPreferences(this.context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Please Wait as We create an account ... ");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(MobileUser... mobileUsers) {

        String message = null;

        MobileUser user = mobileUsers[0];
        NetClient client = Config.getNetClient();
        Call<MobileUser> call = client.signup(user);
        try {
            Response<MobileUser> response = call.execute();
            if (response.isSuccessful() && response.body() instanceof MobileUser){
                message = "Success";
                new SessionPreferences(context).insertLoggedInUser(response.body());
            } else {
                message = "Error "+response.code();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return message;
    }

    @Override
    protected void onPostExecute(String aVoid) {
        dialog.dismiss();
        if (aVoid != null){
            if (aVoid.equals("Success")){
                Toast.makeText(context,"Welcome",Toast.LENGTH_SHORT).show();
                sessionPreferences.saveFirstTimeStatus("false");
                context.startActivity(new Intent(context,HomePage.class));
                appCompatActivity.finish();
            } else {
                Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server currently Unreachable",Toast.LENGTH_SHORT).show();
        }
    }
}
