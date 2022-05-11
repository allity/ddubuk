package com.example.ddubuk;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import static android.content.Context.MODE_PRIVATE;

public class Checklist extends Fragment {
    ViewGroup viewGroup;

    private CheckBox checkBox;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;
    private CheckBox checkBox6;
    private CheckBox checkBox7;
    private CheckBox checkBox8;
    private CheckBox checkBox9;
    private CheckBox checkBox10;
    private CheckBox checkBox11;
    private CheckBox checkBox12;
    private CheckBox checkBox13;
    private CheckBox checkBox14;
    private CheckBox checkBox15;
    private CheckBox checkBox16;
    private CheckBox checkBox17;
    private CheckBox checkBox18;
    private CheckBox checkBox19;
    private CheckBox checkBox20;
    private CheckBox checkBox21;
    private CheckBox checkBox22;
    private CheckBox checkBox23;
    private CheckBox checkBox24;
    private CheckBox checkBox25;
    private EditText memo;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.checklist, container, false);

        checkBox = (CheckBox)viewGroup.findViewById(R.id.cb);
        checkBox2 = (CheckBox)viewGroup.findViewById(R.id.cb2);
        checkBox3 = (CheckBox)viewGroup.findViewById(R.id.cb3);
        checkBox4 = (CheckBox)viewGroup.findViewById(R.id.cb4);
        checkBox5 = (CheckBox)viewGroup.findViewById(R.id.cb5);
        checkBox6 = (CheckBox)viewGroup.findViewById(R.id.cb6);
        checkBox7 = (CheckBox)viewGroup.findViewById(R.id.cb7);
        checkBox8 = (CheckBox)viewGroup.findViewById(R.id.cb8);
        checkBox9 = (CheckBox)viewGroup.findViewById(R.id.cb9);
        checkBox10 = (CheckBox)viewGroup.findViewById(R.id.cb10);
        checkBox11 = (CheckBox)viewGroup.findViewById(R.id.cb11);
        checkBox12 = (CheckBox)viewGroup.findViewById(R.id.cb12);
        checkBox13 = (CheckBox)viewGroup.findViewById(R.id.cb13);
        checkBox14 = (CheckBox)viewGroup.findViewById(R.id.cb14);
        checkBox15 = (CheckBox)viewGroup.findViewById(R.id.cb15);
        checkBox16 = (CheckBox)viewGroup.findViewById(R.id.cb16);
        checkBox17 = (CheckBox)viewGroup.findViewById(R.id.cb17);
        checkBox18 = (CheckBox)viewGroup.findViewById(R.id.cb18);
        checkBox19 = (CheckBox)viewGroup.findViewById(R.id.cb19);
        checkBox20 = (CheckBox)viewGroup.findViewById(R.id.cb20);
        checkBox21 = (CheckBox)viewGroup.findViewById(R.id.cb21);
        checkBox22 = (CheckBox)viewGroup.findViewById(R.id.cb22);
        checkBox23 = (CheckBox)viewGroup.findViewById(R.id.cb23);
        checkBox24 = (CheckBox)viewGroup.findViewById(R.id.cb24);
        checkBox25 = (CheckBox)viewGroup.findViewById(R.id.cb25);
        memo = (EditText)viewGroup.findViewById(R.id.memo);

        Log.d("", "onCreate: 저장된 값을 불러오기 위해 preferences 파일 찾음");
        SharedPreferences preferences = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);

        Log.d("", "onCreate: 저장된 값이 있는지 확인");
        Boolean ck1 = preferences.getBoolean("checkBox",false);
        Boolean ck2 = preferences.getBoolean("checkBox2",false);
        Boolean ck3 = preferences.getBoolean("checkBox3",false);
        Boolean ck4 = preferences.getBoolean("checkBox4",false);
        Boolean ck5 = preferences.getBoolean("checkBox5",false);
        Boolean ck6 = preferences.getBoolean("checkBox6",false);
        Boolean ck7 = preferences.getBoolean("checkBox7",false);
        Boolean ck8 = preferences.getBoolean("checkBox8",false);
        Boolean ck9 = preferences.getBoolean("checkBox9",false);
        Boolean ck10 = preferences.getBoolean("checkBox10",false);
        Boolean ck11 = preferences.getBoolean("checkBox11",false);
        Boolean ck12 = preferences.getBoolean("checkBox12",false);
        Boolean ck13 = preferences.getBoolean("checkBox13",false);
        Boolean ck14 = preferences.getBoolean("checkBox14",false);
        Boolean ck15 = preferences.getBoolean("checkBox15",false);
        Boolean ck16 = preferences.getBoolean("checkBox16",false);
        Boolean ck17 = preferences.getBoolean("checkBox17",false);
        Boolean ck18 = preferences.getBoolean("checkBox18",false);
        Boolean ck19 = preferences.getBoolean("checkBox19",false);
        Boolean ck20 = preferences.getBoolean("checkBox20",false);
        Boolean ck21 = preferences.getBoolean("checkBox21",false);
        Boolean ck22 = preferences.getBoolean("checkBox22",false);
        Boolean ck23 = preferences.getBoolean("checkBox23",false);
        Boolean ck24 = preferences.getBoolean("checkBox24",false);
        Boolean ck25 = preferences.getBoolean("checkBox25",false);
        String text = preferences.getString("memo","memo");

        Log.d("", "onCreate: 값 띄우기");
        memo.setText(text);
        checkBox.setChecked(ck1);
        checkBox2.setChecked(ck2);
        checkBox3.setChecked(ck3);
        checkBox4.setChecked(ck4);
        checkBox5.setChecked(ck5);
        checkBox6.setChecked(ck6);
        checkBox7.setChecked(ck7);
        checkBox8.setChecked(ck8);
        checkBox9.setChecked(ck9);
        checkBox10.setChecked(ck10);
        checkBox11.setChecked(ck11);
        checkBox12.setChecked(ck12);
        checkBox13.setChecked(ck13);
        checkBox14.setChecked(ck14);
        checkBox15.setChecked(ck15);
        checkBox16.setChecked(ck16);
        checkBox17.setChecked(ck17);
        checkBox18.setChecked(ck18);
        checkBox19.setChecked(ck19);
        checkBox20.setChecked(ck20);
        checkBox21.setChecked(ck21);
        checkBox22.setChecked(ck22);
        checkBox23.setChecked(ck23);
        checkBox24.setChecked(ck24);
        checkBox25.setChecked(ck25);

        return viewGroup;
    }

    @Override
    public void onStop(){
        //어플리케이션이 화면에서 사라질 때 UI상태를 저장
        Log.d("", "onStop: activity종료 전 저장하기 시작");
        super.onStop();
        SharedPreferences sf = getActivity().getSharedPreferences("preferences", MODE_PRIVATE);

        Log.d("", "onStop: editor를 이용하여 값 저장");
        SharedPreferences.Editor editor = sf.edit();

        Log.d("", "onStop: 저장중");
        editor.putBoolean("checkBox",checkBox.isChecked());
        editor.putBoolean("checkBox2",checkBox2.isChecked());
        editor.putBoolean("checkBox3",checkBox3.isChecked());
        editor.putBoolean("checkBox4",checkBox4.isChecked());
        editor.putBoolean("checkBox5",checkBox5.isChecked());
        editor.putBoolean("checkBox6",checkBox6.isChecked());
        editor.putBoolean("checkBox7",checkBox7.isChecked());
        editor.putBoolean("checkBox8",checkBox8.isChecked());
        editor.putBoolean("checkBox9",checkBox9.isChecked());
        editor.putBoolean("checkBox10",checkBox10.isChecked());
        editor.putBoolean("checkBox11",checkBox11.isChecked());
        editor.putBoolean("checkBox12",checkBox12.isChecked());
        editor.putBoolean("checkBox13",checkBox13.isChecked());
        editor.putBoolean("checkBox14",checkBox14.isChecked());
        editor.putBoolean("checkBox15",checkBox15.isChecked());
        editor.putBoolean("checkBox16",checkBox16.isChecked());
        editor.putBoolean("checkBox17",checkBox17.isChecked());
        editor.putBoolean("checkBox18",checkBox18.isChecked());
        editor.putBoolean("checkBox19",checkBox19.isChecked());
        editor.putBoolean("checkBox20",checkBox20.isChecked());
        editor.putBoolean("checkBox21",checkBox21.isChecked());
        editor.putBoolean("checkBox22",checkBox22.isChecked());
        editor.putBoolean("checkBox23",checkBox23.isChecked());
        editor.putBoolean("checkBox24",checkBox24.isChecked());
        editor.putBoolean("checkBox25",checkBox25.isChecked());
        editor.putString("memo",memo.getText().toString());


        editor.commit();
        Log.d("", "onStop: 저장 완료");
    }
}