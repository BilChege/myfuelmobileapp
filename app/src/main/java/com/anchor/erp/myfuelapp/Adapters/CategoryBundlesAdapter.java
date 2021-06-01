package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.example.nbs.myfuelapp.R;

import java.util.List;

import static android.content.ContentValues.TAG;

public class CategoryBundlesAdapter extends RecyclerView.Adapter<CategoryBundlesAdapter.ViewHolder> {

    private List<FuelPackage> fuelPackages;
    private AppCompatActivity context;
    private Context _context;
    private SessionPreferences sessionPreferences;

    public CategoryBundlesAdapter(List<FuelPackage> fuelPackages,AppCompatActivity context) {
        this.fuelPackages = fuelPackages;
        this.context = context;
        this._context = context;
        sessionPreferences = new SessionPreferences(context);
    }

    @NonNull
    @Override
    public CategoryBundlesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.bundleelement,viewGroup,false);
        ViewHolder v = new ViewHolder(view);
        return v;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        viewHolder.tv.setText(String.format(String.format("%,.2f",fuelPackages.get(i).getPriceOfPackage())+" Ksh."));
        Log.e(TAG, "onBindViewHolder: category adapter"+String.valueOf(fuelPackages.get(i).getAmountOffuel())+" bundles for "+String.valueOf(fuelPackages.get(i).getPriceOfPackage())+" shillings" );
        viewHolder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return fuelPackages.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView tv;
        CardView container;
        LinearLayout cont;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.bundlename);
//            container = itemView.findViewById(R.id.bundleincategory);
            cont = itemView.findViewById(R.id.bndlcont);
        }
    }

}
