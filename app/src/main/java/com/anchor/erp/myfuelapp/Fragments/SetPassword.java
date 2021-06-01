package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.Activities.Login;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.textfield.TextInputEditText;

public class SetPassword extends Fragment {

    private Context context;
    private TextInputEditText setpassword,repeatpassword;
    private String accountPassword;
    private Button finish,back;
    private PasswordPass listener;
    private AppCompatActivity appCompatActivity;

    public SetPassword() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (PasswordPass) context;
        appCompatActivity = (AppCompatActivity) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_set_password,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setpassword = view.findViewById(R.id.set_password);
        repeatpassword = view.findViewById(R.id.repeat_password);
        finish = view.findViewById(R.id.nxtfrompassword);
        back = view.findViewById(R.id.skippassword);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(setpassword.getText().toString())&&!TextUtils.isEmpty(repeatpassword.getText().toString())){
                    if (setpassword.getText().toString().equals(repeatpassword.getText().toString())){
                        accountPassword = setpassword.getText().toString();
                        listener.passPasswordAndFinish(accountPassword);
                    }else {
                        Toast.makeText(context,"PASSWORDS MUST MATCH ",Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context,"ENTER AND REPEAT THE PASSWORD",Toast.LENGTH_LONG).show();
                }
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Login.class));
                appCompatActivity.finish();
            }
        });
    }

    public interface PasswordPass{
        void passPasswordAndFinish(String accountPassword);
    }
}
