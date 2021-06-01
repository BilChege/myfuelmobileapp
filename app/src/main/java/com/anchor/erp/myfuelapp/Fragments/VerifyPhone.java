package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.anchor.erp.myfuelapp.BroadcastRecievers.SmsListener;
import com.anchor.erp.myfuelapp.Activities.Login;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;

import static android.content.ContentValues.TAG;

public class VerifyPhone extends Fragment implements Config.SmsListener {

    private Context context;
    private SmsListener mylistener;
    private VerificationPass mlistener;
    private Button back, next, verify;
    private EditText etCode;
    private TextView message;
    private AppCompatActivity appCompatActivity;
    private String code;
    private String phone;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        appCompatActivity = (AppCompatActivity) context;
        mlistener = (VerificationPass) context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_verify_phone,container,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        etCode = view.findViewById(R.id.verificationcode);
        back = view.findViewById(R.id.vrfy_back);
        next = view.findViewById(R.id.vrfy_next);
        message = view.findViewById(R.id.vrf_message);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etCode.getText().toString())){
                    mlistener.passVerification(etCode.getText().toString());
                }else {
                    Toast.makeText(context,"PLEASE ENTER THE VERIFICATION CODE SENT TO YOU VIA SMS",Toast.LENGTH_LONG).show();
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

    @Override
    public void bindSmsListener(String code,String phone,Context context) {
        this.phone = phone;
        this.code = code;
        Log.e(TAG, "bindSmsListener: !!!!!!! CODE RECIEVED   !!!!!"+code+"    "+"CODE SET  "+this.code );
        mylistener = new SmsListener();
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.provider.Telephony.SMS_RECEIVED");
        context.registerReceiver(mylistener,filter);
    }

    @Override
    public void unbindSmsListener() {

    }

    @Override
    public void messagesRecieved(String[] message) {
        String code = message[0];
        etCode.setText(code.substring(0,5));
    }

    public interface VerificationPass {
        void passVerification(String entered_code);
        void buttonverifyClicked();
    }
}
