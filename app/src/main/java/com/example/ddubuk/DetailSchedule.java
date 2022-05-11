package com.example.ddubuk;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailSchedule extends Fragment implements View.OnClickListener {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<DetailData> arrayList;

    FragmentManager fgm;

    MainActivity ma = new MainActivity();
    Budget budget = new Budget();
    Schedule schedule = new Schedule();

    ArrayList<DayData> items;

    LinearLayout testLayout;
    ImageButton btn_modify;

    ViewGroup viewGroup;
    TextView viewName;
    ListView listViewDays;
    String userId;
    int d;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.detail_schedule, container, false);

        fgm = getFragmentManager();

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();
        recyclerView = viewGroup.findViewById(R.id.detailRecycler);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        viewName = (TextView) viewGroup.findViewById(R.id.viewName);
        viewName.setText(schedule.getTravel_name());

        userId = LoginActivity.getEmail();

        listViewDays = (ListView) viewGroup.findViewById(R.id.viewDays);

        items = new ArrayList<DayData>();

        d = Integer.parseInt(schedule.getDays());

        for (int i = 0; i < d; i++) {
            DayData oItem = new DayData();
            oItem.day = (i + 1) + "일차";
            items.add(oItem);
        }

        // ListView, Adapter 생성 및 연결 ------------------------
        DayAdapter oAdapter = new DayAdapter(items);
        listViewDays.setAdapter(oAdapter);

        testLayout = (LinearLayout) viewGroup.findViewById(R.id.testLayout);
        listViewDays.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                testLayout.setVisibility(View.VISIBLE);
                listViewDays.setVisibility(View.GONE);
                Button btn_previous = (Button) viewGroup.findViewById(R.id.btn_previous);
                btn_previous.setText(position + 1 + "일차");
                getSchedule(position + 1);
            }
        });

        Button btn_previous = (Button) viewGroup.findViewById(R.id.btn_previous);
        btn_previous.setOnClickListener(this);

        Button budget = (Button) viewGroup.findViewById(R.id.budget);
        budget.setOnClickListener(this);

        btn_modify = (ImageButton) viewGroup.findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(this);

        ImageButton btn_back = (ImageButton) viewGroup.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.budget:
                getFragmentManager().beginTransaction().replace(R.id.main_layout, budget).commitAllowingStateLoss();
                break;
            case R.id.btn_previous:
                testLayout.setVisibility(View.GONE);
                listViewDays.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_modify:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                builder.setTitle("어떤 것을 수정할까요?"); // AlertDialog.builder를 통해 Title text를 입력
                builder.setPositiveButton("여행 이름", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        modifyTravelName();
                    }
                });
                builder.setNegativeButton("여행 날짜", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //추가~~~~~~~~~~~~~~~~~~~~//
                    }
                });
                AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                dialog.show(); // dialog를 화면에 뿌려 줌

                break;
            case R.id.btn_back:
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout, new FragmentCalender())
                        .addToBackStack(null)
                        .commit();
                break;
        }
    }

    public void getSchedule(int pos) {
        ma.mdatabase = FirebaseDatabase.getInstance().getReference("사용자")
                .child(userId).child("일정").child(schedule.getKey()).child("detail").child(String.valueOf(pos));    //사용자에게 접근!!!
        ma.mdatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    DetailData detailData = snapshot.getValue(DetailData.class);
                    arrayList.add(detailData);
                    FragmentViewPath.ds.add(detailData);
                    Log.e("<detail schedule>", String.valueOf(snapshot.getValue()));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Detail recyclerView", String.valueOf(databaseError.toException()));
            }
        });
        adapter = new DetailAdapter(arrayList, getActivity(), fgm);

        ((DetailAdapter) adapter).setOnItemClickListener(new DetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String placeName, String placeAddress) {

            }
        });

        recyclerView.setAdapter(adapter);
    }

    public void modifyTravelName() {
        EditText modifyName = (EditText) viewGroup.findViewById(R.id.modify_name);
        ImageButton btn_okay = (ImageButton) viewGroup.findViewById(R.id.btn_okay);

        viewName.setVisibility(View.GONE);
        modifyName.setVisibility(View.VISIBLE);

        btn_modify.setVisibility(View.GONE);
        btn_okay.setVisibility(View.VISIBLE);

        btn_okay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String n = String.valueOf(modifyName.getText());

                viewName.setText(n);

                viewName.setVisibility(View.VISIBLE);
                modifyName.setVisibility(View.GONE);

                btn_modify.setVisibility(View.VISIBLE);
                btn_okay.setVisibility(View.GONE);

                HashMap newName = new HashMap<>();
                newName.put("travel_name", n);

                ma.mdatabase.child("사용자").child(userId).child("일정").child(schedule.getKey()).updateChildren(newName);
                schedule.setTravel_name(n);

                Toast.makeText(getContext(), "수정되었습니다!", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
