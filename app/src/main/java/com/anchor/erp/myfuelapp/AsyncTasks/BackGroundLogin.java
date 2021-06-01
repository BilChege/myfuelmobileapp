package com.anchor.erp.myfuelapp.AsyncTasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Activities.HomePage;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Vehicle;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class BackGroundLogin extends AsyncTask<String,String,String> {

    private Context context;
    private String email,password;
    private ProgressDialog dialog;
    private AppCompatActivity context1;
    private AppDB appDB;
    private SessionPreferences sessionPreferences;

    public BackGroundLogin(AppCompatActivity context1) {
        this.context = context1;
        this.context1 = context1;
        dialog = new ProgressDialog(this.context);
        appDB = new AppDB(context);
        sessionPreferences = new SessionPreferences(context);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        dialog.setMessage("Signing you in. Please Wait ... ");
        dialog.setCancelable(false);
        dialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        this.email = params[0];
        this.password = params[1];
        String[] parts = email.split("\\.");
        String emailtosend = parts[0];
        String message = null;
        String encodedPass = Base64.encodeToString(password.getBytes(),Base64.NO_WRAP);

        NetClient client = Config.getNetClient();
        Call<MobileUser> call = client.loginuser(email,encodedPass);
        Log.e(TAG, "onClick: !!!!!!!!!!!          @@@@@@@@@@@@@    "+call.request().url() );

        try {
            Response<MobileUser> response = call.execute();

            if (response.isSuccessful()&&response.body()instanceof MobileUser){
                Log.e(TAG, "doInBackground: "+response.body() );
                MobileUser user = response.body();
                System.out.println("@@@@@@@@@@@ LOGGED IN USER : "+user.toString());
                String passfrmdb = user.getAccountPassword();
                if (user.getId() > 0){
                    message = "Success";
                    new SessionPreferences(context).insertLoggedInUser(user);
                    if (!response.body().getVehicles().isEmpty()){
                        List<Vehicle> sessionVehicles = new ArrayList<>();
                        for (Vehicle v:response.body().getVehicles()){
                            sessionVehicles.add(v);
                        }
                        new SessionPreferences(context).setnumvehicles(new SessionPreferences(context).addsessionvehicles(sessionVehicles));
                    }
                    if (response.body().getBalances() != null){
                        sessionPreferences.insertBalances(response.body().getBalances());
                    }
                } else if (user.getId() < 0){
                    message = "Wrong Password";
                } else {
                    message = "User does not exist";
                }
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
        super.onPostExecute(aVoid);
        dialog.dismiss();
        if (aVoid != null){
            if (aVoid.equals("Success")){
                Toast.makeText(context,"Welcome",Toast.LENGTH_SHORT).show();
                sessionPreferences.saveFirstTimeStatus("false");
                sessionPreferences.setLoggedInStatus("true");
                context.startActivity(new Intent(context,HomePage.class));
                context1.finish();
            } else if(aVoid.equals("wrong password")){
                Toast.makeText(context,"Wrong Password.",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context,aVoid,Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context,"Server currently Unreachable",Toast.LENGTH_SHORT).show();
        }
    }

}
