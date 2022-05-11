package com.example.ddubuk;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FragmentAccount extends Fragment implements View.OnClickListener {
    MainActivity ma = new MainActivity();

    ViewGroup viewGroup;
    NoticeFragment noticeFragment;
    Bookmark bookmark;
    Checklist checklist;

    static Button idView;
    static LinearLayout layout_idView;
    static TextView view_userId;
    ImageView randomIcon;
    TextView tasteView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_account, container, false);

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();

        noticeFragment = new NoticeFragment();
        bookmark = new Bookmark();
        checklist = new Checklist();

        layout_idView = (LinearLayout) viewGroup.findViewById(R.id.layout_idView);
        view_userId = (TextView) viewGroup.findViewById(R.id.view_userId);
        randomIcon = (ImageView) viewGroup.findViewById(R.id.randomIcon);
        tasteView = (TextView) viewGroup.findViewById(R.id.tasteView);

        Button notice = (Button) viewGroup.findViewById(R.id.notice);
        notice.setOnClickListener(this);

        Button bookmark = (Button) viewGroup.findViewById(R.id.btn_addPlan);
        bookmark.setOnClickListener(this);

        Button checklist = (Button) viewGroup.findViewById(R.id.checklist);
        checklist.setOnClickListener(this);

        idView = (Button) viewGroup.findViewById(R.id.idView);
        idView.setOnClickListener(this);

        if (LoginActivity.getEmail() != null) {
            idView.setVisibility(View.GONE);
            view_userId.setText(LoginActivity.getEmail() + "님");
            layout_idView.setVisibility(View.VISIBLE);

            switch (setRandomIcon()) {
                case 1:
                    randomIcon.setImageResource(R.drawable.random1);
                    break;
                case 2:
                    randomIcon.setImageResource(R.drawable.random2);
                    break;
                case 3:
                    randomIcon.setImageResource(R.drawable.random3);
                    break;
                case 4:
                    randomIcon.setImageResource(R.drawable.random4);
                    break;
                case 5:
                    randomIcon.setImageResource(R.drawable.random5);
                    break;
            }
        }
        setTasteView();

        return viewGroup;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_addPlan:
                if (LoginActivity.getEmail() != null) {
                    getFragmentManager().beginTransaction().replace(R.id.main_layout, bookmark).commitAllowingStateLoss();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                    builder.setTitle("로그인이 필요합니다!\n로그인 화면으로 이동할까요?"); // AlertDialog.builder를 통해 Title text를 입력
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent login = new Intent(getActivity(), LoginActivity.class);
                            login.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                            startActivity(login);
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                    dialog.show(); // dialog를 화면에 뿌려 줌
                }
                break;
            case R.id.idView:
                Intent login = new Intent(getActivity(), LoginActivity.class);
                login.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                startActivity(login);
                break;
            case R.id.notice:
                getFragmentManager().beginTransaction().replace(R.id.main_layout, noticeFragment).commitAllowingStateLoss();
                break;
            case R.id.set:

                break;
            case R.id.checklist:
                getFragmentManager().beginTransaction().replace(R.id.main_layout, checklist).commitAllowingStateLoss();
                break;
        }
    }

    public int setRandomIcon() {
        int num = (int) (Math.random() * 6) + 1;
        Log.e("<random>", String.valueOf(num));

        return num;
    }

    public void setTasteView() {
        if (LoginActivity.getEmail() != null) {
            ma.mdatabase = FirebaseDatabase.getInstance().getReference("사용자").child(LoginActivity.getEmail()).child("설정").child("취향");
            ma.mdatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String hash = String.valueOf(dataSnapshot.getValue());
                    String result = hash.substring(hash.lastIndexOf("n") + 5, hash.length() - 1);
                    Log.e("<취향 태그요>", result);
                    tasteView.setText(result);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
