package com.example.ddubuk;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class NoticeFragment extends Fragment {
    ViewGroup viewGroup;

//    public static TestFragment newInstance() {
//        return new TestFragment();
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.notice, container, false);
        Log.e("------->", "Notice Fragment");
        return viewGroup;
    }
}
