package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anchor.erp.myfuelapp.Database.AppDB;
import com.anchor.erp.myfuelapp.Fragments.SetPin;
import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundSignUp;
import com.anchor.erp.myfuelapp.Fragments.NameAndContacts;
import com.anchor.erp.myfuelapp.Fragments.SetPassword;
import com.anchor.erp.myfuelapp.Fragments.VerifyPhone;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SignUp extends AppCompatActivity implements NameAndContacts.NamesPass, VerifyPhone.VerificationPass, SetPassword.PasswordPass, SetPin.Finish {

    private Fragment namesAndContacts,verifyPhone,setPassword,setPin;
    private FragmentTransaction transaction,transaction1,transaction2,transaction3;
    private String firstName,lastname,phone,email,accountpassword,pin,enteredcode,codefromserver;
    private AppDB appDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        appDB = new AppDB(SignUp.this);
        appDB.onCreate(appDB.getWritableDatabase());

        Fragment current = getSupportFragmentManager().findFragmentById(R.id.frame);
        namesAndContacts = new NameAndContacts();
        setPassword = new SetPassword();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        appDB.onCreate(appDB.getReadableDatabase());

        if (current == null){
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.frame,namesAndContacts);
            transaction.commit();
        }

    }

    @Override
    public void namesPass(String fname, String lname, String email, String phone) {
        firstName = fname;
        lastname = lname;
        this.email = email;
        this.phone = phone;
        String [] params = {phone};
        new BackGroundVerifyPhone().execute(params);
        verifyPhone = new VerifyPhone();
        transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.frame,verifyPhone);
        transaction1.addToBackStack(null);
        transaction1.commit();
    }

    @Override
    public void passPasswordAndFinish(String accountPassword) {
        this.accountpassword = accountPassword;
        setPin = new SetPin();
        transaction3 = getSupportFragmentManager().beginTransaction();
        transaction3.replace(R.id.frame,setPin);
        transaction3.addToBackStack(null);
        transaction3.commit();
    }

    @Override
    public void passVerification(String entered_code) {
        this.enteredcode = entered_code;
        if (enteredcode.equals(codefromserver)){
            setPassword = new SetPassword();
            transaction2 = getSupportFragmentManager().beginTransaction();
            transaction2.replace(R.id.frame,setPassword);
            transaction2.addToBackStack(null);
            transaction2.commit();
        } else {
            Toast.makeText(SignUp.this,"Please Recheck and re-enter the code sent to you via sms",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void buttonverifyClicked() {

    }

    @Override
    public void btnFinishClicked(String pin) throws InvalidKeySpecException, NoSuchAlgorithmException {
        this.pin = pin;
        pushuser();
    }

    private void pushuser() throws InvalidKeySpecException, NoSuchAlgorithmException {

        MobileUser user = new MobileUser();
        user.setFirstName(firstName);
        user.setLastName(lastname);
        user.setEmail(email);
        user.setPhone(phone);
        String encodedPass = Base64.encodeToString(accountpassword.getBytes(),Base64.NO_WRAP);
        user.setAccountPassword(encodedPass);
        String encodedPin = Base64.encodeToString(pin.getBytes(),Base64.NO_WRAP);
        user.setPin(encodedPin);
        new BackGroundSignUp(SignUp.this).execute(user);
    }

    boolean pressedonce = false;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.frame);
        if (f instanceof NameAndContacts){
            if (pressedonce){
                finishAffinity();
            } else {
                pressedonce = true;
                Toast.makeText(SignUp.this,"Press Back Again to exit",Toast.LENGTH_SHORT).show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        pressedonce = false;
                    }
                },2000);
            }
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }

    public class BackGroundVerifyPhone extends AsyncTask<String,String,String>{

        private ProgressDialog dialog;

        public BackGroundVerifyPhone() {
            dialog = new ProgressDialog(SignUp.this);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Requesting Code ... ");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {

            String response_str = null;

            Retrofit retrofit = new Retrofit.Builder().baseUrl(Config.baseUrl_demo).addConverterFactory(GsonConverterFactory.create()).build();
            NetClient client = retrofit.create(NetClient.class);
            Call<String> call = client.verifyphone(strings[0]);
            try {
                Response<String> response = call.execute();
                if (response.isSuccessful() && response.body() instanceof String){
                    response_str = response.body();
                } else {
                    response_str = "Error";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return response_str;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            dialog.dismiss();
            if (!s.equals(null)){
                if (s.equals("Error")){
                    Toast.makeText(SignUp.this,s,Toast.LENGTH_SHORT).show();
                } else {
                    codefromserver = s;
                }
            } else {
                Toast.makeText(SignUp.this,"Server currently Unreachable",Toast.LENGTH_SHORT).show();
            }

        }
    }

}
