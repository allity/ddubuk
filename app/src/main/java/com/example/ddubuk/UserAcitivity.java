package com.example.ddubuk;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;

public class UserAcitivity extends AppCompatActivity {
    MainActivity ma = new MainActivity();

    private ListView user_list;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> arrayList;

    private int n = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        user_list = (ListView) findViewById(R.id.user_list);

        showUserList();
    }

    private void showUserList() {
        // 리스트 어댑터 생성 및 세팅
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        user_list.setAdapter(adapter);

        arrayList = new ArrayList<String>();

        // 데이터 받아오기 및 어댑터 데이터 추가 및 삭제 등..리스너 관리

        ma.mdatabase.child("사용자").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e("LOG", "dataSnapshot.getKey() : " + dataSnapshot.getKey());
                adapter.add(dataSnapshot.getKey());
                arrayList.add(dataSnapshot.getKey());

                Log.e("List-------->", String.valueOf(arrayList));
                if (checkUser(arrayList, dataSnapshot.getKey())) {
                    Log.e("-------------->", "기존회원");
                }
                else {
                    Log.e("-------------->", "신규회원");
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        });

    }

    public boolean checkUser(ArrayList<String> arrayList, String id) {
        if (arrayList.contains(id))
            return true;
        else return false;
    }
}