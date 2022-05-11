package com.example.ddubuk;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class Research2Activity extends AppCompatActivity implements View.OnClickListener {
    MainActivity ma = new MainActivity();

    private Spinner spinner_hours;
    private Spinner spinner_min;
    private TextView tv_hours;
    private TextView tv_min;

    private int hour;
    private int min;
    private int time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.research2);

        spinner_hours = (Spinner) findViewById(R.id.select_hours);
        tv_hours = (TextView) findViewById(R.id.hourView);

        spinner_hours.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_hours.setText(parent.getItemAtPosition(position).toString());
                hour = Integer.parseInt(parent.getItemAtPosition(position).toString());
                Log.i("시간------------>", String.valueOf(hour));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinner_min = (Spinner) findViewById(R.id.select_min);
        tv_min = (TextView) findViewById(R.id.minView);

        spinner_min.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_min.setText(parent.getItemAtPosition(position).toString());
                min = Integer.parseInt(parent.getItemAtPosition(position).toString());
                Log.i("분------------>", String.valueOf(min));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button ok = (Button) findViewById(R.id.btn_ok);
        ok.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                setTime(hour, min);

                HashMap newUser = new HashMap<>();
                newUser.put("취향", ResearchActivity.getResearch());
                newUser.put("시간", getTime());

                ma.mdatabase.child("사용자").child(LoginActivity.getEmail()).child("설정").setValue(newUser);

                Toast.makeText(this, "감사합니다!", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    public int getTime() {
        return time;
    }

    public void setTime(int hour, int min) {
        this.time = hour*60 + min;
        Log.i("총 시간--------->", String.valueOf(time));
    }
}