package com.example.ddubuk;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class FragmentHome extends Fragment implements View.OnClickListener {
    MainActivity mainActivity = new MainActivity();
    FragmentCategory fragmentCategory = new FragmentCategory();
    ViewGroup viewGroup;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        Button seoul = (Button) viewGroup.findViewById(R.id.seoul);
        seoul.setOnClickListener(this);

        Button jeonnam = (Button) viewGroup.findViewById(R.id.jeonnam);
        jeonnam.setOnClickListener(this);

        Button gangwon = (Button) viewGroup.findViewById(R.id.gangwon);
        gangwon.setOnClickListener(this);

        Button kyonggi = (Button) viewGroup.findViewById(R.id.kyonggi);
        kyonggi.setOnClickListener(this);

        Button incheon = (Button) viewGroup.findViewById(R.id.incheon);
        incheon.setOnClickListener(this);

        Button chungbuk = (Button) viewGroup.findViewById(R.id.chungbuk);
        chungbuk.setOnClickListener(this);

        Button ulleungdo = (Button) viewGroup.findViewById(R.id.ulleungdo);
        ulleungdo.setOnClickListener(this);

        Button gyeongbuk = (Button) viewGroup.findViewById(R.id.gyeongbuk);
        gyeongbuk.setOnClickListener(this);

        Button daegu = (Button) viewGroup.findViewById(R.id.daegu);
        daegu.setOnClickListener(this);

        Button jeonbuk = (Button) viewGroup.findViewById(R.id.jeonbuk);
        jeonbuk.setOnClickListener(this);

        Button gyeongnam = (Button) viewGroup.findViewById(R.id.gyeongnam);
        gyeongnam.setOnClickListener(this);

        Button jeju = (Button) viewGroup.findViewById(R.id.jeju);
        jeju.setOnClickListener(this);

        Button busan = (Button) viewGroup.findViewById(R.id.busan);
        busan.setOnClickListener(this);

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.seoul:
                FragmentCategory.setCity("서울");
                getFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentCategory).commitAllowingStateLoss();
                break;
            case R.id.jeonnam:
                FragmentCategory.setCity("전남");
                getFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentCategory).commitAllowingStateLoss();
                break;
            case R.id.gangwon:
                FragmentCategory.setCity("강릉");
                getFragmentManager().beginTransaction().replace(R.id.main_layout, fragmentCategory).commitAllowingStateLoss();
                break;
            case R.id.gyeongbuk:
            case R.id.busan:
            case R.id.jeju:
            case R.id.gyeongnam:
            case R.id.jeonbuk:
            case R.id.daegu:
            case R.id.ulleungdo:
            case R.id.chungbuk:
            case R.id.chungnam:
            case R.id.kyonggi:
                Toast.makeText(getActivity(), "준비중입니다!", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}

