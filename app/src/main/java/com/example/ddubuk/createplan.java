package com.example.ddubuk;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class createplan extends AppCompatActivity {

    EditText p1;
    EditText p2;
    EditText p3;
    EditText p4;

    EditText d1;
    EditText d2;
    EditText d3;

    Button fifi;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    String user = "zwon5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createplan);

        setup();
    }
    private void setup() {

        p1 = findViewById(R.id.pname1);
        p2 = findViewById(R.id.pname2);
        p3 = findViewById(R.id.pname3);
        p4 = findViewById(R.id.pname4);

        d1 = findViewById(R.id.dname1);
        d2 = findViewById(R.id.dname2);
        d3 = findViewById(R.id.dname3);

        fifi = findViewById(R.id.fifi);
        fifi.setOnClickListener(listener);
    }

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.fifi:
                        exec();
                        Intent intent = new Intent(createplan.this, recommend.class);
                        startActivity(intent);
                        break;
                }
            }
        };

    private void exec(){
        DatabaseReference getFrq = database.getReference("여행지");

        getFrq.addListenerForSingleValueEvent(new ValueEventListener() {
            String hash="";
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Custom custom = snapshot.getValue(Custom.class);
                    assert custom != null;
                    if(custom.getName().equals(p1.getText().toString()))
                        hash = hash.concat(custom.getHashtag()).concat(",");
                    else if(custom.getName().equals(p2.getText().toString()))
                        hash = hash.concat(custom.getHashtag()).concat(",");
                    else if(custom.getName().equals(p3.getText().toString()))
                        hash = hash.concat(custom.getHashtag()).concat(",");
                    else if(custom.getName().equals(p4.getText().toString()))
                        hash = hash.concat(custom.getHashtag()).concat(",");
                }
                calANDinsert(hash);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });
    }
    private void calANDinsert(String hash){

        String h[] = hash.split(",");
        int count=0;
        int c[] = new int[h.length];

        for(int i = 0; i<h.length; i++){
            if(!h[i].equals("9999")){
                for(int j = i+1; j<h.length; j++){
                    if(h[i].equals(h[j])){
                        count++;
                        h[j] = "9999";
                    }
                }
                c[i] = count;
            }
        }
        int temp=0;
        int max=0;
        String stemp="";

        for(int i = 0; i < c.length; i++){
            for(int j = i+1; j < c.length; j++) {
                if(c[max] < c[j]){
                    max=j;
                }
            }
            temp = c[i]; stemp = h[i];
            c[i] = c[max]; h[i] = h[max];
            c[max] = temp; h[max] = stemp;
        }

        String htag = "";
        for(int i = 0; i < c.length; i++){
            if(!h[i].equals("9999"))
                htag = htag.concat(h[i]).concat(",");
        }

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("사용자").child(user).child("hash");

        Map<String, userhash> hashtag = new HashMap<>();
        userhash userhash = new userhash();
        userhash.setTag(htag);
        hashtag.put("in", userhash);
        ref.setValue(hashtag);
    }
}
