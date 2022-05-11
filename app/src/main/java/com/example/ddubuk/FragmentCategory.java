package com.example.ddubuk;

import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.MarkerIcons;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class FragmentCategory extends Fragment implements OnMapReadyCallback, View.OnClickListener {
    ViewGroup viewGroup;
    MainActivity ma = new MainActivity();
    Schedule schedule = new Schedule();

    FirebaseDatabase database = FirebaseDatabase.getInstance(); //해시를 위한 디비
    private String id;
    ArrayList<Custom> randmarkname = new ArrayList<>();

    String adname;
    String adadress;
    double adlatitude;
    double adlongitude;

    static String getday = "1";

    Vector<LatLng> markerPosition;
    Vector<Marker> bedM = new Vector<>();
    Vector<Marker> landM = new Vector<>();
    Vector<Marker> medicalM = new Vector<>();
    Vector<Marker> bycicleM = new Vector<>();
    Vector<Marker> korea_foodM = new Vector<>();
    Vector<Marker> japan_foodM = new Vector<>();
    Vector<Marker> boonsick_foodM = new Vector<>();
    Vector<Marker> fast_foodM = new Vector<>();
    Vector<Marker> coffee_foodM = new Vector<>();
    Vector<Marker> chicken_foodM = new Vector<>();
    Vector<Marker> top10M = new Vector<>();

    Vector<Marker> searchM = new Vector<>();

    static NaverMap naverMap;

    SearchView searchView;

    Button tour;
    Button medical;
    Button bed;
    Button food;
    Button korea;
    Button japan;
    Button boonsick;
    Button chicken;
    Button coffee;
    Button fast;
    Button finish;
    Button realFinish;
    Button bycicle;
    Button top10;
    Button ct_bookmark;     //카테고리 중에 북마크
    ImageButton bookmark;   //일정 담는거
    ImageButton open;
    ImageButton addPlanClose;
    ImageButton addPlanClose2;
    ImageButton btn_clear;

    String keyword;
    String userHash;

    int f_count, b_count, m_count, t_count, k_count, j_count, boon_count, c_count, ch_count, by_count, top_count = 0;

    LinearLayout layout_2;
    LinearLayout layout_3;
    LinearLayout layout_4;
    LinearLayout layout_5;

    static String city = "서울";
    TextView textView_n;
    TextView textView_a;

    TextView selectTravelName;  //장소 담을 일정
    TextView selectTravelName2;

    daySchedule daySchedule;
    protected static ArrayList<ArrayList<daySchedule>> daysc = new ArrayList<>();
    protected static RecyclerView.Adapter detailSchAdapter;
    protected static RecyclerView detailSchRecyclerView;

    ArrayList<setDays> sdsd = new ArrayList<>();

    private RecyclerView.Adapter dayAdapter;
    private RecyclerView.Adapter pathAdapter;
    protected static RecyclerView.Adapter transAdapter;

    RecyclerView pathRecyclerView;
    static RecyclerView transportationRecyclerView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.fragment_category, container, false);

        id = LoginActivity.getEmail();

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();

        Log.e("<map>", "onCreate");

        searchView = viewGroup.findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //검색 버튼 눌렸을 때
                keyword = query;

                if (keyword.length() < 2)
                    Toast.makeText(getContext(), "검색어를 두 글자 이상 입력해주세요!", Toast.LENGTH_SHORT).show();
                else {
                    btn_clear.setVisibility(View.VISIBLE);
//                    keyword = "남산";
                    OverlayImage a = MarkerIcons.RED;
                    MapSearch(naverMap, "jsons/landmark.json", keyword, MarkerIcons.YELLOW, true, searchM);
                    MapSearch(naverMap, "jsons/bed.json", keyword, MarkerIcons.BLUE, true, searchM);
                    MapSearch(naverMap, "jsons/food_boonsick.json", keyword, a, true, searchM);
                    MapSearch(naverMap, "jsons/food_chicken.json", keyword, a, true, searchM);
                    MapSearch(naverMap, "jsons/food_coffee.json", keyword, a, true, searchM);
                    MapSearch(naverMap, "jsons/food_fastfood.json", keyword, a, true, searchM);
                    MapSearch(naverMap, "jsons/food_japan.json", keyword, a, true, searchM);
                    MapSearch(naverMap, "jsons/food_korea.json", keyword, a, true, searchM);
                    MapSearch(naverMap, "jsons/medical.json", keyword, MarkerIcons.PINK, true, searchM);

                    Toast.makeText(getContext(), "검색 완료! 지도를 움직여 결과를 확인해주세요 :)", Toast.LENGTH_SHORT).show();
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //검색창에 글자를 썼을 때
                Log.i("<keyword>", newText);
                keyword = newText;

                return false;
            }
        });

        this.InitializeView();
        this.SetListener();

        FragmentManager fm = getFragmentManager();
        MapFragment mapFragment = (MapFragment) fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        } else {
            mapFragment = null;
            mapFragment = MapFragment.newInstance();
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        layout_2 = viewGroup.findViewById(R.id.layout_2);
        layout_3 = viewGroup.findViewById(R.id.layout_3);
        layout_4 = viewGroup.findViewById(R.id.planlayout);
        layout_5 = viewGroup.findViewById(R.id.pathDay);

        Button create = (Button) viewGroup.findViewById(R.id.create);
        daysc.clear();
        sdsd.clear();

        RecyclerView dayRecyclerView = viewGroup.findViewById(R.id.daybutton);
        dayRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        dayRecyclerView.setLayoutManager(layoutManager);

        pathRecyclerView = viewGroup.findViewById(R.id.pathdayButton);
        pathRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        pathRecyclerView.setLayoutManager(layoutManager2);

        detailSchRecyclerView = viewGroup.findViewById(R.id.detailSchedule);
        dayRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager1 = new LinearLayoutManager(getActivity());
        detailSchRecyclerView.setLayoutManager(layoutManager1);

        transportationRecyclerView = viewGroup.findViewById(R.id.transportationRecyclerView);
        transportationRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager3 = new LinearLayoutManager(getActivity());
        transportationRecyclerView.setLayoutManager(layoutManager3);

        selectTravelName = viewGroup.findViewById(R.id.selectTravelName);
        selectTravelName2 = viewGroup.findViewById(R.id.selectTravelName2);

        String days = Schedule.getDays();
        for (int i = 0; i < Integer.parseInt(days); i++) {
            setDays sd = new setDays();
            sd.setDay(String.valueOf(i + 1));
            sdsd.add(sd);
            ArrayList<daySchedule> dayScheduleArrayList = new ArrayList<>();
            daysc.add(dayScheduleArrayList);
        }
        dayAdapter = new setDaysAdapter(sdsd, getActivity());
        dayRecyclerView.setAdapter(dayAdapter);

        exec(); //디비 가져오기~

        if (LoginActivity.getEmail() != null)
            setUserHash();

        return viewGroup;
    }

    public void InitializeView() {
        food = viewGroup.findViewById(R.id.food);
        tour = viewGroup.findViewById(R.id.tour);
        medical = viewGroup.findViewById(R.id.medical);
        bed = viewGroup.findViewById(R.id.bed);
        korea = viewGroup.findViewById(R.id.korea);
        japan = viewGroup.findViewById(R.id.japan);
        boonsick = viewGroup.findViewById(R.id.boonsick);
        chicken = viewGroup.findViewById(R.id.chicken);
        fast = viewGroup.findViewById(R.id.fast);
        coffee = viewGroup.findViewById(R.id.coffee);
        bookmark = viewGroup.findViewById(R.id.btn_addPlan);
        finish = viewGroup.findViewById(R.id.finish);
        open = viewGroup.findViewById(R.id.open);
        bycicle = viewGroup.findViewById(R.id.bycicle);
        top10 = viewGroup.findViewById(R.id.top10);
        realFinish = viewGroup.findViewById(R.id.realFinish);
        ct_bookmark = viewGroup.findViewById(R.id.ct_bookmark);

        addPlanClose = viewGroup.findViewById(R.id.btn_addPlanClose);
        addPlanClose2 = viewGroup.findViewById(R.id.btn_addPlanClose2);
        btn_clear = viewGroup.findViewById(R.id.btn_clear);

        Log.e("<map>", "InitializeView");
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        //카메라 설정
        setCamera(naverMap, city);

        Log.e("<map>", "onMapReady");
    }

    @Override
    public void onClick(View v) {
        //버튼의 id를 가져옴
        String j;
        com.naver.maps.map.overlay.OverlayImage a;
        switch (v.getId()) {
            case R.id.food:
                layout_2.setVisibility(layout_2.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                break;
            case R.id.bed:
                j = "jsons/bed.json";
                a = OverlayImage.fromResource(R.drawable.sleep);
                if (b_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, bedM);
                    bed.setPressed(true);
                } else {
                    buttonMarker(naverMap, j, a, false, bedM);
                }
                b_count++;
                break;
            case R.id.tour:
                j = "jsons/landmark.json";
                a = OverlayImage.fromResource(R.drawable.landmark);
                if (t_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, landM);
                } else {
                    buttonMarker(naverMap, j, a, false, landM);
                }
                t_count++;
                break;
            case R.id.medical:
                j = "jsons/medical.json";
                a = OverlayImage.fromResource(R.drawable.medical);
                if (m_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, medicalM);
                } else {
                    buttonMarker(naverMap, j, a, false, medicalM);
                }
                m_count++;
                break;
            case R.id.bycicle:
                Log.e("j", "f");
                j = "jsons/bycicle.json";
                a = OverlayImage.fromResource(R.drawable.bycicle);
                if (by_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, bycicleM);
                } else {
                    buttonMarker(naverMap, j, a, false, bycicleM);
                }
                by_count++;
                break;
            case R.id.korea:
                j = "jsons/food_korea.json";
                a = OverlayImage.fromResource(R.drawable.rice);
                if (k_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, korea_foodM);
                } else {
                    buttonMarker(naverMap, j, a, false, korea_foodM);
                }
                k_count++;
                break;
            case R.id.japan:
                Log.e("dd", "dd");
                j = "jsons/food_japan.json";
                a = OverlayImage.fromResource(R.drawable.sushi);
                if (j_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, japan_foodM);

                } else {
                    buttonMarker(naverMap, j, a, false, japan_foodM);
                }
                j_count++;
                break;
            case R.id.chicken:
                Log.e("dd", "dd");
                j = "jsons/food_chicken.json";
                a = OverlayImage.fromResource(R.drawable.chicken);
                if (ch_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, chicken_foodM);

                } else {
                    buttonMarker(naverMap, j, a, false, chicken_foodM);
                }
                ch_count++;
                break;
            case R.id.boonsick:
                Log.e("dd", "dd");
                j = "jsons/food_boonsick.json";
                a = OverlayImage.fromResource(R.drawable.snack);
                if (boon_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, boonsick_foodM);

                } else {
                    buttonMarker(naverMap, j, a, false, boonsick_foodM);
                }
                boon_count++;
                break;
            case R.id.fast:
                Log.e("dd", "dd");
                j = "jsons/food_fastfood.json";
                a = OverlayImage.fromResource(R.drawable.fastfood);
                if (f_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, fast_foodM);

                } else {
                    buttonMarker(naverMap, j, a, false, fast_foodM);
                }
                f_count++;
                break;
            case R.id.coffee:
                Log.e("dd", "dd");
                j = "jsons/food_coffee.json";
                a = OverlayImage.fromResource(R.drawable.coffee);
                if (c_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, coffee_foodM);

                } else {
                    buttonMarker(naverMap, j, a, false, coffee_foodM);
                    Log.e("dnn", "nn");
                }
                c_count++;
                break;
            case R.id.btn_addPlan:
                if (schedule.getTravel_name() == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                    builder.setTitle("일정을 먼저 만들어야합니다 :( \n바로 일정을 만까요?"); // AlertDialog.builder를 통해 Title text를 입력
                    builder.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AddPlanDialog addPlanDialog = AddPlanDialog.newInstance(new AddPlanDialog.NameInputListener() {
                                @Override
                                public void onNameInputComplete(String name) {
                                    if (name != null) {
                                        //name is Texted EditText
                                    }
                                }
                            });
                            addPlanDialog.show(getFragmentManager(), "addDialog");
                        }
                    });
                    builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getContext(), "일정은 캘린더에서 언제든지 만들 수 있습니다!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                    dialog.show(); // dialog를 화면에 뿌려 줌
                } else {
                    layout_3.setVisibility(View.GONE);
                    daySchedule = new daySchedule();
                    daySchedule.setName(adname);
                    daySchedule.setLatitude(adlatitude);
                    daySchedule.setLongitude(adlongitude);
                    daySchedule.setAdress(adadress);
                    daysc.get(Integer.parseInt(getday) - 1).add(daySchedule);
                    detailSchAdapter = new detailSchAdapter(daysc.get(Integer.parseInt(getday) - 1));//, getActivity());
                    detailSchAdapter.notifyDataSetChanged();
                    detailSchRecyclerView.setAdapter(detailSchAdapter);

                    Toast.makeText(getContext(), adname + "을 담았습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.finish:
                layout_3.setVisibility(View.GONE);
                if (bedM.size() != 0) {
                    for (Marker m : bedM) {
                        m.setMap(null);
                    }
                    bedM.clear();
                }

                if (landM.size() != 0) {
                    for (Marker m : landM) {
                        m.setMap(null);
                    }
                    landM.clear();
                }

                if (medicalM.size() != 0) {
                    for (Marker m : medicalM) {
                        m.setMap(null);
                    }
                    medicalM.clear();
                }

                if (korea_foodM.size() != 0) {
                    for (Marker m : korea_foodM) {
                        m.setMap(null);
                    }
                    korea_foodM.clear();
                }

                if (japan_foodM.size() != 0) {
                    for (Marker m : japan_foodM) {
                        m.setMap(null);
                    }
                    japan_foodM.clear();
                }

                if (boonsick_foodM.size() != 0) {
                    for (Marker m : boonsick_foodM) {
                        m.setMap(null);
                    }
                    boonsick_foodM.clear();
                }

                if (fast_foodM.size() != 0) {
                    for (Marker m : fast_foodM) {
                        m.setMap(null);
                    }
                    fast_foodM.clear();
                }

                if (coffee_foodM.size() != 0) {
                    for (Marker m : coffee_foodM) {
                        m.setMap(null);
                    }
                    coffee_foodM.clear();
                }

                if (chicken_foodM.size() != 0) {
                    for (Marker m : chicken_foodM) {
                        m.setMap(null);
                    }
                    chicken_foodM.clear();
                }

                layout_4.setVisibility(View.GONE);

                pathAdapter = new pathAdapter(sdsd);
                pathRecyclerView.setAdapter(pathAdapter);

                layout_5.setVisibility(View.VISIBLE);
                break;
            case R.id.open:
                if (LoginActivity.getEmail() == null)
                    Toast.makeText(getContext(), "로그인을 해주세요!", Toast.LENGTH_SHORT).show();
                else {
                    if (schedule.getTravel_name() == null) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()); // AlertDialog를 띄울 activity를 argument로 지정해야 한다.
                        builder.setTitle("일정을 먼저 만들어야합니다 :( \n바로 일정을 만까요?"); // AlertDialog.builder를 통해 Title text를 입력
                        builder.setPositiveButton("네", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AddPlanDialog addPlanDialog = AddPlanDialog.newInstance(new AddPlanDialog.NameInputListener() {
                                    @Override
                                    public void onNameInputComplete(String name) {
                                        if (name != null) {
                                            //name is Texted EditText
                                        }
                                    }
                                });
                                addPlanDialog.show(getFragmentManager(), "addDialog");
                            }
                        });
                        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() { // AlertDialog.Builder에 Positive Button을 생성
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "일정은 캘린더에서 언제든지 만들 수 있습니다!", Toast.LENGTH_SHORT).show();
                            }
                        });
                        AlertDialog dialog = builder.create(); // 위의 builder를 생성할 AlertDialog 객체 생성
                        dialog.show(); // dialog를 화면에 뿌려 줌
                    } else {
                        selectTravelName.setText(schedule.getTravel_name());
                        selectTravelName2.setText(schedule.getTravel_name());
                        layout_4.setVisibility(layout_4.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
                    }
                }
                break;
            case R.id.top10:
                Log.e("top10", "tlqkf");
                j = "jsons/topTen.json";
                a = OverlayImage.fromResource(R.drawable.best_place);
                if (top_count % 2 == 0) {
                    buttonMarker(naverMap, j, a, true, top10M);
                } else {
                    buttonMarker(naverMap, j, a, false, top10M);
                    Log.e("dnn", "nn");
                }
                top_count++;
                break;
            case R.id.realFinish:
                String hash = "";
                for (int p = 0; p < daysc.size(); p++) {
                    for (int q = 0; q < daysc.get(p).size(); q++) {
                        ArrayList<daySchedule> info = daysc.get(p);
                        String name = info.get(q).getName();
                        String latitude = String.valueOf(info.get(q).getLatitude());
                        String longitude = String.valueOf(info.get(q).getLongitude());
                        String address = info.get(q).getAdress();
                        String transportation = info.get(q).getTransportation();
                        String path = info.get(q).getPath();
                        //얘네 이름 위도 경도 사용자 디비 상세일정에 넣으면 됩니다.

                        HashMap newDetail = new HashMap<>();
                        newDetail.put("place_name", name);
                        newDetail.put("place_address", address);
                        newDetail.put("latitude", latitude);
                        newDetail.put("longitude", longitude);
                        newDetail.put("transportation", transportation);
                        newDetail.put("path", path);

                        Log.e("<add plan>", "왜 안되냐구여");
                        Log.e("<add plan>", LoginActivity.getEmail() + ", " + schedule.getKey() + ", " + newDetail);

                        ma.mdatabase = FirebaseDatabase.getInstance().getReference();
                        ma.mdatabase.child("사용자").child(LoginActivity.getEmail()).child("일정").child(schedule.getKey()).child("detail").child(String.valueOf(p + 1)).child(String.valueOf(q)).setValue(newDetail);

                        Log.e("avav", name + "," + latitude + "--" + longitude);

                        for (int r = 0; r < randmarkname.size(); r++) { //이것은 해시 찾아서 사용자한테 넣는 코드
                            if (randmarkname.get(r).getName().equals(name)){
                                hash = hash.concat(randmarkname.get(r).getHashtag()).concat(",");
                            }
                        }

                    }
                }
//                Log.e("<기존 해시>", userHash);
                Log.e("<여행지 해시>", hash);
                calANDinsert(hash + userHash);

                layout_5.setVisibility(View.GONE);
                Toast.makeText(getContext(), "완료되었습니다!", Toast.LENGTH_SHORT).show();

                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.main_layout, new FragmentCalender())
                        .addToBackStack(null)
                        .commit();

                break;
            case R.id.btn_addPlanClose:
                layout_4.setVisibility(View.GONE);
                break;
            case R.id.btn_addPlanClose2:
                layout_5.setVisibility(View.GONE);
                layout_4.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_clear:
                Toast.makeText(getContext(), "검색결과를 지웠습니다.", Toast.LENGTH_SHORT).show();
                for (Marker m : searchM) {
                    m.setMap(null);
                }
                searchM.clear();
                break;
        }
    }

    public static void updateplan() {
        detailSchAdapter = new detailSchAdapter(daysc.get(Integer.parseInt(getday) - 1));//, getActivity());
        detailSchAdapter.notifyDataSetChanged();
        detailSchRecyclerView.setAdapter(detailSchAdapter);
    }

    public void SetListener() {
        food.setOnClickListener(this);
        bed.setOnClickListener(this);
        medical.setOnClickListener(this);
        tour.setOnClickListener(this);
        korea.setOnClickListener(this);
        japan.setOnClickListener(this);
        boonsick.setOnClickListener(this);
        chicken.setOnClickListener(this);
        fast.setOnClickListener(this);
        coffee.setOnClickListener(this);
        finish.setOnClickListener(this);
        bookmark.setOnClickListener(this);
        open.setOnClickListener(this);
        bycicle.setOnClickListener(this);
        top10.setOnClickListener(this);
        realFinish.setOnClickListener(this);
        ct_bookmark.setOnClickListener(this);

        addPlanClose.setOnClickListener(this);
        addPlanClose2.setOnClickListener(this);
        btn_clear.setOnClickListener(this);
    }

    public void buttonMarker(NaverMap naverMap, String j, com.naver.maps.map.overlay.OverlayImage a, boolean tf, Vector<Marker> vMarker) {
        AssetManager assetManager = getResources().getAssets();
        markerPosition = new Vector<>();
        textView_n = (TextView) viewGroup.findViewById(R.id.name);
        textView_a = (TextView) viewGroup.findViewById(R.id.address);

        if (tf) {
            try {
                InputStream is = assetManager.open(j);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                StringBuffer buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
                String jsonData = buffer.toString();
                JSONArray jsonarray = new JSONArray(jsonData);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonObj = jsonarray.getJSONObject(i);

                    double longitude = jsonObj.getDouble("경도");
                    double latitude = jsonObj.getDouble("위도");

                    String n;
                    try {
                        n = jsonObj.getString("상호명");
                    } catch (JSONException e) {
                        try {
                            n = jsonObj.getString("이름");
                        } catch (JSONException ee) {
                            n = jsonObj.getString("대여소명");
                        }
                    }

                    String d;
                    try {
                        d = jsonObj.getString("도로명주소");
                    } catch (JSONException e) {
                        try {
                            d = jsonObj.getString("주소");
                            if (d.contains("\\n\\t\\t\\t")) {
                                d = d.replace("\\n\\t\\t\\t", "");
                            }
                        } catch (JSONException ee) {
                            d = jsonObj.getString("거치대수");
                        }
                    }

                    String name = n;
                    String add = d;

                    switch (city) {
                        case "서울":
                            if (latitude <= 37.643460 && latitude >= 37.424293 && longitude <= 127.232915 && longitude >= 126.779291) {
                                setMark(latitude, longitude, a, name, vMarker, add);
                            }
                            break;
                        case "강릉":
//                            37.752070, 128.875985
                            if (latitude <= 38.643460 && latitude >= 37.000000 && longitude <= 129.232915 && longitude >= 127.779291) {
                                setMark(latitude, longitude, a, name, vMarker, add);
                            }
                            break;
                        case "전남":
//                            34.760388, 127.662298
                            if (latitude <= 35.160415 && latitude >= 33.838605 && longitude <= 127.892558 && longitude >= 126.851407) {
                                setMark(latitude, longitude, a, name, vMarker, add);
                            }
                            break;
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            for (Marker m : vMarker) {
                m.setMap(null);
            }
            vMarker.clear();
        }
    }

    public static void setCity(String n) {
        city = n;
    }

    public void setCamera(NaverMap naverMap, String n) {
        this.naverMap = naverMap;
        LatLng initialPosition;
        CameraUpdate cameraUpdate;

        switch (n) {
            case "서울":
                initialPosition = new LatLng(37.5666102, 126.9783881);//서울시청
                cameraUpdate = CameraUpdate.scrollTo(initialPosition);
                naverMap.moveCamera(cameraUpdate);
                break;
            case "전남":
                initialPosition = new LatLng(34.760388, 127.662298);//서울시청
                cameraUpdate = CameraUpdate.scrollTo(initialPosition);
                naverMap.moveCamera(cameraUpdate);
                break;
            case "강릉":
                initialPosition = new LatLng(37.752070, 128.875985);//서울시청
                cameraUpdate = CameraUpdate.scrollTo(initialPosition);
                naverMap.moveCamera(cameraUpdate);
                break;
        }

    }

    public void setMark(double latitude, double longitude, com.naver.maps.map.overlay.OverlayImage a, String name, Vector<Marker> vMarker, String add) {
        markerPosition.add(new LatLng(latitude, longitude));
        Marker marker = new Marker();
        LatLng latLng = new LatLng(latitude, longitude);
        marker.setPosition(latLng);
        marker.setIcon(a);
        marker.setWidth(70);
        marker.setHeight(75);
        marker.setMap(naverMap);
        marker.setCaptionText(name);
        vMarker.add(marker);

        marker.setOnClickListener(o -> {
            Animation animation = new AlphaAnimation(0, 1);
            animation.setDuration(300);

            if (textView_n.getText().equals(name)) {
                layout_3.setVisibility(layout_3.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            } else {
                if (layout_3.getVisibility() == View.VISIBLE) {
                    layout_3.setVisibility(View.GONE);
                    textView_n.setText(name);
                    textView_a.setText(add);
                    layout_3.setVisibility(View.VISIBLE);

                    LatLng l = marker.getPosition();
                    adname = name;
                    adlatitude = l.latitude;
                    adlongitude = l.longitude;
                    adadress = add;

                    layout_3.setAnimation(animation);
                } else {
                    textView_n.setText(name);
                    textView_a.setText(add);
                    layout_3.setVisibility(View.VISIBLE);

                    LatLng l = marker.getPosition();
                    adname = name;
                    adlatitude = l.latitude;
                    adlongitude = l.longitude;
                    adadress = add;

                    layout_3.setAnimation(animation);
                }
            }
            return true;
        });
    }

    public void MapSearch(NaverMap naverMap, String j, String keyword, com.naver.maps.map.overlay.OverlayImage a, boolean tf, Vector<Marker> vMarker) {
        AssetManager assetManager = getResources().getAssets();
        markerPosition = new Vector<>();
        textView_n = (TextView) viewGroup.findViewById(R.id.name);
        textView_a = (TextView) viewGroup.findViewById(R.id.address);

        if (tf) {
            try {
                InputStream is = assetManager.open(j);
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader reader = new BufferedReader(isr);

                StringBuffer buffer = new StringBuffer();
                String line = reader.readLine();
                while (line != null) {
                    buffer.append(line + "\n");
                    line = reader.readLine();
                }
                String jsonData = buffer.toString();
                JSONArray jsonarray = new JSONArray(jsonData);

                for (int i = 0; i < jsonarray.length(); i++) {
                    JSONObject jsonObj = jsonarray.getJSONObject(i);

                    double longitude = 0;
                    double latitude = 0;
                    String n = null;
                    String d = null;

                    try {
                        if (jsonObj.getString("상호명").contains(keyword)) {
                            longitude = jsonObj.getDouble("경도");
                            latitude = jsonObj.getDouble("위도");

                            n = jsonObj.getString("상호명");

                            Log.e("<search>", n);

                            try {
                                d = jsonObj.getString("도로명주소");
                            } catch (JSONException e) {
                                d = jsonObj.getString("소재지도로명주소");
                            }

                            setMark(latitude, longitude, a, n, vMarker, d);
                        }
                    } catch (JSONException e) {
                        if (jsonObj.getString("관광지명").contains(keyword)) {
                            longitude = jsonObj.getDouble("경도");
                            latitude = jsonObj.getDouble("위도");

                            n = jsonObj.getString(("관광지명"));

                            Log.e("<search>", n);

                            try {
                                d = jsonObj.getString("도로명주소");
                            } catch (JSONException e2) {
                                d = jsonObj.getString("소재지도로명주소");
                            }

                            setMark(latitude, longitude, a, n, vMarker, d);
                        }
                    }
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }
        } else {
            for (Marker m : vMarker) {
                m.setMap(null);
            }
            vMarker.clear();
        }
    }

    private void exec() {
        DatabaseReference getFrq = database.getReference("여행지");
        randmarkname.clear();

        getFrq.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Custom custom = snapshot.getValue(Custom.class);
                    randmarkname.add(custom);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("walkaway", String.valueOf(databaseError.toException()));
            }
        });
    }

    private void calANDinsert(String hash) {

        String h[] = hash.split(",");
        int count = 0;
        int c[] = new int[h.length];

        for (int i = 0; i < h.length; i++) {
            if (!h[i].equals("9999")) {
                for (int j = i + 1; j < h.length; j++) {
                    if (h[i].equals(h[j])) {
                        count++;
                        h[j] = "9999";
                    }
                }
                c[i] = count;
            }
        }
        int temp = 0;
        int max = 0;
        String stemp = "";

        for (int i = 0; i < c.length; i++) {
            for (int j = i + 1; j < c.length; j++) {
                if (c[max] < c[j]) {
                    max = j;
                }
            }
            temp = c[i];
            stemp = h[i];
            c[i] = c[max];
            h[i] = h[max];
            c[max] = temp;
            h[max] = stemp;
        }

        String htag = "";
        for (int i = 0; i < c.length; i++) {
            if (!h[i].equals("9999"))
                htag = htag.concat(h[i]).concat(",");
        }

//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference ref = database.getReference("사용자").child(id).child("hash").child("in");

        Map<String, userhash> hashtag = new HashMap<>();
        userhash userhash = new userhash();
        userhash.setTag(htag);
        hashtag.put("tag", userhash);
//        ref.setValue(hashtag);

        HashMap newHash = new HashMap<>();
        newHash.put("취향", htag);

        Log.e("<바뀐 해쉬,,>", userhash.getTag());

        ma.mdatabase = FirebaseDatabase.getInstance().getReference();
        ma.mdatabase.child("사용자").child(id).child("설정").updateChildren(newHash);
    }

    public void setUserHash() {
        ma.mdatabase = FirebaseDatabase.getInstance().getReference("사용자").child(id).child("설정").child("취향");
        ma.mdatabase.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                String hash = String.valueOf(dataSnapshot.getValue());
                Toast.makeText(getContext(), hash, Toast.LENGTH_SHORT).show();
                String result = hash.substring(hash.lastIndexOf("n") + 5, hash.length() - 1);
                userHash = result;
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}