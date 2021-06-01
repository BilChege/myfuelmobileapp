package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Activities.CategoryBundles;
import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.FuelPackage;
import com.example.nbs.myfuelapp.R;

import java.util.ArrayList;
import java.util.List;

public class BundlesAdapter extends RecyclerView.Adapter<BundlesAdapter.ViewHolder> {

    private List<String> titles;
    private AppCompatActivity context;
    private Context _context;
    private List<FuelPackage> fuelPackages;

    public BundlesAdapter(List<String> titles, List<FuelPackage> packages, AppCompatActivity context) {
        this.titles = titles;
        this.context = context;
        this._context = context;
        this.fuelPackages = packages;
    }

    @NonNull
    @Override
    public BundlesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_element,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final BundlesAdapter.ViewHolder holder, final int position) {
        holder.tv.setText(titles.get(position));
        holder.choiceview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<FuelPackage> _fuelPackages = new ArrayList<>();
                for (FuelPackage f:fuelPackages){
                    if (f.getTypeOfPackage().equals(titles.get(position))){
                        _fuelPackages.add(f);
                    }
                }
                int numpackages = new SessionPreferences(_context).createFuelPackages(_fuelPackages);
                Intent i = new Intent(_context, CategoryBundles.class);
                i.putExtra("numpackages",numpackages);
                i.putExtra("page title",titles.get(position));
                _context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView tv;
        private CardView choiceview;

        public ViewHolder(final View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.bundlecategory);
//            choiceview = itemView.findViewById(R.id.card);
        }
    }

}
