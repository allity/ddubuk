package com.example.ddubuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class festivalAdapter extends RecyclerView.Adapter<festivalAdapter.festivalViewHolder>{
    MainActivity ma = new MainActivity();

    private ArrayList<festival> arrayList;
    private Context context;

    public festivalAdapter(ArrayList<festival> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public festivalAdapter.festivalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_festival, parent, false);
        festivalAdapter.festivalViewHolder holder = new festivalAdapter.festivalViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final festivalAdapter.festivalViewHolder holder, final int position) {
        holder.tv_festivalName.setText(arrayList.get(position).getFestivalname());
        holder.tv_festivalDate.setText(arrayList.get(position).getFestivalDate());
        holder.tv_festivalPhone.setText(arrayList.get(position).getFestivalPhone());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class festivalViewHolder extends RecyclerView.ViewHolder {
        TextView tv_festivalName;
        TextView tv_festivalDate;
        TextView tv_festivalPhone;

        public festivalViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_festivalName = itemView.findViewById(R.id.tv_festivalName);
            tv_festivalDate = itemView.findViewById(R.id.tv_festivalName);
            tv_festivalPhone = itemView.findViewById(R.id.tv_festivalName);
        }
    }
}
