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
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.Activities.Login;
import com.example.nbs.myfuelapp.R;
import com.google.android.material.textfield.TextInputEditText;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class SetPin extends Fragment {

    private TextInputEditText setpin,rptpin;
    private Button skip,finish;
    private Context context;
    private Finish listener;
    private String strsetpin,strrptpin;

    public SetPin() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (Finish) context;
        this.context = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.set_pin,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        setpin = view.findViewById(R.id.txtsetpin);
        rptpin = view.findViewById(R.id.txtrepeatpin);
        skip = view.findViewById(R.id.btnskipsetpin);
        finish = view.findViewById(R.id.btnfinish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(setpin.getText().toString()) && !TextUtils.isEmpty(rptpin.getText().toString())){
                    if (setpin.getText().toString().equals(rptpin.getText().toString())){
                        try {
                            listener.btnFinishClicked(setpin.getText().toString());
                        } catch (InvalidKeySpecException e) {
                            e.printStackTrace();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
                    } else {
                        Toast.makeText(context,"The Inputs must match",Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context,"Please Enter and repeat a secure pin value",Toast.LENGTH_SHORT).show();
                }
            }
        });
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, Login.class));
            }
        });
    }

    public interface Finish{
        void btnFinishClicked(String pin) throws InvalidKeySpecException, NoSuchAlgorithmException;
    }
}
