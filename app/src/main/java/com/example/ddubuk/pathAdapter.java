package com.example.ddubuk;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;

public class pathAdapter extends RecyclerView.Adapter<pathAdapter.pathViewHolder> {
    MainActivity ma = new MainActivity();

    int selectColor=0;
    private ArrayList<setDays> arrayList;
    NaverMap naverMap = FragmentCategory.naverMap;
    public pathAdapter(ArrayList<setDays> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public pathAdapter.pathViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_daybutton, parent, false);
        pathAdapter.pathViewHolder holder = new pathAdapter.pathViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final pathAdapter.pathViewHolder holder, final int position) {
        holder.day.setText(arrayList.get(position).getDay());



        holder.day.setOnClickListener(v -> {
            fp.clearMarker();
            Log.e("3", "1515");
            fp.clearOverlay();
            ArrayList<ArrayList<daySchedule>> daysc1 = FragmentCategory.daysc;
            ArrayList<daySchedule> detail1 = daysc1.get(Integer.parseInt(arrayList.get(position).getDay())-1);
            Log.e("asdf", String.valueOf(position));

            FragmentCategory.transAdapter = new transportationAdapter(detail1);
            FragmentCategory.transAdapter.notifyDataSetChanged();
            FragmentCategory.transportationRecyclerView.setAdapter(FragmentCategory.transAdapter);

            upDateCamera(detail1.get(0).getLatitude(), detail1.get(0).getLongitude());

            for(int i = 0; i < detail1.size()-1; i++){
                String LongitudeStart = String.valueOf(detail1.get(i).getLongitude());
                String LatitudeStart = String.valueOf(detail1.get(i).getLatitude());
                String start = LongitudeStart+","+LatitudeStart;

                String LongitudeGoal = String.valueOf(detail1.get(i+1).getLongitude());
                String LatitudeGoal = String.valueOf(detail1.get(i+1).getLatitude());
                String goal = LongitudeGoal+","+LatitudeGoal;

                selectColor = Integer.parseInt(arrayList.get(position).getDay())-1;
                Log.e("1", "1515");
                fp f = new fp(start, goal, "", selectColor, true, detail1.get(i));
                Log.e("2", "1515");
                Log.e("4", "1515");
                f.run();

                Log.e("5", "1515");

                if(i != 0){
                    LatLng latLng3 = new LatLng(Double.parseDouble(LatitudeStart), Double.parseDouble(LongitudeStart));
                    Marker marker3 = new Marker();
                    marker3.setPosition(latLng3);
                    marker3.setIcon(MarkerIcons.PINK);
                    marker3.setWidth(100);
                    marker3.setHeight(120);
                    marker3.setMap(naverMap);
                    fp.markerO.add(marker3);
                    Log.e("markerO", String.valueOf(fp.markerO.size()));
                }
            }
            LatLng latLng1 = new LatLng(detail1.get(0).getLatitude(), detail1.get(0).getLongitude());
            Marker marker1 = new Marker();
            marker1.setPosition(latLng1);
            marker1.setIcon(MarkerIcons.RED);
            marker1.setWidth(100);
            marker1.setHeight(120);
            marker1.setMap(naverMap);
            fp.markerO.add(marker1);

            LatLng latLng2 = new LatLng(detail1.get(detail1.size()-1).getLatitude(), detail1.get(detail1.size()-1).getLongitude());
            Marker marker2 = new Marker();
            marker2.setPosition(latLng2);
            marker2.setIcon(MarkerIcons.BLUE);
            marker2.setWidth(100);
            marker2.setHeight(120);
            marker2.setMap(naverMap);
            fp.markerO.add(marker2);

            Log.e("6", "1515");
        });
    }

    public void upDateCamera(double Longitude, double Latitude){
        LatLng Position = new LatLng(Longitude, Latitude);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(Position);
        naverMap.moveCamera(cameraUpdate);
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size():0);
    }

    public class pathViewHolder extends RecyclerView.ViewHolder {
        Button day;

        public pathViewHolder(@NonNull View itemView) {
            super(itemView);
            day = itemView.findViewById(R.id.dayButton100);
        }
    }
}