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

    private View.OnClickListener mOnItemClickListener;

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

    //TODO: Step 2 of 4: Assign itemClickListener to your local View.OnClickListener variable
    public void setOnItemClickListener(View.OnClickListener itemClickListener) {
        mOnItemClickListener = itemClickListener;
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

            //TODO: Step 3 of 4: setTag() as current view holder along with
            // setOnClickListener() as your local View.OnClickListener variable.
            // You can set the same mOnItemClickListener on multiple views if required
            // and later differentiate those clicks using view's id.
            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }
}
