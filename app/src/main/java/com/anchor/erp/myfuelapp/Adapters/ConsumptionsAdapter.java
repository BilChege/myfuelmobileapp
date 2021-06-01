package com.anchor.erp.myfuelapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.nbs.myfuelapp.R;

import java.util.List;

public class ConsumptionsAdapter extends BaseAdapter {

    private Context context;
    private List<String> regs;
    private List<String> values;
    private LayoutInflater inflater;

    public ConsumptionsAdapter(Context context, List<String> regs, List<String> values) {
        this.context = context;
        this.regs = regs;
        this.values = values;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return values.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.consumption_item,parent,false);
        TextView cicarreg = convertView.findViewById(R.id.cicarreg);
        TextView cittamt = convertView.findViewById(R.id.cittamt);
        cicarreg.setText(regs.get(position));
        cittamt.setText(String.format("%,.2f",Double.parseDouble(values.get(position))));

        return convertView;
    }
}
