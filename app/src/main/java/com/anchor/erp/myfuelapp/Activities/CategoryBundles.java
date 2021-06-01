package com.anchor.erp.myfuelapp.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Adapters.CategoryBundlesAdapter;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.example.nbs.myfuelapp.R;

import java.util.List;

public class CategoryBundles extends AppCompatActivity {

    private static final String TAG = "categorybundles";
    private RecyclerView recyclerView;
    private SessionPreferences sessionPreferences;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_bundles);

        recyclerView = findViewById(R.id.categorybundles);
        layoutManager = new GridLayoutManager(CategoryBundles.this,2);
//        ((LinearLayoutManager) layoutManager).setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        sessionPreferences = new SessionPreferences(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent i = getIntent();
        setTitle(i.getStringExtra("page title")+" Packages");
        List<FuelPackage> packages = new SessionPreferences(CategoryBundles.this).getFuelpackages(i.getIntExtra("numpackages",-1));
        for (FuelPackage f:packages){
            Log.e(TAG, "onCreate: "+f.getId() );
        }
        CategoryBundlesAdapter adapter = new CategoryBundlesAdapter(packages,CategoryBundles.this);
        recyclerView.setAdapter(adapter);
    }
}
