package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
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

public class ResetPassword extends AppCompatActivity {

    private TextInputEditText oldpass,newpass,repeatpass;
    private Button changepass;
    private SessionPreferences sessionPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        oldpass = findViewById(R.id.oldpass);
        newpass = findViewById(R.id.newpass);
        repeatpass = findViewById(R.id.rptpass);
        sessionPreferences = new SessionPreferences(ResetPassword.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        changepass = findViewById(R.id.btnnewpass);
        changepass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(oldpass.getText().toString()) && !TextUtils.isEmpty(newpass.getText().toString()) && !TextUtils.isEmpty(repeatpass.getText().toString())){
                    final MobileUser user = sessionPreferences.getLoggedInUser();
                    String oldPassVal = oldpass.getText().toString();
                    final String encoded = Base64.encodeToString(oldPassVal.getBytes(),Base64.NO_WRAP);
                    Call<Boolean> call = Config.getNetClient().verifyPass(user.getId(),encoded);
                    final ProgressDialog dialog = new ProgressDialog(ResetPassword.this);
                    dialog.setMessage("Verifying old password ... ");
                    dialog.show();
                    call.enqueue(new Callback<Boolean>() {
                        @Override
                        public void onResponse(Call<Boolean> call, Response<Boolean> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                Boolean valid = response.body();
                                if (valid){
                                    if (newpass.getText().toString().equals(repeatpass.getText().toString())){
                                        String newPasswordValue = newpass.getText().toString();
                                        String encodedNewPass = Base64.encodeToString(newPasswordValue.getBytes(),Base64.NO_WRAP);
                                        user.setAccountPassword(encodedNewPass);
                                        user.setPin(null);
                                        new BackGroundUpdateUser(user).execute();
                                    } else {
                                        Toast.makeText(ResetPassword.this,"Passwords Dont Match",Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(ResetPassword.this,"The old password you entered is incorrect",Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(ResetPassword.this,"Error "+response.code()+" Occured",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Boolean> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(ResetPassword.this,"Service is currently unreachable",Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(ResetPassword.this,"Please Insert All the data above",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private class BackGroundUpdateUser extends AsyncTask<String,String,String> {

        private MobileUser user;
        private ProgressDialog dialog;

        public BackGroundUpdateUser(MobileUser user) {
            this.user = user;
            dialog = new ProgressDialog(ResetPassword.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Please Wait ... ");
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
                    new SessionPreferences(ResetPassword.this).insertLoggedInUser(response.body());
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
            if (s.equals("Success")){
                Toast.makeText(ResetPassword.this,"Password Successfully changed",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ResetPassword.this,Settings.class));
                finish();
            } else {
                Toast.makeText(ResetPassword.this,s,Toast.LENGTH_SHORT).show();
            }
        }
    }
}
