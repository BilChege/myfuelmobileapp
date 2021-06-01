package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChangePin extends AppCompatActivity {

    private static final String TAG = "ChangePin";
    private TextInputEditText oldpin,newpin,repeatpin;
    private Button savenewpin;
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pin);

        sessionPreferences = new SessionPreferences(ChangePin.this);
        oldpin = findViewById(R.id.oldpin);
        newpin = findViewById(R.id.newpin);
        repeatpin = findViewById(R.id.rptnewpin);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        savenewpin = findViewById(R.id.btnnewpin);

        savenewpin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(oldpin.getText().toString()) && !TextUtils.isEmpty(newpin.getText().toString()) && !TextUtils.isEmpty(repeatpin.getText().toString())){
                    final MobileUser user = sessionPreferences.getLoggedInUser();
                    String oldPinValue = oldpin.getText().toString();
                    String encoded = Base64.encodeToString(oldPinValue.getBytes(),Base64.NO_WRAP);
                    Call<Boolean> call = Config.getNetClient().verifyPin(user.getId(),encoded);
                    final ProgressDialog dialog = new ProgressDialog(ChangePin.this);
                    dialog.setMessage("Verifying old pin");
                    dialog.show();
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                Boolean valid = response.body();
                                if (valid){
                                    if (newpin.getText().toString().equals(repeatpin.getText().toString())){
                                        Log.i(TAG, "onResponse: "+user.toString());
                                        String newPinValue = newpin.getText().toString();
                                        String encodedNewPin = Base64.encodeToString(newPinValue.getBytes(),Base64.NO_WRAP);
                                        user.setPin(encodedNewPin);
                                        user.setAccountPassword(null);
                                        Log.i(TAG, "onClick: "+user.toString());
                                        new BackGroundChangePin(user).execute();
                                    } else {
                                        Toast.makeText(ChangePin.this,"The new Pin must match",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ChangePin.this,"The old pin you entered is incorrect",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ChangePin.this,"Error "+response.code()+" Occurred",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(ChangePin.this,"Service is currently unreachable",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ChangePin.this,"Please Fill in All the fields",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private class BackGroundChangePin extends AsyncTask<String,String,String> {

        private MobileUser user;
        private ProgressDialog dialog;

        public BackGroundChangePin(MobileUser user) {
            this.user = user;
            dialog = new ProgressDialog(ChangePin.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please wait ... ");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String message = null;

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.baseUrl_demo).addConverterFactory(GsonConverterFactory.create()).build();
            NetClient client = retrofit.create(NetClient.class);
            Call<MobileUser> call = client.updateuser(user);
            try {
                Response<MobileUser> response = call.execute();
                if (response.isSuccessful() && response.body() instanceof MobileUser){
                    message = "Success";
                    new SessionPreferences(ChangePin.this).insertLoggedInUser(response.body());
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
            if(s != null){
                if (s.equals("Success")){
                    Toast.makeText(ChangePin.this,"Pin updated successfully",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ChangePin.this,Settings.class));
                    finish();
                } else {
                    Toast.makeText(ChangePin.this,s,Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(ChangePin.this,"Server is currently unreachable",Toast.LENGTH_SHORT).show();
            }
        }
    }
}
