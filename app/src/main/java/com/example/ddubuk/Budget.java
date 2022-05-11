package com.example.ddubuk;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class Budget extends Fragment implements View.OnClickListener {
    MainActivity ma = new MainActivity();
    Schedule schedule = new Schedule();

    ArrayList<String> items;
    ArrayAdapter<String> adapter;

    private Spinner spinner_use;
    private EditText UseAmount;
    private TextView view;
    private TextView viewUse;
    private TextView viewLeft;
    private ListView listViewUse;
    static TextView viewTotal;

    ImageButton delete;

    private String use;     //어디에 썼는지 분류

    ViewGroup viewGroup;
    String email;
    String total;      //초기설정

    private String[] uselist;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.budget, container, false);
        ma.mdatabase = FirebaseDatabase.getInstance().getReference();
        email = LoginActivity.getEmail();

        view = (TextView) viewGroup.findViewById(R.id.viewName);
        view.setText(schedule.getTravel_name());

        total = schedule.getBudget();

        if (schedule.getBudget().equals("null")) {
            BudgetDialog dialog = BudgetDialog.newInstance(new BudgetDialog.NameInputListener() {
                @Override
                public void onNameInputComplete(String name) {
                    if (name != null) {
                        //name is Texted EditText
                    }
                }
            });
            dialog.show(getFragmentManager(), "addDialog");
        }

        total = schedule.getBudget();
        viewTotal = (TextView) viewGroup.findViewById(R.id.viewTotal);
        viewTotal.setText(total + "원");

        viewUse = (TextView) viewGroup.findViewById(R.id.viewUseM);
        viewLeft = (TextView) viewGroup.findViewById(R.id.viewLeftM);

        if (!schedule.getUse_money().equals("null")) {
            setViewUse();
        }

        spinner_use = (Spinner) viewGroup.findViewById(R.id.spinnerUse);
        spinner_use.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                use = parent.getItemAtPosition(position).toString();
                Log.i("------------>", use);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        UseAmount = (EditText) viewGroup.findViewById(R.id.useAmount);

        Button change = (Button) viewGroup.findViewById(R.id.btn_change);
        change.setOnClickListener(this);

        Button add = (Button) viewGroup.findViewById(R.id.btn_add);
        add.setOnClickListener(this);

        delete = (ImageButton) viewGroup.findViewById(R.id.btn_delete);
        delete.setOnClickListener(this);

        ImageButton btn_back = (ImageButton) viewGroup.findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        return viewGroup;
    }

    public void setViewUse() {
        uselist = schedule.getUse_money().split(",");
        int result = Integer.parseInt(schedule.getBudget());
        int useTotal = 0;
        items = new ArrayList<String>();

        for (int i = 0; i < uselist.length; i++) {
            listViewUse = (ListView) viewGroup.findViewById(R.id.listViewUse);

            if (!uselist[i].equals("null")) {
                items.add(uselist[i]);
                int u = Integer.parseInt(uselist[i].substring(uselist[i].lastIndexOf("]") + 1));

                if ((result - u) < 0)
                    Toast.makeText(getContext(), "예산이 초과되었습니다!", Toast.LENGTH_SHORT).show();

                result = result - u;
                useTotal = useTotal + u;
            }

            adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_single_choice, items);

            listViewUse.setAdapter(adapter);
            listViewUse.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

            Log.e("<uselist>", uselist[i]);
        }

        viewLeft.setText(result + "원");
        viewUse.setText(useTotal + "원");

        if (items.isEmpty())
            delete.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_change:
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                builder.setTitle("예산을 " + UseAmount.getText() + "원으로 수정할까요?"); // AlertDialog.builder를 통해 Title text를 입력
                builder.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        HashMap setBudget = new HashMap<>();
                        setBudget.put("budget", UseAmount.getText().toString());

                        ma.mdatabase.child("사용자").child(email).child("일정").child(schedule.getKey()).updateChildren(setBudget);
                        schedule.setBudget(String.valueOf(UseAmount.getText()));
                        viewTotal.setText(String.valueOf(UseAmount.getText()));
                        setViewUse();
                    }
                });
                builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "취소되었습니다!", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                dialog.show(); // dialog를 화면에 뿌려 줌
                break;
            case R.id.btn_add:
                Log.e("사용금액---------->", use + UseAmount.getText());

                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                builder2.setTitle("[" + use + "] " + UseAmount.getText() + "원이 맞나요?"); // AlertDialog.builder를 통해 Title text를 입력
                builder2.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String n = schedule.getUse_money() + ", [" + use + "]" + UseAmount.getText().toString();
                        HashMap useMoney = new HashMap<>();
                        useMoney.put("use_money", n);

                        ma.mdatabase.child("사용자").child(email).child("일정").child(schedule.getKey()).updateChildren(useMoney);

                        Toast.makeText(getContext(), "추가되었습니다!", Toast.LENGTH_SHORT).show();
                        schedule.setUse_money(n);

                        setViewUse();
                    }
                });
                builder2.setNegativeButton("아니요", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getContext(), "다시 입력해주세요!", Toast.LENGTH_SHORT).show();
                    }
                });
                AlertDialog dialog2 = builder2.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                dialog2.show(); // dialog를 화면에 뿌려 줌
                break;
            case R.id.btn_delete:
                final int[] pos = new int[1];
                listViewUse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Log.e("listView checkPosition", items.get(position));
                        pos[0] = position;
                    }
                });

                AlertDialog.Builder builder3 = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                builder3.setTitle(items.get(pos[0]) + "을 삭제할까요?"); // AlertDialog.builder를 통해 Title text를 입력
                builder3.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        items.remove(pos[0]);

                        String n = "null";

                        for (int i = 0; i < items.size(); i++) {
                            n = n + "," + items.get(i);
                        }

                        schedule.setUse_money(n);

                        HashMap useMoney = new HashMap<>();
                        useMoney.put("use_money", n);

                        ma.mdatabase.child("사용자").child(email).child("일정").child(schedule.getKey()).updateChildren(useMoney);

                        Toast.makeText(getContext(), "삭제되었습니다!", Toast.LENGTH_SHORT).show();

                        setViewUse();
                    }
                });
                builder3.setNegativeButton("아니요", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                AlertDialog dialog3 = builder3.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                dialog3.show(); // dialog를 화면에 뿌려 줌
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
}
