package com.anchor.erp.myfuelapp.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.example.nbs.myfuelapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmAccount extends Fragment {

    private Context context;
    private EditText email;
    private Button confirm;
    private ProgressDialog dialog;
    private ConfirmUser confirmUser;
    private SessionPreferences sessionPreferences;

    public ConfirmAccount() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.confirmaccount,container,false);
        email = view.findViewById(R.id.email_address);
        confirm = view.findViewById(R.id.verifyEmail);
        sessionPreferences = new SessionPreferences(context);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(email.getText().toString())){
                    final String emailAddress = email.getText().toString();
                    Call<MobileUser> call = Config.getNetClient().checkUser(emailAddress);
                    dialog = new ProgressDialog(context);
                    dialog.setMessage("Confirming account ... ");
                    dialog.show();
                    call.enqueue(new Callback<MobileUser>() {
                        @Override
                        public void onResponse(Call<MobileUser> call, Response<MobileUser> response) {
                            dialog.dismiss();
                            if (response.isSuccessful()){
                                MobileUser mobileUser = response.body();
                                if (mobileUser.getId() > 0){
                                    sessionPreferences.insertLoggedInUser(mobileUser);
                                    confirmUser.userConfirmed(emailAddress);
                                } else {
                                    email.setError("User not found");
                                }
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
                } else {
                    email.setError("Enter your email address");
                }
            }
        });
    }

    public interface ConfirmUser{
        void userConfirmed(String email);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        confirmUser = (ConfirmUser) context;
    }
}
