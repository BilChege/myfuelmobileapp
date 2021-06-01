package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.nbs.myfuelapp.R;

public class VerifyEmail extends Fragment {

    private Context context;
    private EditText pin;
    private Button verify;
    private Verify verifyInf;

    public VerifyEmail() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verifyemail,container,false);
        pin = view.findViewById(R.id.pin);
        verify = view.findViewById(R.id.verifyPin);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(pin.getText().toString())){
                    String pinValue = pin.getText().toString();
                    verifyInf.checkCode(pinValue);
                } else {
                    pin.setError("Enter verification code");
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        verifyInf = (Verify) context;
    }

    public interface Verify{
        void checkCode(String code);
    }
}
