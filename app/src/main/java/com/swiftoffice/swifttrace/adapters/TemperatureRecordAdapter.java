package com.swiftoffice.swifttrace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.swiftoffice.swifttrace.R;

import java.util.ArrayList;
import java.util.HashMap;

public class TemperatureRecordAdapter extends RecyclerView.Adapter<TemperatureRecordAdapter.MyViewHolder>{

    ArrayList<HashMap<String,String>> TemperatureRecordList;

    Context context;

    public TemperatureRecordAdapter(Context context, ArrayList<HashMap<String, String>> temptrecordList) {
        this.context= context;
        TemperatureRecordList = temptrecordList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temperature_record, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvTempDate.setText(TemperatureRecordList.get(position).get("Date"));
        holder.tvTempTime.setText(TemperatureRecordList.get(position).get("Time"));
        holder.tvTempReadingValue.setText(TemperatureRecordList.get(position).get("Temperature"));
//        holder.tvTempTime.setText();
    }

    @Override
    public int getItemCount() {
        return TemperatureRecordList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTempTime,tvTempDate, tvTempReadingValue;
        public MyViewHolder(View itemView) {
            super(itemView);
            tvTempTime= (TextView)itemView.findViewById(R.id.tvTempTime);
            tvTempDate= (TextView)itemView.findViewById(R.id.tvTempDate);
            tvTempReadingValue = (TextView)itemView.findViewById(R.id.tvTempReadingValue);


        }
    }
}
