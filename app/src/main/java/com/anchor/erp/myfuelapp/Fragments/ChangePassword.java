package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ChangePassword extends Fragment {

    private EditText newPass, rptPass;
    private Button savePass;
    private Context context;
    private SessionPreferences sessionPreferences;

    public ChangePassword() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.changepassword,container,false);
        newPass = view.findViewById(R.id.newPass);
        rptPass = view.findViewById(R.id.rptPass);
        sessionPreferences = new SessionPreferences(context);
        savePass = view.findViewById(R.id.savePassword);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        savePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()){
                    String rawPass = newPass.getText().toString();
                    String encodedPass = Base64.encodeToString(rawPass.getBytes(),Base64.NO_WRAP);
                    MobileUser mobileUser = sessionPreferences.getLoggedInUser();
                    mobileUser.setAccountPassword(encodedPass);
                    Log.i(TAG, "onClick: "+mobileUser.toString());
                    Call<MobileUser> call = Config.getNetClient().updateuser(mobileUser);
                    final ProgressDialog dialog = new ProgressDialog(context);
                    dialog.setMessage("Saving new Password");
                    dialog.show();
                    call.enqueue(new Callback<MobileUser>() {
                        @Override
                        public void onResponse(Call<MobileUser> call, Response<MobileUser> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                AppCompatActivity activity = (AppCompatActivity) context;
                                Toast.makeText(context,"Your password has been updated successfully",Toast.LENGTH_SHORT).show();
                                activity.finish();
                            } else {
                                Toast.makeText(context,"Error "+response.code()+" Occurred",Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<MobileUser> call, Throwable t) {
                            dialog.dismiss();
                            Toast.makeText(context,"Service is currently unreachable",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private boolean validate(){
        boolean result = true;
        if (TextUtils.isEmpty(newPass.getText().toString())){
            newPass.setError("Enter a new Pin");
            result = false;
        } else if (TextUtils.isEmpty(rptPass.getText().toString())){
            rptPass.setError("Repeat your new pin");
            result = false;
        } else if (!newPass.getText().toString().equals(rptPass.getText().toString())){
            rptPass.setError("The values must match");
            result = false;
        }
        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }
}
