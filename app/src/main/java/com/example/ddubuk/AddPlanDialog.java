package com.example.ddubuk;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class AddPlanDialog extends DialogFragment implements View.OnClickListener {
    public static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    MainActivity ma = new MainActivity();
    Schedule schedule = new Schedule();

    int anfdmavy=0; //db가져오는 flag!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

    String id = LoginActivity.getEmail();
    ArrayList<String> date = new ArrayList<>();
//    FragmentCalender fragmentCalender = new FragmentCalender();

    EditText mName;
    EditText mDeparture;
    EditText mArrival;
    NameInputListener listener;

    Date date_t = new Date();
    String string_t = dateFormat.format(date_t);
    String term;

    //HashMap<String, Object> childUpdates = null;

    public static AddPlanDialog newInstance(NameInputListener listener) {
        AddPlanDialog fragment = new AddPlanDialog();
        fragment.listener = listener;
        return fragment;
    }

    public interface NameInputListener {
        void onNameInputComplete(String name);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.MyDialog);
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.pop_add_plan, null);
        builder.setView(view);

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();

        mName = (EditText) view.findViewById(R.id.edit_tripName);
        mDeparture = (EditText) view.findViewById(R.id.departure);
        mArrival = (EditText) view.findViewById(R.id.arrival);
        mDeparture.setText(string_t);
        mArrival.setText(string_t);

        getDate();
        Button confirm = (Button) view.findViewById(R.id.confirm);
        confirm.setOnClickListener(this);

        Button cancel = (Button) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                String get_tripName = mName.getText().toString();
                String get_departure = mDeparture.getText().toString();
                String get_arrival = mArrival.getText().toString();

                if (DateCheck(get_departure, get_arrival) == 1) {
                    errorDialog("출발일을 다시 입력해주세요 :(");
                } else if (DateCheck(get_departure, get_arrival) == 2) {
                    errorDialog("도착일을 다시 입력해주세요 :(");
                } else if (DateCheck(get_departure, get_arrival) == 3) {
                    errorDialog("날짜를 다시 입력해주세요 :(");
                } else if (DateCheck(get_departure, get_arrival) == 4) { //추가됨%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                    errorDialog("선택하신 날짜에 이미 일정이 있습니다. :(");
                } else if (get_tripName.equals("") || get_tripName == null) {
                    errorDialog("일정 이름을 입력해주세요 :(");
                } else {
                    String id = LoginActivity.getEmail();
                    term = tripTerm(get_departure, get_arrival);

                    HashMap newPlan = new HashMap<>();
                    newPlan.put("travel_name", get_tripName);
                    newPlan.put("departure", get_departure);
                    newPlan.put("arrival", get_arrival);
                    newPlan.put("days", term);
                    newPlan.put("budget", "null");
                    newPlan.put("use_money", "null");
                    newPlan.put("detail", "null");

                    if (id == null) {
                        Log.i("아이디 없음", "tlqkf");
                    } else {
                        Log.i("됐나?", id);
                        String key = ma.mdatabase.child("사용자").child(id).child("일정").push().getKey();
                        newPlan.put("key", key);
                        ma.mdatabase.child(key).setValue(newPlan); //여기수정됨!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!1
                        Log.e("-------------------->", "일정 생성");

//                        childUpdates = new HashMap<>();

                        schedule.setKey(key);
                        schedule.setTravel_name(get_tripName);
                        schedule.setDeparture(get_departure);
                        schedule.setArrival(get_arrival);
                        schedule.setDays(term);
                        schedule.setBudget("null");
                        schedule.setUse_money("null");
                        schedule.setDetail("null");
                    }
                    dismiss();
                    getFragmentManager().beginTransaction().replace(R.id.main_layout, new FragmentCategory()).commitAllowingStateLoss();
                    Toast.makeText(getContext(), "여행지를 추가해보세요!", Toast.LENGTH_SHORT).show();
                }

                Log.i("출발----------->", get_departure);
                Log.i("도착----------->", get_arrival);

                break;

            case R.id.cancel:
                Log.e("-------------------->", "일정 만들기 취소");
                dismiss();
                break;
        }
    }

    public int DateCheck(String departure, String arrival) {
        try {
            Date today = dateFormat.parse(string_t);
            Date to = dateFormat.parse(departure);   //출발일
            Date from = dateFormat.parse(arrival);   //도착일

            int result = today.compareTo(to);
            int result2 = to.compareTo(from);

            String tokento[] = String.valueOf(to).split(" ");
            String tokenfrom[] = String.valueOf(from).split(" ");

            String tt = tokento[2];
            String tf = tokenfrom[2];

            if (result == 1) {
                return 1;
            } else if (result2 == 1) {
                return 2;
            } else if (result == 1 && result2 == 1) {
                return 3;
            } else if (checkDate(tt, tf) == 4){ //추가됨%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
                return 4;
            } else return 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 3;
    }

    public String tripTerm(String departure, String arrival) {
        try {
            Date to = dateFormat.parse(departure);   //출발일
            Date from = dateFormat.parse(arrival);   //도착일

            long diffSec = (from.getTime() - to.getTime());
            term = String.valueOf(diffSec/(24*60*60*1000) + 1);
            Log.i("여행 일 수--------------->", term);

            return term;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "-";
    }

    public void getDate(){
        ma.mdatabase = FirebaseDatabase.getInstance().getReference("사용자").child(id).child("일정");    //사용자에게 접근!!!

        ma.mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                int depart = String.valueOf(dataSnapshot.getValue()).indexOf("departure");
                String subdepart = String.valueOf(dataSnapshot.getValue()).substring(depart);

                int arrival = String.valueOf(dataSnapshot.getValue()).indexOf("arrival");
                String subarrival = String.valueOf(dataSnapshot.getValue()).substring(arrival);

                String arraydepart[] = subdepart.split(",");
                String arrayarrival[] = subarrival.split(",");

                String a[] = arraydepart[0].split("=");
                String b[] = arrayarrival[0].split("=");

                String travelday = a[1]+"~"+b[1];
                Log.e("andi", travelday);
                date.add(travelday);
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

            }
        });
        anfdmavy=1;
    }

    public int checkDate(String to, String from) { //사용자 디비에 일정에 날짜 가져와서 입력된 날짜랑 체크함 논리적 오류면 4리턴
        for(int i = 0; i < date.size(); i++){
            String p[] = date.get(i).split("~");
            String q[] = p[0].split("-");
            String r[] = p[1].split("-");

            int toDay = Integer.parseInt(to);
            int fromDay = Integer.parseInt(from);

            int departMonth = Integer.parseInt(q[1]);
            int departDay = Integer.parseInt(q[2]);
            int arrivalMonth = Integer.parseInt(r[1]);
            int arrivalDay = Integer.parseInt(r[2]);

            Log.d("tnstjeofh", toDay+","+fromDay+","+departMonth+","+departDay+","+arrivalMonth+","+arrivalDay);

            if(departMonth == arrivalMonth) {
                if (departDay <= toDay && arrivalDay >= toDay)
                    return 4;
                else if (departDay <= fromDay && arrivalDay >= fromDay)
                    return 4;
                else if(departDay <= toDay && arrivalDay >= fromDay)
                    return 4;
                else
                    return 0;
            }
            else {
                if ((departDay < toDay && 31 > toDay) || (0 < toDay && arrivalDay < toDay))
                    return 4;
                else if ((departDay < fromDay && 31 > toDay) || (0 < fromDay && arrivalDay < toDay))
                    return 4;
                else
                    return 0;
            }
        }
        return 0;
    }

    public void errorDialog(String text) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
        builder.setTitle(text); // AlertDialog.builder를 통해 Title text를 입력
        builder.setPositiveButton("알겠습니다!", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
        dialog.show(); // dialog를 화면에 뿌려 줌
    }
}
