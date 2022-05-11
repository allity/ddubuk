package com.example.ddubuk;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.ArrayList;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

public class WalkAdapter extends RecyclerView.Adapter<WalkAdapter.WalkViewHolder> {
    MainActivity ma = new MainActivity();
    LoginActivity loginActivity = new LoginActivity();

    private ArrayList<walk> arrayList;
    private Context context;

    public WalkAdapter(ArrayList<walk> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public WalkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
        WalkViewHolder holder = new WalkViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final WalkViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .load(arrayList.get(position).getImg())
                .into(holder.iv_emg);
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_region.setText(arrayList.get(position).getRegion());

        if (loginActivity.getEmail() != null) {
            Query query = ma.mdatabase.child("사용자").child(loginActivity.getEmail()).child("북마크").orderByChild("name").equalTo(arrayList.get(position).getName());

            query.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull final DataSnapshot dataSnapshot, @Nullable String s) {
                    for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                        if (postSnapshot.exists()) {
                            holder.btn_bookmark.setVisibility(View.GONE);
                            holder.btn_bookmark2.setVisibility(View.VISIBLE);
                        }

                        holder.btn_bookmark2.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String ref = String.valueOf(dataSnapshot.getRef());
                                String[] cut_ref = ref.split("/");
                                String key = cut_ref[6];
                                Log.e("<split ref>", key);
                                ma.mdatabase.child("사용자").child(loginActivity.getEmail()).child("북마크").child(key).removeValue();

                                holder.btn_bookmark2.setVisibility(View.GONE);
                                holder.btn_bookmark.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
                }
            });
        }

        holder.btn_bookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (loginActivity.getEmail() != null && holder.btn_bookmark.getVisibility() == View.VISIBLE) {
                    HashMap newBookmark = new HashMap<>();
                    newBookmark.put("address", arrayList.get(position).getRegion());
                    newBookmark.put("img", arrayList.get(position).getImg());
                    newBookmark.put("name", arrayList.get(position).getName());

                    ma.mdatabase = FirebaseDatabase.getInstance().getReference();
                    ma.mdatabase.child("사용자").child(loginActivity.getEmail()).child("북마크").push().setValue(newBookmark);

                    holder.btn_bookmark.setVisibility(View.GONE);
                    holder.btn_bookmark2.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(context, "로그인이 필요합니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class WalkViewHolder extends RecyclerView.ViewHolder {

        ImageView iv_emg;
        TextView tv_name;
        TextView tv_region;
        ImageButton btn_bookmark;
        ImageButton btn_bookmark2;

        public WalkViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_emg = itemView.findViewById(R.id.iv_emg);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_region = itemView.findViewById(R.id.tv_region);
            btn_bookmark = itemView.findViewById(R.id.btn_bookmark);
            btn_bookmark2 = itemView.findViewById(R.id.btn_bookmark2);
        }
    }
}
