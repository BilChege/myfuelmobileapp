package com.anchor.erp.myfuelapp.Activities;

import android.Manifest;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundSambaza;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Fragments.ContactsList;
import com.anchor.erp.myfuelapp.Models.Contact;
import com.anchor.erp.myfuelapp.Models.Balances;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Network.NetClient;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Sambaza extends AppCompatActivity implements Config.RecieverFromContacts {

    private EditText reciever,amount;
    private Button send;
    private FloatingActionButton contacts;
    private SessionPreferences sessionPreferences;
    private boolean userverified;
    private RelativeLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sambaza);

        reciever = findViewById(R.id.recieverphone);
        amount = findViewById(R.id.amount);
        send = findViewById(R.id.btnsend);
        contacts = findViewById(R.id.contacts);
        container = findViewById(R.id.container);
        sessionPreferences = new SessionPreferences(this);

        enableRunTimePermission();

        reciever.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 10){
                    int absolute = Integer.valueOf(s.toString());
                    verifyUserWithNumber(String.valueOf(absolute));
                }
            }
        });

        contacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Fragment previous = fragmentManager.findFragmentByTag("dialog");
                if (previous != null){
                    fragmentTransaction.remove(previous);
                }
                ContactsList contactsList = new ContactsList();
                contactsList.show(fragmentTransaction,"dialog");
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    if (userverified){
                        String sender = String.valueOf(sessionPreferences.getLoggedInUser().getId());
                        String phone = reciever.getText().toString().replaceAll("\\s","");
                        int absolute = Integer.valueOf(phone);
                        String amt = amount.getText().toString();
                        String [] params = {sender, String.valueOf(absolute),amt};
                        new BackGroundSambaza(Sambaza.this).execute(params);
                    } else {
                        Snackbar.make(container,"User With the specified phone number not verified",Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(container,"Check the Inputs above",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void enableRunTimePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(Sambaza.this, Manifest.permission.READ_CONTACTS)){
            Toast.makeText(Sambaza.this,"Permisson Request allows Access to contacts app",Toast.LENGTH_SHORT).show();
        } else {
            ActivityCompat.requestPermissions(Sambaza.this,new String[]{Manifest.permission.READ_CONTACTS},1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private boolean validate() {
        MobileUser loggedIn = sessionPreferences.getLoggedInUser();
        boolean response = true;
        if (TextUtils.isEmpty(reciever.getText().toString())){
            reciever.setError("Enter the Recipient Phone Number");
            response = false;
        }
        if (TextUtils.isEmpty(amount.getText().toString())){
            amount.setError("Enter the amount to send");
            response = false;
        } else {
            Balances balances = sessionPreferences.getBalances();
            double cashbalance = balances.getAccount();
            double tosend = Double.parseDouble(amount.getText().toString());
            if (tosend > cashbalance){
                amount.setError("The Specified amount exceeds your Current balance. Your Balance is "+cashbalance);
                response = false;
            }
        }
        if (reciever.getText().toString().equals(loggedIn.getPhone())){
            reciever.setError("Cannot Sambaza to this phone number");
            response = false;
        }
        return response;
    }

    @Override
    public void readContact() {
        reciever = findViewById(R.id.recieverphone);
        container = findViewById(R.id.container);
        send = findViewById(R.id.btnsend);
        amount = findViewById(R.id.amount);
        contacts = findViewById(R.id.contacts);
        sessionPreferences = new SessionPreferences(this);
        Contact selected = sessionPreferences.getSelectedContact();
        String phone = selected.getPhone();
        if (phone.substring(0,3).equals("+254")){
            phone = phone.replace(phone.substring(0,3),"0");
        }
        String correct = phone.replaceAll(" ","");
        reciever.setText(correct);
        int absolute = Integer.valueOf(correct);
        String tosend = String.valueOf(absolute);
        verifyUserWithNumber(tosend);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    if (userverified){
                        String sender = String.valueOf(sessionPreferences.getLoggedInUser().getId());
                        String phone = reciever.getText().toString();
                        int absolute_phone = Integer.valueOf(phone);
                        String tosend = String.valueOf(absolute_phone);
                        String amt = amount.getText().toString();
                        String [] params = {sender,tosend,amt};
                        new BackGroundSambaza(Sambaza.this).execute(params);
                    } else {
                        Snackbar.make(container,"User With the specified phone number not verified",Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(container,"Check the inputs above",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void verifyUserWithNumber(String phone) {
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Verifying user... ");
        dialog.show();
        NetClient netClient = Config.getNetClient();
        Call<MobileUser> call = netClient.verifySystemUser(phone);
        call.enqueue(new Callback<MobileUser>() {
            @Override
            public void onResponse(Call<MobileUser> call, Response<MobileUser> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    MobileUser mobileUser = response.body();
                    if (mobileUser.getId() > 0){
                        Snackbar.make(container,"User has been verified",Snackbar.LENGTH_SHORT).show();
                        userverified = true;
                    } else {
                        reciever.setError("Invalid app User");
                        Snackbar.make(container,"This User does not Have The app",Snackbar.LENGTH_SHORT).show();
                        userverified = false;
                    }
                } else {
                    Toast.makeText(Sambaza.this,"Error "+response.code()+" Occured",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<MobileUser> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(Sambaza.this,"Check your Network Connection",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
