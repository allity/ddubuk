package com.example.ddubuk;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.LocationOverlay;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.PathOverlay;
import com.naver.maps.map.util.MarkerIcons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class FragmentViewPath extends Fragment implements OnMapReadyCallback {
    Schedule schedule = new Schedule();

    ViewGroup viewGroup;

    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS  = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    static String pathpath;
    static String pathlatitude;
    static String pathlongitude;
    static String pathname;
    static String longitude2;
    static String latitude2;
    static String pathname2;
    String start = pathlatitude+","+pathlongitude;
    String goal = latitude2+","+longitude2;
    float pdist=1000;

    private Timer timer;
    String npath = "";

    TextView travelName;
    ImageButton exit;

    static ArrayList<DetailData> ds = new ArrayList<>();
    Vector<LatLng> markerPosition = new Vector<>();

    private static NaverMap naverMap2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.viewpath, container, false);

        travelName = (TextView) viewGroup.findViewById(R.id.viewName);
        travelName.setText(schedule.getTravel_name());

        exit = (ImageButton) viewGroup.findViewById(R.id.btn_back);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout, new DetailSchedule())
                        .addToBackStack(null)
                        .commit();
            }
        });

        FragmentManager fm = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.pathMap);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.pathMap, mapFragment).commit();
        } else {
            mapFragment = null;
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.pathMap, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        if (!checkLocationServicesStatus()) {

            showDialogForLocationServiceSetting();
        }else {

            checkRunTimePermission();
        }

        gpsTracker = new GpsTracker(getActivity());
        timer = new Timer();
        timer.schedule(new logtimer(), 0, 2000*10);

        return viewGroup;
    }

    class logtimer extends TimerTask {
        double latitude;
        double longitude;
        @Override
        public void run() {
            latitude = gpsTracker.getLatitude();
            longitude = gpsTracker.getLongitude();

            Log.e("latitude", String.valueOf(latitude));
            Log.e("longitude", String.valueOf(longitude));

            npath = npath+":"+latitude+","+longitude;
            Log.e("asdf", npath);
            timeout();
        }

        public void timeout(){
            String a[] = goal.split(",");

            Location locationA = new Location("point A");
            locationA.setLatitude(Double.parseDouble(a[0]));
            locationA.setLongitude(Double.parseDouble(a[1]));

            Location locationB = new Location("point B");
            locationB.setLatitude(latitude);
            locationB.setLongitude(longitude);

            float dist = locationA.distanceTo(locationB);

            if(dist<20) {
                if (timer != null) {
                    timer.cancel();
                    String[] path = npath.substring(1).split(":");
                    float totaldist = 0;
                    for (int j = 0; j < path.length - 1; j++) {
                        String b[] = path[j].split(",");
                        String c[] = path[j + 1].split(",");

                        locationA = new Location("point A");
                        locationA.setLatitude(Double.parseDouble(b[0]));
                        locationA.setLongitude(Double.parseDouble(b[1]));

                        locationB = new Location("point B");
                        locationB.setLatitude(Double.parseDouble(c[0]));
                        locationB.setLongitude(Double.parseDouble(c[1]));

                        dist = locationA.distanceTo(locationB);
                        totaldist += dist;
                    }
                    if(totaldist < pdist){
                        insertData(start, goal, npath, totaldist);
                    }
                }
            }
        }
        public void insertData(String start, String goal, String npath, float totaldist){
            String s = start.replace(".", "d");
            s = s.replace(",", "s");

            String g = goal.replace(".", "d");
            g = g.replace(",", "s");

            String sg = s+"c"+g;

            HashMap newPath = new HashMap<>();
            newPath.put(sg, npath);


            FirebaseDatabase f = FirebaseDatabase.getInstance();//
            DatabaseReference d = f.getReference();
            d.child("????????????").child("path").push().setValue(newPath);
        }

    }

    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap2 = naverMap;



        Log.e("<>", pathpath+","+pathname+","+pathlatitude+","+pathlongitude+"dksEMsi?");
        setCamera(Double.parseDouble(pathlatitude), Double.parseDouble(pathlongitude));
        setMark(Double.parseDouble(pathlatitude), Double.parseDouble(pathlongitude), pathname);
        setMark(Double.parseDouble(latitude2), Double.parseDouble(longitude2), pathname2);

        String start = pathlongitude+","+pathlatitude;
        String goal = longitude2+","+latitude2;

        new Thread(){
            public void run(){
                String car = Direction15(start, goal, "");

                Log.e("<><>", start+","+goal);
                Bundle bun = new Bundle();
                bun.putString("Naver_Html", car);
                bun.putString("start", start);
                bun.putString("goal", goal);
                Message msg = new Message();
                msg.setData(bun);
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void setCamera(double latitude, double longitude) {
        LatLng initialPosition = new LatLng(latitude, longitude);
        CameraUpdate cameraUpdate = CameraUpdate.scrollTo(initialPosition);
        naverMap2.moveCamera(cameraUpdate);
    }

    public void setMark(double latitude, double longitude, String name) {
        Marker marker = new Marker();
        LatLng latLng = new LatLng(latitude, longitude);
        marker.setPosition(latLng);
        marker.setIcon(MarkerIcons.GREEN);
        marker.setWidth(70);
        marker.setHeight(75);
        marker.setMap(naverMap2);
        marker.setCaptionText(name);
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
            if(responseCode==200) { // ?????? ??????
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {  // ?????? ??????
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

    @SuppressLint("HandlerLeak")
    private
    Handler handler = new Handler(){
        public void handleMessage(Message msg){
            Bundle bun = msg.getData();
            markerPosition = new Vector<>();
            String result = bun.getString("Naver_Html");
            String start = bun.getString("start");
            String goal = bun.getString("goal");

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

            while(true) {
                try {
                    LocationOverlay locationOverlay = naverMap2.getLocationOverlay();
                    PathOverlay Opath = new PathOverlay();
                    Opath.setCoords(markerPosition);
                    Opath.setWidth(20);
                    Opath.setColor(Color.YELLOW);
                    Opath.setMap(naverMap2);

                    Log.e("qoieurwiauopefjasmzl","wehufweouafweio");
                    break;
                } catch (IllegalArgumentException e) {
                    Log.e("29", "1515");
                    continue;
                }
            }
        }

    };

    @Override
    public void onRequestPermissionsResult(int permsRequestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grandResults) {

        if ( permsRequestCode == PERMISSIONS_REQUEST_CODE && grandResults.length == REQUIRED_PERMISSIONS.length) {

            // ?????? ????????? PERMISSIONS_REQUEST_CODE ??????, ????????? ????????? ???????????? ??????????????????

            boolean check_result = true;


            // ?????? ???????????? ??????????????? ???????????????.

            for (int result : grandResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    check_result = false;
                    break;
                }
            }


            if ( check_result ) {

                //?????? ?????? ????????? ??? ??????
                ;
            }
            else {
                // ????????? ???????????? ????????? ?????? ????????? ??? ?????? ????????? ??????????????? ?????? ???????????????.2 ?????? ????????? ????????????.

                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[1])) {

                    Toast.makeText(getActivity(), "???????????? ?????????????????????. ?????? ?????? ???????????? ???????????? ??????????????????.", Toast.LENGTH_LONG).show();
                    getActivity().finish();

                }else {

                    Toast.makeText(getActivity(), "???????????? ?????????????????????. ??????(??? ??????)?????? ???????????? ???????????? ?????????. ", Toast.LENGTH_LONG).show();

                }
            }

        }
    }

    void checkRunTimePermission(){

        //????????? ????????? ??????
        // 1. ?????? ???????????? ????????? ????????? ???????????????.
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION);


        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

            // 2. ?????? ???????????? ????????? ?????????
            // ( ??????????????? 6.0 ?????? ????????? ????????? ???????????? ???????????? ????????? ?????? ????????? ?????? ???????????????.)


            // 3.  ?????? ?????? ????????? ??? ??????



        } else {  //2. ????????? ????????? ????????? ?????? ????????? ????????? ????????? ???????????????. 2?????? ??????(3-1, 4-1)??? ????????????.

            // 3-1. ???????????? ????????? ????????? ??? ?????? ?????? ????????????
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), REQUIRED_PERMISSIONS[0])) {

                // 3-2. ????????? ???????????? ?????? ?????????????????? ???????????? ????????? ????????? ???????????? ????????? ????????????.
                Toast.makeText(getActivity(), "??? ?????? ??????????????? ?????? ?????? ????????? ???????????????.", Toast.LENGTH_LONG).show();
                // 3-3. ??????????????? ????????? ????????? ?????????. ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);


            } else {
                // 4-1. ???????????? ????????? ????????? ??? ?????? ?????? ???????????? ????????? ????????? ?????? ?????????.
                // ?????? ????????? onRequestPermissionResult?????? ???????????????.
                ActivityCompat.requestPermissions(getActivity(), REQUIRED_PERMISSIONS,
                        PERMISSIONS_REQUEST_CODE);
            }

        }

    }

    //??????????????? GPS ???????????? ?????? ????????????
    private void showDialogForLocationServiceSetting() {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("?????? ????????? ????????????");
        builder.setMessage("?????? ???????????? ???????????? ?????? ???????????? ???????????????.\n"
                + "?????? ????????? ???????????????????");
        builder.setCancelable(true);
        builder.setPositiveButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent callGPSSettingIntent
                        = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivityForResult(callGPSSettingIntent, GPS_ENABLE_REQUEST_CODE);
            }
        });
        builder.setNegativeButton("??????", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        builder.create().show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {

            case GPS_ENABLE_REQUEST_CODE:

                //???????????? GPS ?????? ???????????? ??????
                if (checkLocationServicesStatus()) {
                    if (checkLocationServicesStatus()) {

                        Log.d("@@@", "onActivityResult : GPS ????????? ?????????");
                        checkRunTimePermission();
                        return;
                    }
                }

                break;
        }
    }
    public boolean checkLocationServicesStatus() {
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    public class GpsTracker extends Service implements LocationListener {

        private final Context mContext;
        Location location;
        double latitude;
        double longitude;

        private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
        private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1;
        protected LocationManager locationManager;


        public GpsTracker(Context context) {
            this.mContext = context;
            getLocation();
        }


        public Location getLocation() {
            try {
                locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

                boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if (!isGPSEnabled && !isNetworkEnabled) {

                } else {

                    int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION);
                    int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION);


                    if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED &&
                            hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {

                        ;
                    } else
                        return null;


                    if (isNetworkEnabled) {


                        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                        if (locationManager != null)
                        {
                            location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                            if (location != null)
                            {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }


                    if (isGPSEnabled)
                    {
                        if (location == null)
                        {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATES, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                            if (locationManager != null)
                            {
                                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                                if (location != null)
                                {
                                    latitude = location.getLatitude();
                                    longitude = location.getLongitude();
                                }
                            }
                        }
                    }
                }
            }
            catch (Exception e)
            {
                Log.d("@@@", ""+e.toString());
            }

            return location;
        }

        public double getLatitude()
        {
            if(location != null)
            {
                latitude = location.getLatitude();
            }

            return latitude;
        }

        public double getLongitude()
        {
            if(location != null)
            {
                longitude = location.getLongitude();
            }

            return longitude;
        }

        @Override
        public void onLocationChanged(Location location)
        {
        }

        @Override
        public void onProviderDisabled(String provider)
        {
        }

        @Override
        public void onProviderEnabled(String provider)
        {
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras)
        {
        }

        @Override
        public IBinder onBind(Intent arg0)
        {
            return null;
        }


        public void stopUsingGPS()
        {
            if(locationManager != null)
            {
                locationManager.removeUpdates(GpsTracker.this);
            }
        }


    }
}