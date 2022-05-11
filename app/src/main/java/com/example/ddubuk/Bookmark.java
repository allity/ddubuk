package com.example.ddubuk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Bookmark extends Fragment {
    MainActivity ma = new MainActivity();
    ViewGroup viewGroup;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Place> arrayList;
    private String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.bookmark, container, false);
        Log.e("------->", "Bookmark Fragment");

        this.InitializeView();
        this.showBookmark();

        return viewGroup;
    }

    public void InitializeView(){
        recyclerView = viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true); //리사이클러뷰 기존 성능 강화
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>(); //Place 객체를 담을 arraylist(adapter쪽으로)
        id = LoginActivity.getEmail();
    }

    private void showBookmark() {
        ma.mdatabase = FirebaseDatabase.getInstance().getReference("사용자").child(id).child("북마크");
        ma.mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //firebase DB의 데이터를 받아오는 곳
                arrayList.clear();  // 기존 배열리스트가 존재하지 않게 초기화
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){    //반복문으로 데이터 list를 추출해냄
                    Place place = snapshot.getValue(Place.class);   //만들어뒀던 Place객체에 데이터를 담음
                    arrayList.add(place); //담은 데이터들을 배열리스트에 넣고 리사이클러뷰로 보낼 준비
                }
                adapter.notifyDataSetChanged(); //리스트 저장 및 새로고침
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //디비를 가져오던 중 에러 발생 시
                Log.e("Bookmark", String.valueOf(databaseError.toException())); //에러를 출력
            }
        });
        adapter = new BookmarkAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);   //리사이클러뷰에 어뎁터 연결
    }
}