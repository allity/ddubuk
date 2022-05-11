package com.example.ddubuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class DetailAdapter extends RecyclerView.Adapter<DetailAdapter.CustomViewHolder> {
    FragmentManager fgm;

    interface OnItemClickListener {
        void onItemClick(View view, String placeName, String placeAddress);  //보내줄 정보
    }

    OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    private ArrayList<DetailData> arrayList;
    private Context context;

    public DetailAdapter(ArrayList<DetailData> arrayList, Context context, FragmentManager fgm) {
        this.arrayList = arrayList;
        this.context = context;
        this.fgm = fgm;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.detail_item, parent, false);
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CustomViewHolder holder, final int position) {
        holder.view_num.setText(String.valueOf(position + 1));
        holder.placeName.setText(arrayList.get(position).getPlace_name());
        holder.placeAddress.setText(arrayList.get(position).getPlace_address());

        if (arrayList.get(position).getTransportation().equals("")) {
            holder.transportation.setText("");
            holder.road.setVisibility(View.INVISIBLE);
            holder.plainText.setVisibility(View.INVISIBLE);
//            holder.viewPath.setVisibility(View.INVISIBLE);
        }
        else
            holder.transportation.setText(arrayList.get(position).getTransportation());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    mListener.onItemClick(holder.itemView, arrayList.get(position).getPlace_name(), arrayList.get(position).getPlace_address());
                }
            }
        });

        holder.road.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!arrayList.get(position).getTransportation().equals("")) {
                    FragmentViewPath.pathlatitude = arrayList.get(position).getLatitude();
                    FragmentViewPath.pathname = arrayList.get(position).getPlace_name();
                    FragmentViewPath.pathpath = arrayList.get(position).getPath();
                    FragmentViewPath.pathlongitude = arrayList.get(position).getLongitude();
                    FragmentViewPath.latitude2 = arrayList.get(position + 1).getLatitude();
                    FragmentViewPath.longitude2 = arrayList.get(position + 1).getLongitude();
                    FragmentViewPath.pathname2 = arrayList.get(position + 1).getPlace_name();
                    fgm.beginTransaction().replace(R.id.main_layout, new FragmentViewPath()).commitAllowingStateLoss();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView placeName;
        TextView placeAddress;
        TextView view_num;
        TextView transportation;
        ImageButton road;
        TextView plainText;
//        Button viewPath;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.placeName = itemView.findViewById(R.id.view_place);
            this.placeAddress = itemView.findViewById(R.id.view_address);
            this.view_num = itemView.findViewById(R.id.view_num);
            this.transportation = itemView.findViewById(R.id.transportation);
            this.road = itemView.findViewById(R.id.road);
            this.plainText = itemView.findViewById(R.id.plainText);
//            this.viewPath = itemView.findViewById(R.id.viewPath);
        }
    }
}
