package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.anchor.erp.myfuelapp.Models.FuelCar;
import com.example.nbs.myfuelapp.R;
import com.anchor.erp.myfuelapp.Utils.Config;

import java.text.SimpleDateFormat;
import java.util.List;

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private Context context;
    private List<FuelCar> fuelCars;
    private SimpleDateFormat simpleDateFormat;
    private Config.TransactionsListener listener;

    public TransactionsAdapter(Context context, List<FuelCar> fuelCars) {
        this.context = context;
        this.fuelCars = fuelCars;
        this.listener = (Config.TransactionsListener) context;
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    }

    @NonNull
    @Override
    public TransactionsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.transactionelement,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionsAdapter.ViewHolder holder, int position) {
        final FuelCar fuelCar = fuelCars.get(position);
        holder.station.setText(fuelCar.getStationid());
        holder.vehicle.setText(fuelCar.getVehicle().getRegno());
        holder.date.setText(simpleDateFormat.format(fuelCar.getDateFueled()));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.transactionSelected(fuelCar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fuelCars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView station, vehicle, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            station = itemView.findViewById(R.id.etstationname);
            vehicle = itemView.findViewById(R.id.vehicle);
            date = itemView.findViewById(R.id.etdate);
        }
    }
}
