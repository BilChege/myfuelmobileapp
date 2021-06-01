package com.anchor.erp.myfuelapp.Activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.anchor.erp.myfuelapp.AsyncTasks.BackGroundLogin;
import com.anchor.erp.myfuelapp.Database.AppDB;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class Login extends AppCompatActivity {

    private static final String TAG = "LOGIN";
    private Button login;
    private TextView signup,forgotPass;
    private TextInputEditText uname,pword;
    private AppDB appDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login = findViewById(R.id.btnlogin);
        appDB = new AppDB(Login.this);
        signup = findViewById(R.id.signuplink);
        forgotPass = findViewById(R.id.linkforgotpass);
        uname = findViewById(R.id.username);
        pword = findViewById(R.id.password);
        appDB.onCreate(appDB.getWritableDatabase());
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,SignUp.class));
                finish();
            }
        });
        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(uname.getText().toString())&&!TextUtils.isEmpty(pword.getText().toString())){
                    String[] creds = {uname.getText().toString(),pword.getText().toString()};
                    new BackGroundLogin(Login.this).execute(creds);
                }else {
                    Toast.makeText(Login.this,"Username or Password is empty",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    boolean pressedonce = false;
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onBackPressed() {
        if (pressedonce){
            finishAffinity();
        } else {
            pressedonce = true;
            Toast.makeText(Login.this,"Hit Back again to exit",Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    pressedonce = false;
                }
            },2000);
        }
    }
}
