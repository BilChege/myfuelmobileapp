package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.anchor.erp.myfuelapp.Models.MobileUser;
import com.anchor.erp.myfuelapp.Models.Purchase;
import com.anchor.erp.myfuelapp.MpesaModels.StkRequest;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.anchor.erp.myfuelapp.Utils.MpesaPayment;
import com.example.nbs.myfuelapp.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;

public class NewBundlesAdapter extends BaseExpandableListAdapter {

    private List<String> titles;
    private HashMap<String,List<FuelPackage>> details;
    private Context context;
    private SessionPreferences sessionPreferences;

    public NewBundlesAdapter(List<String> titles, HashMap<String, List<FuelPackage>> details, Context context) {
        this.titles = titles;
        this.details = details;
        this.context = context;
        sessionPreferences = new SessionPreferences(context);
    }

    @Override
    public int getGroupCount() {
        return titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return details.get(titles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return titles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return details.get(titles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.recycler_element,parent,false);
        TextView category = convertView.findViewById(R.id.bundlecategory);
        category.setText((String)getGroup(groupPosition)+" Packages");
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final FuelPackage selected = (FuelPackage) getChild(groupPosition,childPosition);
        convertView = LayoutInflater.from(context).inflate(R.layout.bundleelement,parent,false);
        TextView name = convertView.findViewById(R.id.bundlename);
        name.setText(String.format("%,.2f",selected.getPriceOfPackage())+" Kshs");
        Button buy = convertView.findViewById(R.id.buypkg);
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                final Date d = new Date();
                final MobileUser loggedInUser = sessionPreferences.getLoggedInUser();
                Calendar c = Calendar.getInstance();
                c.setTime(d);
                c.add(Calendar.DAY_OF_WEEK,selected.getExpirydays());
                String expiry = sdf.format(c.getTime());
                Date expirydate = null;
                try {
                    expirydate = sdf.parse(expiry);
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final Date finalExpirydate = expirydate;
                new AlertDialog.Builder(context).setTitle("Buy Package")
                        .setMessage("Amount: "+String.format("%,.2f",selected.getPriceOfPackage())+" Ksh\n" +
                                "Expiry Date: "+new SimpleDateFormat("dd/MM/yyyy").format(expirydate)+"\n" +
                                "Points awarded: "+selected.getPoints()+"\n" +
                                "Buy Package?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MobileUser user = new MobileUser();
                                user.setId(new SessionPreferences(context).getLoggedInUser().getId());
                                String sproperdate = sdf.format(d);
                                Log.e(TAG, "onClick: &&&&&&&&&&&& DATE FORMATTED "+sproperdate );
                                Purchase p = new Purchase();
                                p.setaPackage(selected);
                                p.setUser(user);
                                Date properdate = null;
                                try {
                                    properdate = sdf.parse(sproperdate);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                p.setDatePurchased(properdate);
                                p.setExpiryDate(finalExpirydate);
                                Log.e(TAG, "onClick: Fuel package "+p.getaPackage().getId());
//                                new BackgroundBuyPackage(p, context).execute();
                                sessionPreferences.setPurchase_to_be_made(p);
                                StkRequest stkRequest = new StkRequest();
                                stkRequest.setAccountReference(Config.ACCOUNT_REFERENCE);
                                stkRequest.setAmount(String.valueOf((int) selected.getPriceOfPackage()));
                                stkRequest.setBusinessShortCode(Config.BUSINESS_SHORT_CODE);
                                stkRequest.setCallBackURL(Config.CALL_BACK_URL);
                                stkRequest.setPartyA("254"+loggedInUser.getPhone());
                                stkRequest.setPartyB(Config.BUSINESS_SHORT_CODE);
                                stkRequest.setPhoneNumber("254"+loggedInUser.getPhone());
                                stkRequest.setPassword(Config.generateMpesaPassword());
                                stkRequest.setTimestamp(Config.generateTimeStamp());
                                stkRequest.setTransactionType(Config.TRANSACTION_TYPE);
                                stkRequest.setTransactionDesc("Payment for MyFuel App package");
                                Log.e(TAG, "onClick: @@@@@@@@@@@@@@@  REQUEST OBJECT : "+stkRequest.toString());
                                Log.e(TAG, "onClick: @@@@@@@@@@@@@@@   JSON REQUEST : "+new Gson().toJson(stkRequest));
                                SharedPreferences sharedPreferences = context.getSharedPreferences(Config.key_purchase_status,Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt(Config.key_purchase_status,1);
                                editor.commit();
                                MpesaPayment mpesaPayment = new MpesaPayment(context);
                                mpesaPayment.requestPin(stkRequest);
                            }
                        }).setNegativeButton("No",null).show();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
