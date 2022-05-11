package com.example.ddubuk;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class BudgetDialog extends DialogFragment implements View.OnClickListener {
    MainActivity ma = new MainActivity();
    Schedule schedule = new Schedule();
    NameInputListener listener;

    EditText editBudget;

    public static BudgetDialog newInstance(BudgetDialog.NameInputListener listener) {
        BudgetDialog fragment = new BudgetDialog();
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
        View view = inflater.inflate(R.layout.pop_budget, null);
        builder.setView(view);

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();

        editBudget = (EditText) view.findViewById(R.id.editBudget);

        Button accept = (Button) view.findViewById(R.id.btn_accept);
        accept.setOnClickListener(this);

        return builder.create();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_accept:
                String get_budget = editBudget.getText().toString();

                String id = LoginActivity.getEmail();

                HashMap setBudget = new HashMap<>();
                setBudget.put("budget", get_budget);

                if (id == null) {
                    Log.i("아이디 없음", "tlqkf");
                } else {
                    Log.i("됐나?", id);

                    ma.mdatabase.child("사용자").child(id).child("일정").child(schedule.getKey()).updateChildren(setBudget);
                    schedule.setBudget(get_budget);

                    Budget.viewTotal.setText(get_budget + "원");
                    Log.e("-------------------->", "예산 설정");
                }
                dismiss();

                break;
        }
    }
}
