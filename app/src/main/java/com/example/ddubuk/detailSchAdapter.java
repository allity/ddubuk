package com.example.ddubuk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class detailSchAdapter extends RecyclerView.Adapter<detailSchAdapter.detailSchViewHolder> {
    MainActivity ma = new MainActivity();

    private ArrayList<daySchedule> arrayList;
    //private Context context;

    public detailSchAdapter(ArrayList<daySchedule> arrayList) {
        this.arrayList = arrayList;
        //this.context = context;
    }

    @NonNull
    @Override
    public detailSchAdapter.detailSchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_detailsch, parent, false);
        detailSchAdapter.detailSchViewHolder holder = new detailSchAdapter.detailSchViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final detailSchAdapter.detailSchViewHolder holder, final int position) {
        holder.planname.setText(arrayList.get(position).getName());
        holder.placeNum.setText(String.valueOf(position + 1));

        holder.del_Plan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                arrayList.remove(position);


                // 어댑터에서 RecyclerView에 반영하도록 합니다.
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, arrayList.size());
            }
        });

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class detailSchViewHolder extends RecyclerView.ViewHolder {
        TextView planname;
        ImageButton del_Plan;
        TextView placeNum;

        public detailSchViewHolder(@NonNull View itemView) {
            super(itemView);
            planname = itemView.findViewById(R.id.planname);
            del_Plan = itemView.findViewById(R.id.del_plan);
            placeNum = itemView.findViewById(R.id.place_num);
        }
    }
}