package com.example.ddubuk;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class ResearchActivity extends AppCompatActivity implements View.OnClickListener {
    MainActivity ma = new MainActivity();
    private static String research = "";
    boolean t = false;
    boolean n = false;
    boolean c = false;
    CheckBox traditional;
    CheckBox nature;
    CheckBox city;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.research);

        traditional = (CheckBox) findViewById(R.id.btn_traditional);
        nature = (CheckBox) findViewById(R.id.btn_nature);
        city = (CheckBox) findViewById(R.id.btn_city);

        traditional.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(traditional.isChecked() == true) {
                    Log.i("t-------->", "선택됨");
                    t = true;
                } else {
                    Log.i("t-------->", "취소됨");
                    t = false;
                }
            }
        });

        nature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(nature.isChecked() == true) {
                    Log.i("n-------->", "선택됨");
                    n = true;
                }
                else n = false;
            }
        });

        city.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(city.isChecked() == true) {
                    Log.i("c-------->", "선택됨");
                    c = true;
                }
                else c = false;
            }
        });

        Button next = (Button) findViewById(R.id.btn_next);
        next.setOnClickListener(this);

        Button skip = (Button) findViewById(R.id.btn_skip);
        skip.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_traditional:

                break;
            case R.id.btn_next:
                if (t == true)
                    setResearch(research + "#전통,");
                if(n == true)
                    setResearch(research + "#자연,");
                if(c == true)
                    setResearch(research + "#도시,");

                Log.i("취향-------->", research);

                Intent intent = new Intent(this, Research2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.btn_skip:
                HashMap newUser = new HashMap<>();
                newUser.put("취향", "-");
                newUser.put("시간", "-");

                ma.mdatabase.child("사용자").child(LoginActivity.getEmail()).child("설정").setValue(newUser);
                finish();
                break;
        }
    }

    public static String getResearch() {
        return research;
    }

    public void setResearch(String research) {
        this.research = research;
    }
}
