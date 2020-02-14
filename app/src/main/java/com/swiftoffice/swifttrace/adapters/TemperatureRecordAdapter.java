package com.swiftoffice.swifttrace.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.swiftoffice.swifttrace.R;

public class TemperatureRecordAdapter extends RecyclerView.Adapter<TemperatureRecordAdapter.MyViewHolder>{
    Context context;

    public TemperatureRecordAdapter(Context context) {
        this.context= context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_temperature_record, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
//        holder.tvTempTime.setText();
    }

    @Override
    public int getItemCount() {
        return 3;
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
