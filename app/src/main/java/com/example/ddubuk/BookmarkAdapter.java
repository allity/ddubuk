package com.example.ddubuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.CustomViewHolder> {

    Bookmark bookmark = new Bookmark();

    private ArrayList<Place> arrayList;
    private Context context;

    public BookmarkAdapter(ArrayList<Place> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item, parent, false);   //리사이클러뷰의 한 컬럼을 만들 때에 대한 리스트 아이템을 선언해줌
        CustomViewHolder holder = new CustomViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        if (String.valueOf(arrayList.get(position).getImg()).equals("null")) {
            if (position % 2 == 0)
                holder.iv_img.setImageResource(R.drawable.no_image);
            else
                holder.iv_img.setImageResource(R.drawable.no_image2);
        } else {
            holder.iv_img.setImageResource(R.drawable.no_image);
            Glide.with(holder.itemView)
                    .load(arrayList.get(position).getImg())
                    .into(holder.iv_img);
        }

        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_address.setText(arrayList.get(position).getAddress());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_img;
        TextView tv_name;
        TextView tv_address;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_img = itemView.findViewById(R.id.iv_img);
            this.tv_name = itemView.findViewById(R.id.tv_name);
            this.tv_address = itemView.findViewById(R.id.tv_address);
        }
    }
}