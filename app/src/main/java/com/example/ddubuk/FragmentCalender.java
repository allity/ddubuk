package com.example.ddubuk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class FragmentCalender extends Fragment implements View.OnClickListener {
    GregorianCalendar today = new GregorianCalendar();
    MainActivity ma = new MainActivity();
    Schedule schedule = new Schedule();
    FragmentAccount fragmentAccount = new FragmentAccount();
    ViewGroup viewGroup;

    DetailSchedule detailSchedule = new DetailSchedule();

    private int year = today.get(Calendar.YEAR);
    private int month = today.get(Calendar.MONTH) + 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ItineraryData> arrayList;

    private String id;
    private Button pre_date;
    private Button post_date;
    private TextView textView_Date;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_calender, container, false);

        recyclerView = viewGroup.findViewById(R.id.itinerary_recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        id = LoginActivity.getEmail();

        if (id != null) {
            setViewPlan();
        }

        this.InitializeView();
        this.SetListener();

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.create:
                if (id == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog??? ?????? activity??? argument??? ???????????? ??????.
                    builder.setTitle("???????????? ???????????????!\n????????? ???????????? ????????????????"); // AlertDialog.builder??? ?????? Title text??? ??????
                    builder.setPositiveButton("???", new DialogInterface.OnClickListener() { // AlertDialog.Builder??? Positive Button??? ??????
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent login = new Intent(getActivity(), LoginActivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(login);
                            getFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentAccount).commitAllowingStateLoss();
                        }
                    });
                    builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() { // AlertDialog.Builder??? Positive Button??? ??????
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create(); // ?????? builder??? ????????? AlertDialog ?????? ??????
                    dialog.show(); // dialog??? ????????? ?????? ???
                    break;
                } else {
                    AddPlanDialog dialog = AddPlanDialog.newInstance(new AddPlanDialog.NameInputListener() {
                        @Override
                        public void onNameInputComplete(String name) {
                            if (name != null) {
                                //name is Texted EditText
                            }
                        }
                    });
                    dialog.show(getFragmentManager(), "addDialog");
                    break;
                }
        }
    }

    public void InitializeView() {
        pre_date = (Button) viewGroup.findViewById(R.id.pre_date);
        post_date = (Button) viewGroup.findViewById(R.id.post_date);
        textView_Date = (TextView) viewGroup.findViewById(R.id.textView_Date);
        textView_Date.setText(year + "??? " + month + "???");

        Button create = (Button) viewGroup.findViewById(R.id.create);
        create.setOnClickListener(this);
    }

    public void SetListener() {
        View.OnClickListener Listener = new View.OnClickListener() {
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.pre_date:
                        if (month == 1) {
                            year = year - 1;
                            month = 12;
                        } else
                            month = month - 1;
                        textView_Date.setText(year + "??? " + month + "???");
                        arrayList.clear();
                        setViewPlan();
                        break;
                    case R.id.post_date:
                        if (month == 12) {
                            year = year + 1;
                            month = 1;
                        } else
                            month = month + 1;
                        textView_Date.setText(year + "??? " + month + "???");
                        arrayList.clear();
                        setViewPlan();
                        break;
                }
            }
        };
        pre_date.setOnClickListener(Listener);
        post_date.setOnClickListener(Listener);
    }

    public void setViewPlan() {
        ma.mdatabase = FirebaseDatabase.getInstance().getReference("?????????").child(id).child("??????");    //??????????????? ??????!!!

        ma.mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                ItineraryData itineraryData = dataSnapshot.getValue(ItineraryData.class);

                int depart = String.valueOf(dataSnapshot.getValue()).indexOf("departure");
                String sub = String.valueOf(dataSnapshot.getValue()).substring(depart);

                String array[] = sub.split(",");

                String a[] = array[0].split("=");
                String b[] = a[1].split("-");   //[0]:year, [1]:month, day

                if (Integer.parseInt(b[1]) < 10) {
                    if (b[0].equals(String.valueOf(year)) && b[1].equals("0" + String.valueOf(month))) {
                        arrayList.add(itineraryData);
                    }
                } else {
                    if (b[0].equals(String.valueOf(year)) && b[1].equals(String.valueOf(month))) {
                        arrayList.add(itineraryData);
                    }
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        adapter = new ItineraryAdapter(arrayList, getActivity());

        ((ItineraryAdapter) adapter).setOnItemClickListener(new ItineraryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, String tripName, String budget, String key, String use_money, String days, String departure) {
                schedule.setTravel_name(tripName);
                schedule.setBudget(budget);
                schedule.setKey(key);
                schedule.setUse_money(use_money);
                schedule.setDays(days);
                schedule.setDeparture(departure);
                getFragmentManager().beginTransaction().replace(R.id.main_layout, detailSchedule).commitAllowingStateLoss();
            }
        });

        ((ItineraryAdapter) adapter).setOnItemLongClickListener(new ItineraryAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, String del_key, int pos) {
                Log.e("<????????? ?????? ?????????>", del_key);

                deletePlan(del_key, pos);
            }
        });

        recyclerView.setAdapter(adapter);
    }

    public void deletePlan(String key, int pos) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog??? ?????? activity??? argument??? ???????????? ??????.
        builder.setTitle("??? ????????? ???????????????? "); // AlertDialog.builder??? ?????? Title text??? ??????
        builder.setPositiveButton("???", new DialogInterface.OnClickListener() { // AlertDialog.Builder??? Positive Button??? ??????
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ma.mdatabase = FirebaseDatabase.getInstance().getReference("?????????").child(id).child("??????").child(key);
                ma.mdatabase.removeValue();

                Toast.makeText(getContext(), "?????????????????????!", Toast.LENGTH_SHORT).show();

                // ArratList?????? ?????? ???????????? ????????????
                arrayList.remove(pos);


                // ??????????????? RecyclerView??? ??????????????? ?????????.
                adapter.notifyItemRemoved(pos);
                adapter.notifyItemRangeChanged(pos, arrayList.size());
            }
        });
        builder.setNegativeButton("?????????", new DialogInterface.OnClickListener() { // AlertDialog.Builder??? Positive Button??? ??????
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getContext(), "?????????????????????!", Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog dialog = builder.create(); // ?????? builder??? ????????? AlertDialog ?????? ??????
        dialog.show(); // dialog??? ????????? ?????? ???
    }
}
