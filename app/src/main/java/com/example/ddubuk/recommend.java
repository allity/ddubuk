package com.example.ddubuk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class recommend extends Fragment {
    private RecyclerView.Adapter adapter;
    private ArrayList<walk> arrayList;

    private RecyclerView.Adapter adapter2;
    private ArrayList<Gangtop> arrayList2;

    private RecyclerView.Adapter adapter3;
    private ArrayList<Seotop> arrayList3;

    private RecyclerView.Adapter adapter4;
    private ArrayList<Yeotop> arrayList4;

    private RecyclerView.Adapter adapter5;
    private ArrayList<Chuntop> arrayList5;

    private RecyclerView.Adapter adapter6;
    private ArrayList<Custom> arrayList6;

    private String user = "zwon5";

    private ArrayList<userhash> arrayListU;

    private ArrayList<String> arrayListA;

    ViewGroup viewGroup;

    LinearLayout layout_1;
    ImageButton button1;

    LinearLayout layout_2;
    ImageButton button2;

    LinearLayout layout_3;
    ImageButton button3;

    LinearLayout layout_4;
    ImageButton button4;

    LinearLayout layout_5;
    ImageButton button5;

    LinearLayout layout_6;
    ImageButton button6;

    LinearLayout layout_7;
    ImageButton button7;

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_recommend, container, false);

        setup();

        Walkrecom();
        Gangtop10();
        Seotop10();
        Yeotop10();
        Chuntop10();
        getUsertag();

        return viewGroup;
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_walkaway);
//        setup();
//
//        Walkrecom();
//        Gangtop10();
//        Seotop10();
//        Yeotop10();
//        Chuntop10();
//        getUsertag();
//    }



    private void setup(){
        layout_1 = viewGroup.findViewById(R.id.linearLayout3);
        button1 = viewGroup.findViewById(R.id.iv_walk);
        button1.setOnClickListener(myListener);

        layout_2 = viewGroup.findViewById(R.id.linearLayout5);
        button2 = viewGroup.findViewById(R.id.iv_top10);
        button2.setOnClickListener(myListener);

        layout_3 = viewGroup.findViewById(R.id.linearLayoutGang);
        button3 = viewGroup.findViewById(R.id.iv_Gangtop10);
        button3.setOnClickListener(myListener);

        layout_4 = viewGroup.findViewById(R.id.linearLayoutSeo);
        button4 = viewGroup.findViewById(R.id.iv_Seotop10);
        button4.setOnClickListener(myListener);

        layout_5 = viewGroup.findViewById(R.id.linearLayoutYeo);
        button5 = viewGroup.findViewById(R.id.iv_Yeotop10);
        button5.setOnClickListener(myListener);

        layout_6 = viewGroup.findViewById(R.id.linearLayoutChun);
        button6 = viewGroup.findViewById(R.id.iv_Chuntop10);
        button6.setOnClickListener(myListener);

        layout_7 = viewGroup.findViewById(R.id.linearLayoutCustom);
        button7 = viewGroup.findViewById(R.id.iv_custom);
        button7.setOnClickListener(myListener);
    }

    View.OnClickListener myListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.iv_top10:
                    layout_2.setVisibility(layout_2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.iv_walk:
                    layout_1.setVisibility(layout_1.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.iv_Gangtop10:
                    layout_3.setVisibility(layout_3.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.iv_Seotop10:
                    layout_4.setVisibility(layout_4.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.iv_Yeotop10:
                    layout_5.setVisibility(layout_5.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.iv_Chuntop10:
                    layout_6.setVisibility(layout_6.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
                case R.id.iv_custom:
                    layout_7.setVisibility(layout_7.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    break;
            }
        }
    };
    private void Walkrecom(){
        RecyclerView recyclerView = viewGroup.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        DatabaseReference databaseReference = database.getReference("걷기좋은길");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    walk walk = snapshot.getValue(walk.class);
                    arrayList.add(walk);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });

        adapter = new WalkAdapter(arrayList, getActivity());
        recyclerView.setAdapter(adapter);
    }

    private void Gangtop10(){
        RecyclerView recyclerView2 = viewGroup.findViewById(R.id.rv_Gangtop10);
        recyclerView2.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        recyclerView2.setLayoutManager(layoutManager2);
        arrayList2 = new ArrayList<>();

        DatabaseReference databaseReferenceGangtop = database.getReference("도시별 Top10").child("강릉");

        databaseReferenceGangtop.addListenerForSingleValueEvent(new ValueEventListener() {
            Gangtop top10;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList2.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Gangtop top = snapshot.getValue(Gangtop.class);
                    if(snapshot.getKey().equals("Top10"))
                    {
                        top10 = top;
                        continue;
                    }
                    arrayList2.add(top);
                }
                arrayList2.add(top10);
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });

        adapter2 = new GangTopAdapter(arrayList2, getActivity());
        recyclerView2.setAdapter(adapter2);
    }

    private void Seotop10(){
        RecyclerView recyclerView3 = viewGroup.findViewById(R.id.rv_Seotop10);
        recyclerView3.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        recyclerView3.setLayoutManager(layoutManager3);
        arrayList3 = new ArrayList<>();

        DatabaseReference databaseReferenceSeotop = database.getReference("도시별 Top10").child("서울");

        databaseReferenceSeotop.addListenerForSingleValueEvent(new ValueEventListener() {
            Seotop top10;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList3.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Seotop top = snapshot.getValue(Seotop.class);
                    if(snapshot.getKey().equals("Top10"))
                    {
                        top10 = top;
                        continue;
                    }
                    arrayList3.add(top);
                }
                arrayList3.add(top10);
                adapter3.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });

        adapter3 = new SeotopAdapter(arrayList3, getActivity());
        recyclerView3.setAdapter(adapter3);
    }

    private void Yeotop10(){
        RecyclerView recyclerView4 = viewGroup.findViewById(R.id.rv_Yeotop10);
        recyclerView4.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager4 = new LinearLayoutManager(getActivity());
        recyclerView4.setLayoutManager(layoutManager4);
        arrayList4 = new ArrayList<>();

        DatabaseReference databaseReferenceYeotop = database.getReference("도시별 Top10").child("여수");

        databaseReferenceYeotop.addListenerForSingleValueEvent(new ValueEventListener() {
            Yeotop top10;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList4.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Yeotop top = snapshot.getValue(Yeotop.class);
                    if(snapshot.getKey().equals("Top10"))
                    {
                        top10 = top;
                        continue;
                    }
                    arrayList4.add(top);
                }
                arrayList4.add(top10);
                adapter4.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });

        adapter4 = new YeotopAdapter(arrayList4, getActivity());
        recyclerView4.setAdapter(adapter4);
    }

    private void Chuntop10(){
        RecyclerView recyclerView5 = viewGroup.findViewById(R.id.rv_Chuntop10);
        recyclerView5.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager5 = new LinearLayoutManager(getActivity());
        recyclerView5.setLayoutManager(layoutManager5);
        arrayList5 = new ArrayList<>();

        DatabaseReference databaseReferenceChuntop = database.getReference("도시별 Top10").child("춘천");

        databaseReferenceChuntop.addListenerForSingleValueEvent(new ValueEventListener() {
            Chuntop top10;
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList5.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Chuntop top = snapshot.getValue(Chuntop.class);
                    if(snapshot.getKey().equals("Top10"))
                    {
                        top10 = top;
                        continue;
                    }
                    arrayList5.add(top);
                }
                arrayList5.add(top10);
                adapter5.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });

        adapter5 = new ChunTopAdapter(arrayList5, getActivity());
        recyclerView5.setAdapter(adapter5);
    }

    private void getUsertag(){
        arrayListU = new ArrayList<>();
        DatabaseReference databaseReferenceuserhash = database.getReference("사용자").child(user).child("hash");

        databaseReferenceuserhash.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListU.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    userhash userhash = snapshot.getValue(userhash.class);
                    anotherUser(userhash.getTag());

                    arrayListU.add(userhash);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });
    }

    private void Coustomrecommend(final String h[], final String at){
        RecyclerView recyclerView6 = viewGroup.findViewById(R.id.rv_Custom);
        recyclerView6.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager6 = new LinearLayoutManager(getActivity());
        recyclerView6.setLayoutManager(layoutManager6);
        arrayList6 = new ArrayList<>();

        DatabaseReference databaseReferenceCustomrecommend = database.getReference("여행지");

        databaseReferenceCustomrecommend.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList6.clear();
                int c1=0;
                int c2=0;
                int c3=0;
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Custom custom = snapshot.getValue(Custom.class);

                    Log.d("1", h[0]);
                    Log.d("1", at);
                    Log.d("1", h[1]);
                    if(custom.getHashtag().contains(h[0])) {
                        if(c1<4) {
                            arrayList6.add(custom);
                            c1++;
                        }
                    }
                    else if(custom.getHashtag().contains(at)) {
                        if(c2<3) {
                            arrayList6.add(custom);
                            c2++;
                        }
                    }
                    else if(custom.getHashtag().contains(h[1])) {
                        if(c3<3) {
                            arrayList6.add(custom);
                            c3++;
                        }
                    }
                }
                adapter6.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });

        adapter6 = new CustomAdapter(arrayList6, getActivity());
        recyclerView6.setAdapter(adapter6);
    }

    private void anotherUser(final String has){
        arrayListA = new ArrayList<>();

        DatabaseReference anotherUSER = database.getReference("사용자");

        anotherUSER.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayListA.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    arrayListA.add(snapshot.getKey());
                }
                anotherUserHash(arrayListA, has);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });
    }

    private void anotherUserHash(ArrayList<String> arrayListA, final String has){
        arrayListA.remove(user);
        final String h[] = has.split(",");

        for (String s : arrayListA) {
            DatabaseReference anotherUSERhas = database.getReference("사용자").child(s.toString()).child("hash");

            anotherUSERhas.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int flag = 0;
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        userhash userhash = snapshot.getValue(userhash.class);


                        String anhas[] = userhash.getTag().split(",");

                        if (anhas[0].equals(h[0]) && !anhas[1].equals(h[1])) {
                            flag = 1;
                            Coustomrecommend(h, anhas[1]);
                            break;
                        } else if (anhas[0].equals(h[0]) && anhas[1].equals(h[1]) && !anhas[2].equals(h[2])) {
                            flag = 1;
                            Coustomrecommend(h, anhas[2]);
                            break;
                        } else if (anhas[1].equals(h[0]) && anhas[2].equals(h[1]) && !anhas[0].equals(h[2])) {
                            flag = 1;
                            Coustomrecommend(h, anhas[0]);
                            break;
                        }
                    }
                    if (flag == 0)
                        Coustomrecommend(h, h[0]);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("walkaway", String.valueOf(databaseError.toException()));
                }
            });
        }
    }
}
