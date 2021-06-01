package com.anchor.erp.myfuelapp.Activities;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.anchor.erp.myfuelapp.Fragments.ChangePassword;
import com.anchor.erp.myfuelapp.Fragments.ConfirmAccount;
import com.anchor.erp.myfuelapp.Fragments.VerifyEmail;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPassword extends AppCompatActivity implements ConfirmAccount.ConfirmUser, VerifyEmail.Verify {

    Fragment confirmAccount,verifyPin,changePass;
    String verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        confirmAccount = new ConfirmAccount();
        fragmentTransaction.add(R.id.container,confirmAccount);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void userConfirmed(String email) {
        Call<String> call = Config.getNetClient().verifyEmail(email);
        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setMessage("Requesting verification ... ");
        dialog.show();
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                dialog.dismiss();
                if (response.isSuccessful()){
                    String code = response.body();
                    setVerificationCode(code);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    verifyPin = new VerifyEmail();
                    fragmentTransaction.replace(R.id.container,verifyPin);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else {
                    Toast.makeText(ForgotPassword.this,"Error "+response.code()+" Occurred",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(ForgotPassword.this,"Service is currently unreachable",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setVerificationCode(String code) {
        verificationCode = code;
    }

    @Override
    public void checkCode(String code) {
        if (verificationCode.equals(code)){
            changePass = new ChangePassword();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.container,changePass);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        } else {
            Toast.makeText(this,"The code you entered is wrong",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        Fragment current = getSupportFragmentManager().findFragmentById(R.id.container);
        if (current instanceof ConfirmAccount){
            finish();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
