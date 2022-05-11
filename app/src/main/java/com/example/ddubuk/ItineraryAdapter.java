package com.example.ddubuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ItineraryAdapter extends RecyclerView.Adapter<ItineraryAdapter.CustomViewHolder> {

    interface OnItemClickListener {
        void onItemClick(View view, String tripName, String budget, String key, String use_money, String days, String departure);  //보내줄 정보
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, String del_key, int position);
    }

    OnItemClickListener mListener;
    private OnItemLongClickListener mLongListener = null;


    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

    private ArrayList<ItineraryData> arrayList;
    private Context context;

    public ItineraryAdapter(ArrayList<ItineraryData> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itinerary_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.travel_name.setText(arrayList.get(position).getTravel_name());
        holder.travel_period.setText(arrayList.get(position).getDeparture() + " ~ " + arrayList.get(position).getArrival());
        holder.budget.setText(arrayList.get(position).getBudget());
        holder.use_money.setText(arrayList.get(position).getUse_money());
        holder.key.setText(arrayList.get(position).getKey());
        holder.days.setText(arrayList.get(position).getDays());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.itemView, arrayList.get(position).getTravel_name(),
                            arrayList.get(position).getBudget(), arrayList.get(position).getKey(),
                            arrayList.get(position).getUse_money(), arrayList.get(position).getDays(),
                            arrayList.get(position).getDeparture());
                }
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mLongListener.onItemLongClick(v, arrayList.get(position).getKey(), position);
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView travel_name;
        TextView travel_period;
        TextView budget;
        TextView use_money;
        TextView key;
        TextView days;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.travel_name = itemView.findViewById(R.id.travel_name);
            this.travel_period = itemView.findViewById(R.id.travel_period);
            this.budget = itemView.findViewById(R.id.budget);
            this.use_money = itemView.findViewById(R.id.use_money);
            this.key = itemView.findViewById(R.id.key);
            this.days = itemView.findViewById(R.id.days);
        }
    }
}