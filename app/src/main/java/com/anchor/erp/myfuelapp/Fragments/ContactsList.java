package com.anchor.erp.myfuelapp.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Adapters.ContactsListAdapter;
import com.anchor.erp.myfuelapp.Models.Contact;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class ContactsList extends DialogFragment implements Config.ContactsListener {

    private Context context;
    private RecyclerView contactslist;
    private Cursor cursor;
    private Config.RecieverFromContacts listener;
    private List<Contact> contacts = new ArrayList<>();

    public ContactsList() {
        //Default empty constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contacts,container,false);
        contactslist = view.findViewById(R.id.contactslist);
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            getContactsIntoModel();
        } catch (NumberParseException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void getContactsIntoModel() throws NumberParseException {
        cursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (cursor.moveToNext()){
            Contact contact = new Contact();
            String name = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            String phone = cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            Log.i(TAG, "getContactsIntoModel: @@@@@@@@@@@@@@@@  PHONE NUMBER -> "+phone+" AND NAME -> "+name);
            PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();
            Phonenumber.PhoneNumber phoneNumber = null;
            if (phone.startsWith("0")){
                phoneNumber = phoneNumberUtil.parse(phone,"KE");
            }
            if (phoneNumber != null){
                contact.setName(name);
                contact.setPhone(phoneNumberUtil.format(phoneNumber,PhoneNumberUtil.PhoneNumberFormat.NATIONAL));
                contacts.add(contact);
            }
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        ContactsListAdapter adapter = new ContactsListAdapter(context,contacts,this);
        contactslist.setLayoutManager(layoutManager);
        contactslist.setAdapter(adapter);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (Config.RecieverFromContacts) context;
    }

    @Override
    public void contactSelected() {
        listener.readContact();
        dismiss();
    }
}
