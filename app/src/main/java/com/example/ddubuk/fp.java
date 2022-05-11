package com.example.ddubuk;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;

public class fp {
    private String start;
    private String goal;
    private String option;
    private int selectColor;
    private boolean walk;
    daySchedule schedule;

    private NaverMap naverMap = FragmentCategory.naverMap;
    static Vector<PathOverlay> pathO =  new Vector<>();
    static Vector<Marker> markerO = new Vector<>();
    Vector<LatLng> markerPosition;

    int anfdmavy = 0;
    static int smrlavy = 1;

    private int color[] = {Color.YELLOW, Color.BLUE, Color.CYAN, Color.DKGRAY, Color.GRAY, Color.GREEN, Color.LTGRAY, Color.MAGENTA, Color.RED, Color.TRANSPARENT, Color.WHITE, Color.BLACK};

    private ArrayList<dPath> dPathArrayList;
    FirebaseDatabase database = FirebaseDatabase.getInstance();

    public fp(String start, String goal, String option, int selectColor, boolean walk, daySchedule schedule){
        this.start = start;
        this.goal = goal;
        this.option = option;
        this.selectColor = selectColor;
        this.walk = walk;
        this.schedule = schedule;
        Log.e("10", "1515");
    }

    public void run(){
        Log.e("11", "1515");
        clearOverlay();//clearMarker() 삭제 %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
        Log.e("12", "1515");
        findPath();
    }

    public static void clearOverlay(){
        while(true) {
            if (smrlavy == 0){
                Log.e("overlay", String.valueOf(pathO.size()));
                Log.e("13", "1515");
                continue;
            }
            else {
                for (int i = 0; i < pathO.size(); i++) {
                    pathO.get(i).setMap(null);
                    Log.e("14", "1515");
                }
                pathO.clear();
                break;
            }
        }
    }

    public static void clearMarker(){
        if(!markerO.isEmpty())
            for(int i = 0; i< markerO.size(); i++){
                markerO.get(i).setMap(null);
                Log.e("15", "1515");
            }
        markerO.clear();
    }

    public void findPath(){
        smrlavy = 0;
        new Thread(){
            public void run(){
                String car = Direction15(start, goal, "");
                if(walk)
                    car = compare(start, goal, car);

                Bundle bun = new Bundle();
                bun.putString("Naver_Html", car);
                bun.putString("start", start);
                bun.putString("goal", goal);
                Message msg = new Message();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
        smrlavy = 1;
    }

    public String Direction15(String start, String goal, String option){
        String clientId = "50pvl16r1i";
        String clientSecret = "xgrMqOUJyZSS18blID5C2MGAmHQKAZDrqydQpItS";
        try{
            String q = "?start="+start+"&goal="+goal;//+"&option="+option;
            String apiURL = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving"+q;
            URL url = new URL(apiURL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("X-NCP-APIGW-API-KEY-ID" , clientId);
            con.setRequestProperty("X-NCP-APIGW-API-KEY" , clientSecret);
            int responseCode = con.getResponseCode();
            BufferedReader br;
            if(responseCode==200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }

    public String compare(String start, String goal, String result){
        Log.e("result", result);
        int Sindex = result.indexOf("distance") + 10;
        int Eindex = result.indexOf("duration") - 2;
        String distance = result.substring(Sindex, Eindex);

        if(Float.parseFloat(distance) > 5000){
            return result;
        }
        Log.e("21", "1515");
        float dddd = Float.parseFloat(distance);

        dPathArrayList = new ArrayList<>();
        DatabaseReference databaseReference = database.getReference("도보경로").child("path");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String ss) {
                dPathArrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Log.e("22", "1515");
                    String se = snapshot.getKey();
                    se = se.replace("d", ".");
                    se = se.replace("s", ",");
                    String s = se.substring(0, se.indexOf("c"));
                    String e = se.substring(se.indexOf("c") + 1);

                    dPath path = new dPath();
                    path.setPath((String) snapshot.getValue());
                    path.setS(s);
                    path.setE(e);

                    String[] dbs = s.split(",");
                    String[] apis = start.split(",");

                    String[] dbg = e.split(",");
                    String[] apig = goal.split(",");

                    Location locationA = new Location("point A");
                    locationA.setLatitude(Double.parseDouble(dbs[0]));
                    locationA.setLongitude(Double.parseDouble(dbs[1]));

                    Location locationB = new Location("point B");
                    locationB.setLatitude(Double.parseDouble(apis[1]));
                    locationB.setLongitude(Double.parseDouble(apis[0]));

                    float dist = locationA.distanceTo(locationB);

                    Location locationC = new Location("point C");
                    locationC.setLatitude(Double.parseDouble(dbg[0]));
                    locationC.setLongitude(Double.parseDouble(dbg[1]));

                    Location locationD = new Location("point D");
                    locationD.setLatitude(Double.parseDouble(apig[1]));
                    locationD.setLongitude(Double.parseDouble(apig[0]));

                    float distD = locationC.distanceTo(locationD);

                    if(dist < 500 && distD < 500){
                        dPathArrayList.add(path);
                    }
                    else
                        Log.e("dhodksemfdjrk?", "dho...");
                }
                anfdmavy = 1;
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("dPath", (String) dataSnapshot.getValue());
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Log.d("dPath", (String) dataSnapshot.getValue());
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("dPath", dataSnapshot.getKey());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("dPath", String.valueOf(databaseError.toException()));
            }
        });
        while (true) {
            Log.e("23", "1515");
            if (anfdmavy == 1) {
                Log.e("24", "1515");
                int min = -1;
                float mindist = dddd;
                String plusStart = "";
                String rps = "";
                String plusGoal = "";
                String rpg = "";
                if (dPathArrayList.size() != 0) {
                    for (int i = 0; i < dPathArrayList.size(); i++) {
                        String p = dPathArrayList.get(i).getPath();
                        String[] path = p.split(":");
                        float totaldist = 0;
                        float msdist = 99999;
                        float mgdist = 99999;
                        int ms = 0;
                        int mg = 0;
                        rps = "";
                        rpg="";

                        for (int j = 0; j < path.length - 1; j++) {
                            String a[] = path[j].split(",");
                            String c[] = path[j + 1].split(",");

                            Location locationA = new Location("point A");
                            locationA.setLatitude(Double.parseDouble(a[0]));
                            locationA.setLongitude(Double.parseDouble(a[1]));

                            Location locationB = new Location("point B");
                            locationB.setLatitude(Double.parseDouble(c[0]));
                            locationB.setLongitude(Double.parseDouble(c[1]));

                            float dist = locationA.distanceTo(locationB);
                            totaldist += dist;

                            if(j < 15 || j > path.length-15) {
                                String apis[] = start.split(",");
                                Location locationS = new Location("point S");
                                locationS.setLatitude(Double.parseDouble(apis[1]));
                                locationS.setLongitude(Double.parseDouble(apis[0]));

                                String apig[] = goal.split(",");
                                Location locationG = new Location("point G");
                                locationG.setLatitude(Double.parseDouble(apig[1]));
                                locationG.setLongitude(Double.parseDouble(apig[0]));

                                Location locationN = new Location("point ns");
                                locationN.setLatitude(Double.parseDouble(a[0]));
                                locationN.setLongitude(Double.parseDouble(a[1]));

                                float sdist = locationS.distanceTo(locationN);
                                if (sdist < msdist) {
                                    msdist = sdist;
                                    ms = j;
                                    Log.e("?????", String.valueOf(ms));
                                }

                                float gdist = locationG.distanceTo(locationN);
                                if (gdist < mgdist) {
                                    mgdist = gdist;
                                    mg = j;
                                    Log.e("?????", String.valueOf(mg));
                                }
                            }

                        }
                        String reverseS[] = path[ms].split(",");
                        String directionS = Direction15(start, reverseS[1]+","+reverseS[0], "");
                        Log.e("anjwl?", directionS);
                        if(directionS.contains("\"code\":1")) {
                            Log.e("dksdhsi?", "dhkdiwl!");
                            totaldist += 0;
                        }
                        else if (directionS.contains("\"code\":0")) {
                            Sindex = directionS.indexOf("distance") + 10;
                            Eindex = directionS.indexOf("duration") - 2;
                            distance = result.substring(Sindex, Eindex);
                            float sp0 = Float.parseFloat(distance);
                            if(sp0 > 500){
                                String Rs[] = start.split(",");
                                rps = Rs[1] +","+ Rs[0] +":";
                                Log.e("dyry", rps);

                                Location locationA = new Location("point A");
                                locationA.setLatitude(Double.parseDouble(reverseS[0]));
                                locationA.setLongitude(Double.parseDouble(reverseS[1]));

                                Location locationB = new Location("point B");
                                locationB.setLatitude(Double.parseDouble(Rs[1]));
                                locationB.setLongitude(Double.parseDouble(Rs[0]));

                                totaldist += locationA.distanceTo(locationB);
                            }
                            else {
                                Sindex = directionS.indexOf("path") + 8;
                                Eindex = directionS.indexOf("guide") - 4;
                                if (Eindex == 0) {
                                    Eindex = result.indexOf("section") - 4;
                                }
                                plusStart = directionS.substring(Sindex, Eindex);
                                String ps[] = plusStart.split("],\\[");
                                for (int pp = 0; pp < ps.length; pp++) {
                                    String pss[] = ps[pp].split(",");
                                    for(int ppp=0; ppp<2; ppp++){
                                        rps = rps + pss[1] +","+ pss[0] +":";
                                    }
                                    Log.e("dyry2", rps);
                                }
                                totaldist += sp0;
                            }
                        }

                        String reverseG[] = path[mg].split(",");
                        String directionG = Direction15(reverseG[1]+","+reverseG[0], goal,"");
                        Log.e("dkfRK??", directionG);
                        if(directionG.contains("\"code\":1")) {
                            Log.e("dkfRK??", "dhkdiwl!");
                            totaldist += 0;
                        }
                        else if (directionS.contains("\"code\":0")) {
                            Sindex = directionG.indexOf("distance") + 10;
                            Eindex = directionG.indexOf("duration") - 2;
                            distance = directionG.substring(Sindex, Eindex);
                            float peg = Float.parseFloat(distance);
                            totaldist += peg;

                            if(peg > 500){
                                String Rg[] = goal.split(",");
                                rpg = ":"+Rg[1] +","+ Rg[0];
                                Log.e("dyry3", rpg);

                                Location locationA = new Location("point A");
                                locationA.setLatitude(Double.parseDouble(reverseG[0]));
                                locationA.setLongitude(Double.parseDouble(reverseG[1]));

                                Location locationB = new Location("point B");
                                locationB.setLatitude(Double.parseDouble(Rg[1]));
                                locationB.setLongitude(Double.parseDouble(Rg[0]));

                                totaldist += locationA.distanceTo(locationB);
                            }
                            else {
                                Sindex = directionG.indexOf("path") + 8;
                                Eindex = directionG.indexOf("guide") - 4;
                                if (Eindex == 0) {
                                    Eindex = result.indexOf("section") - 4;
                                }
                                plusGoal = directionG.substring(Sindex, Eindex);
                                String pg[] = plusGoal.split("],\\[");
                                for (int pp = 0; pp < pg.length; pp++) {
                                    String pgg[] = pg[pp].split(",");
                                    for(int ppp=0; ppp<2; ppp++){
                                        rpg = rpg +":" + pgg[1] +","+ pgg[0];
                                    }
                                    Log.e("dyry4", rpg);
                                }
                            }
                        }

                        if (mindist > totaldist) {
                            mindist = totaldist;
                            min = i;
                        }
                    }
                    if (min == -1) {
                        Log.e("durl?", "durl?");
                        return result;
                    }
                    else {
                        if(!rps.equals("") && !rpg.equals("")) {
                            Log.e("여긴가1", rps + dPathArrayList.get(min).getPath() + rpg);
                            return rps + dPathArrayList.get(min).getPath() + rpg;
                        }
                        else if(rps.equals("") && !rpg.equals("")) {
                            Log.e("여긴가2", dPathArrayList.get(min).getPath()+rpg);
                            return dPathArrayList.get(min).getPath() + rpg;
                        }
                        else if(!rps.equals("") && rpg.equals("")) {
                            Log.e("여긴가3", rps + dPathArrayList.get(min).getPath());
                            return rps + dPathArrayList.get(min).getPath();
                        }
                        else {
                            Log.e("여긴가4", dPathArrayList.get(min).getPath());
                            return dPathArrayList.get(min).getPath();
                        }
                    }
                } else {
                    Log.e("result", result);
                    return result;
                }
            }
            else {
                continue;
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Bundle bun = msg.getData();
            markerPosition = new Vector<>();
            String result = bun.getString("Naver_Html");
            String start = bun.getString("start");
            String goal = bun.getString("goal");
            Log.e("25", "1515");

            if(result.contains("path")) {
                int Sindex = 0;
                Sindex = result.indexOf("path") + 8;
                int Eindex = 0;
                Eindex = result.indexOf("section") - 4;
                if (Eindex == 0) {
                    Eindex = result.indexOf("guide") - 4;
                }
                String path = result.substring(Sindex, Eindex);
                String p[];
                p = path.split("],\\[");
                int flag=0;
                for (int i = 0; i < p.length; i++) {
                    String pp[] = p[i].split(",");

                    markerPosition.add(new LatLng(Double.parseDouble(pp[1]), Double.parseDouble(pp[0])));
                }
            }
            else{
                String p[];
                Log.e("zzz", result);
                p = result.split(":");
                int flag=0;
                for(int i = 0; i < p.length; i++){
                    Log.e("26", "1515");
                    String pp[] = p[i].split(",");

                    if(i < 15 || i > p.length-15) {
                        String apis[] = start.split(",");
                        Location locationS = new Location("point S");
                        locationS.setLatitude(Double.parseDouble(apis[1]));
                        locationS.setLongitude(Double.parseDouble(apis[0]));

                        String apig[] = goal.split(",");
                        Location locationG = new Location("point G");
                        locationG.setLatitude(Double.parseDouble(apig[1]));
                        locationG.setLongitude(Double.parseDouble(apig[0]));

                        Location locationN = new Location("point ns");
                        locationN.setLatitude(Double.parseDouble(pp[0]));
                        locationN.setLongitude(Double.parseDouble(pp[1]));

                        float sdist = locationS.distanceTo(locationN);
                        Log.e("??????", String.valueOf(sdist));
                        if (sdist < 5)
                            flag = 1;

                        float gdist = locationG.distanceTo(locationN);
                        if (gdist < 5)
                            break;

                        if (flag == 1) {
                            markerPosition.add(new LatLng(Double.parseDouble(pp[0]), Double.parseDouble(pp[1])));
                            Log.e("asdfsadfsadf", String.valueOf(markerPosition.size()));
                        }
                    }
                    else {
                        markerPosition.add(new LatLng(Double.parseDouble(pp[0]), Double.parseDouble(pp[1])));
                    }
                }
            }
            while(true) {
                try {
                    LocationOverlay locationOverlay = naverMap.getLocationOverlay();
                    PathOverlay Opath = new PathOverlay();
                    Opath.setCoords(markerPosition);
                    Opath.setWidth(20);
                    Opath.setColor(color[selectColor]);
                    int t = pathO.size();
                    pathO.add(Opath);
                    while(true){
                        if(t == pathO.size()) {
                            pathO.add(Opath);
                            continue;
                        }
                        else
                            break;
                    }
                    Opath.setMap(naverMap);

                    Log.e("qoieurwiauopefjasmzl","wehufweouafweio");
                    break;
                } catch (IllegalArgumentException e) {
                    Log.e("29", "1515");
                    continue;
                }
            }
        }

    };
}