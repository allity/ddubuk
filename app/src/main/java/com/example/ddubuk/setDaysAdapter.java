package com.example.ddubuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class setDaysAdapter extends RecyclerView.Adapter<setDaysAdapter.setDaysViewHolder> {
    MainActivity ma = new MainActivity();

    private ArrayList<setDays> arrayList;
    private Context context;

    public setDaysAdapter(ArrayList<setDays> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public setDaysAdapter.setDaysViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_daybutton, parent, false);
        setDaysAdapter.setDaysViewHolder holder = new setDaysAdapter.setDaysViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final setDaysAdapter.setDaysViewHolder holder, final int position) {
        holder.day.setText(arrayList.get(position).getDay());

        holder.day.setOnClickListener(v -> {
            FragmentCategory.getday = String.valueOf(Integer.parseInt(arrayList.get(position).getDay()));
            FragmentCategory.updateplan();
        });


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class setDaysViewHolder extends RecyclerView.ViewHolder {
        Button day;

        public setDaysViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dayButton100);
        }
    }
}