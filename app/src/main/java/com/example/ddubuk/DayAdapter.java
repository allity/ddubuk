package com.example.ddubuk;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DayAdapter extends BaseAdapter {
    LayoutInflater inflater = null;
    private ArrayList<DayData> arrayList = null;
    private int nListCnt = 0;

    public DayAdapter(ArrayList<DayData> _oData)
    {
        arrayList = _oData;
        nListCnt = arrayList.size();
    }

    @Override
    public int getCount()
    {
        return nListCnt;
    }

    @Override
    public Object getItem(int position)
    {
        return null;
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            final Context context = parent.getContext();
            if (inflater == null)
            {
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            }
            convertView = inflater.inflate(R.layout.day_item, parent, false);
        }

        TextView day = (TextView) convertView.findViewById(R.id.view_day);

        day.setText(arrayList.get(position).day);
        return convertView;
    }
}
