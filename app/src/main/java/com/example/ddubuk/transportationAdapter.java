package com.example.ddubuk;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.util.MarkerIcons;

import java.util.ArrayList;

public class transportationAdapter extends RecyclerView.Adapter<transportationAdapter.transportationViewHolder> {
    MainActivity ma = new MainActivity();

    NaverMap naverMap = FragmentCategory.naverMap;

    private ArrayList<daySchedule> arrayList;

    public transportationAdapter(ArrayList<daySchedule> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public transportationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_transportation, parent, false);
        transportationViewHolder holder = new transportationViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final transportationAdapter.transportationViewHolder holder, final int position) {
        holder.transportation.setText("도보");

        holder.transportation.setOnClickListener(v -> {
            if(holder.transportation.getText().equals("도보")){
                holder.transportation.setText("자동차");

                String Latistart = String.valueOf(arrayList.get(position).getLatitude());
                String Longistart = String.valueOf(arrayList.get(position).getLongitude());
                String start = Longistart+","+Latistart;

                String Latigoal = String.valueOf(arrayList.get(position+1).getLatitude());
                String Longigoal = String.valueOf(arrayList.get(position+1).getLongitude());
                String goal = Longigoal+","+Latigoal;

                fp f = new fp(start, goal, "", 10, false, arrayList.get(position));
                arrayList.get(position).setTransportation("자동차");
                f.clearOverlay();
                f.run();
                //자동차 길찾기 오버레이

                LatLng latLng1 = new LatLng(Double.parseDouble(Latistart), Double.parseDouble(Longistart));
                Marker marker1 = new Marker();
                marker1.setPosition(latLng1);
                marker1.setIcon(MarkerIcons.RED);
                marker1.setWidth(100);
                marker1.setHeight(120);
                marker1.setMap(naverMap);
                fp.markerO.add(marker1);

                LatLng latLng2 = new LatLng(Double.parseDouble(Latigoal), Double.parseDouble(Longigoal));
                Marker marker2 = new Marker();
                marker2.setPosition(latLng2);
                marker2.setIcon(MarkerIcons.BLUE);
                marker2.setWidth(100);
                marker2.setHeight(120);
                marker2.setMap(naverMap);
                fp.markerO.add(marker2);
            }
            else if(holder.transportation.getText().equals("자동차")){
                holder.transportation.setText("자전거");
                String Latistart = String.valueOf(arrayList.get(position).getLatitude());
                String Longistart = String.valueOf(arrayList.get(position).getLongitude());
                String start = Longistart+","+Latistart;

                String Latigoal = String.valueOf(arrayList.get(position+1).getLatitude());
                String Longigoal = String.valueOf(arrayList.get(position+1).getLongitude());
                String goal = Longigoal+","+Latigoal;

                fp f = new fp(start, goal, "", 5, false, arrayList.get(position));
                arrayList.get(position).setTransportation("자전거");
                f.clearOverlay();
                f.run();
                //자동차 길찾기 오버레이

                LatLng latLng1 = new LatLng(Double.parseDouble(Latistart), Double.parseDouble(Longistart));
                Marker marker1 = new Marker();
                marker1.setPosition(latLng1);
                marker1.setIcon(MarkerIcons.RED);
                marker1.setWidth(100);
                marker1.setHeight(120);
                marker1.setMap(naverMap);
                fp.markerO.add(marker1);

                LatLng latLng2 = new LatLng(Double.parseDouble(Latigoal), Double.parseDouble(Longigoal));
                Marker marker2 = new Marker();
                marker2.setPosition(latLng2);
                marker2.setIcon(MarkerIcons.BLUE);
                marker2.setWidth(100);
                marker2.setHeight(120);
                marker2.setMap(naverMap);
                fp.markerO.add(marker2);
            }
            else {
                holder.transportation.setText("도보");
                String Latistart = String.valueOf(arrayList.get(position).getLatitude());
                String Longistart = String.valueOf(arrayList.get(position).getLongitude());
                String start = Longistart+","+Latistart;

                String Latigoal = String.valueOf(arrayList.get(position+1).getLatitude());
                String Longigoal = String.valueOf(arrayList.get(position+1).getLongitude());
                String goal = Longigoal+","+Latigoal;

                fp f = new fp(start, goal, "", 0, true, arrayList.get(position));
                arrayList.get(position).setTransportation("도보");
                f.clearOverlay(); //clearMarker() 삭제 @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
                f.run();

                LatLng latLng1 = new LatLng(Double.parseDouble(Latistart), Double.parseDouble(Longistart));
                Marker marker1 = new Marker();
                marker1.setPosition(latLng1);
                marker1.setIcon(MarkerIcons.PINK);
                marker1.setWidth(100);
                marker1.setHeight(120);
                marker1.setMap(naverMap);
                fp.markerO.add(marker1);

                LatLng latLng2 = new LatLng(Double.parseDouble(Latigoal), Double.parseDouble(Longigoal));
                Marker marker2 = new Marker();
                marker2.setPosition(latLng2);
                marker2.setIcon(MarkerIcons.GREEN);
                marker2.setWidth(100);
                marker2.setHeight(120);
                marker2.setMap(naverMap);
                fp.markerO.add(marker2);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size()-1:0);
    }

    public class transportationViewHolder extends RecyclerView.ViewHolder {
        Button transportation;

        public transportationViewHolder(@NonNull View itemView) {
            super(itemView);
            transportation = itemView.findViewById(R.id.transportation);
        }
    }
}