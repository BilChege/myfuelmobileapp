package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Database.SessionPreferences;
import com.anchor.erp.myfuelapp.Models.OffersForMobile;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.util.List;

public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.ViewHolder> {

    private Context context;
    private List<OffersForMobile> offers;
    private SessionPreferences sessionPreferences;
    private Config.OffersListener listener;

    public OffersAdapter(Context context, List<OffersForMobile> offers) {
        this.context = context;
        listener = (Config.OffersListener) context;
        this.offers = offers;
        sessionPreferences = new SessionPreferences(this.context);
    }

    @Override
    public OffersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.offer_element,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final OffersForMobile offer = offers.get(position);
        holder.offername.setText(offer.getPromoname());
        holder.offervalue.setText("Redeem "+String.valueOf(offer.getPoints())+" points");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.offerSelected(offer);
            }
        });
    }

    @Override
    public int getItemCount() {
        return offers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView offername,offervalue;

        public ViewHolder(View itemView) {
            super(itemView);
            offername = itemView.findViewById(R.id.offername);
            offervalue = itemView.findViewById(R.id.offerpoints);
        }
    }

}
