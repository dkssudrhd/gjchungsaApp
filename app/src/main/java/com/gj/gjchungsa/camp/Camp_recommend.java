package com.gj.gjchungsa.camp;

import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.ResultSerachKeyword.Camp_mark_Place;
import com.gj.gjchungsa.camp.ResultSerachKeyword.Camp_mark_Place_Adapter;
import com.gj.gjchungsa.camp.edit.Camp_mark_insert;
import com.google.gson.Gson;
import com.kakao.vectormap.KakaoMap;
import com.kakao.vectormap.KakaoMapReadyCallback;
import com.kakao.vectormap.LatLng;
import com.kakao.vectormap.MapLifeCycleCallback;
import com.kakao.vectormap.MapView;
import com.kakao.vectormap.camera.CameraAnimation;
import com.kakao.vectormap.camera.CameraUpdateFactory;
import com.kakao.vectormap.label.Label;
import com.kakao.vectormap.label.LabelLayer;
import com.kakao.vectormap.label.LabelManager;
import com.kakao.vectormap.label.LabelOptions;
import com.kakao.vectormap.label.LabelStyle;
import com.kakao.vectormap.label.LabelStyles;
import com.kakao.vectormap.label.LodLabelLayer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_recommend extends AppCompatActivity{

    public Label labels[];
    public Label place_labels[];
    public static ArrayList<Camp_mark> camp_marks;
    ArrayList<Camp_mark> food_list, view_list, house_list, cafe_list;
    ArrayList<Camp_mark_Place> places = new ArrayList<>();
    String camp_mark_all_url = "http://gjchungsa.com/camp_mark/campmark_all.php";

    private static final String BASE_URL = "https://dapi.kakao.com/";
    private static final String API_KEY = "KakaoAK f815506076c582415708f0c8c20cc244"; // REST API 키

    EditText camp_recommend_editText;
    Button camp_recommend_search, camp_recommend_hide;

    RecyclerView place_recyclerView;
    MapView mapView ;
    KakaoMap kakaoMap;
    LodLabelLayer lodLayer;
    LabelLayer layer;
    LabelManager labelManager;
    double my_location_x, my_location_y;
    double start_x, start_y;
    LocationManager lm;
    Camp_mark_Place now_place; //현재 선택된 장소

    int food_size, view_size, cafe_size, house_size;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_recommend);

        Intent get_intent = getIntent();
        start_x = get_intent.getDoubleExtra("x", 0.0);
        start_y = get_intent.getDoubleExtra("y", 0.0);


        camp_recommend_editText = findViewById(R.id.camp_recommend_editText);
        camp_recommend_search = findViewById(R.id.camp_recommend_search);
        place_recyclerView = findViewById(R.id.camp_mark_place_recyclerview);
        camp_recommend_hide = findViewById(R.id.camp_recommend_hide);

        mapView = findViewById(R.id.map_view);

        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        mapView.start(new MapLifeCycleCallback() {
            @Override
            public void onMapDestroy() {
                // 지도 API 가 정상적으로 종료될 때 호출됨
                Log.d("Camp_recommend", "지도 종료");
            }

            @Override
            public void onMapError(Exception error) {
                // 인증 실패 및 지도 사용 중 에러가 발생할 때 호출됨
                Log.d("Camp_recommend", "지도 에러");
            }
        }, new KakaoMapReadyCallback() {
            @NonNull
            @Override
            public LatLng getPosition() {   //지도 처음시작위치
                return LatLng.from(start_y, start_x);
            }

            @NonNull
            @Override
            public int getZoomLevel() {     //지도 처음 줌 크기
                return 9;
            }

            @Override
            public void onMapReady(KakaoMap kakao) {
                // 인증 후 API 가 정상적으로 실행될 때 호출됨
                kakaoMap = kakao;
                labelManager = kakaoMap.getLabelManager();
                layer = labelManager.getLayer();
                lodLayer = labelManager.getLodLayer();

                my_location();
                Camp_mark_Label();

                IntentFilter filter = new IntentFilter("camp_mark_place_checked");      //브로드 캐스트 수신
                registerReceiver(receiver, filter);                                            // Camp_mark_Place_Adapter의 정보를 받아옴
            }

        });

        camp_recommend_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = camp_recommend_editText.getText().toString();
                Log.d("Camp_recommend", text + "검색");
                searchKeyword(text);
            }
        });

        camp_recommend_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                place_recyclerView.setVisibility(View.INVISIBLE);
                camp_recommend_hide.setVisibility(View.INVISIBLE);
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("camp_mark_place_checked".equals(intent.getAction())) {
                Camp_mark_Place mark_place = (Camp_mark_Place) intent.getSerializableExtra("camp_mark_place");
                click_place(mark_place);
            }
        }
    };

    private void click_place(Camp_mark_Place camp_mark_place){ // place 클릭시 이동하고 넘어감
        place_recyclerView.setVisibility(View.INVISIBLE);
        if(kakaoMap.getZoomLevel()<15)
            kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(
                Double.parseDouble(camp_mark_place.getY()),
                Double.parseDouble(camp_mark_place.getX()))),
                CameraAnimation.from(150, true, true)); //부드럽게 넘어가도록 효과
        search_place_view(camp_mark_place);
    }

    private void click_place(Camp_mark camp_mark){  //camp_mark로 가져오기
        place_recyclerView.setVisibility(View.INVISIBLE);
        if(kakaoMap.getZoomLevel()<15)
            kakaoMap.moveCamera(CameraUpdateFactory.zoomTo(15));
        kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(
                        camp_mark.getCamp_mark_y(),
                        camp_mark.getCamp_mark_x())),
                CameraAnimation.from(150, true, true));

        search_place_view(camp_mark);

    }

    private void search_place_view(Camp_mark camp_mark){        //Camp_mark 선택시 밑에서 올라오기
        LinearLayout linearLayout = findViewById(R.id.camp_recommend_place_linearLayout);
        TextView place_title = findViewById(R.id.camp_recommend_place_title);
        TextView road_address_name = findViewById(R.id.camp_recommend_place_road_address_name);
        TextView phone = findViewById(R.id.camp_recommend_place_phone);
        TextView url = findViewById(R.id.camp_recommend_place_url);
        linearLayout.setVisibility(View.VISIBLE);

        place_title.setText(camp_mark.getCamp_mark_title());
        road_address_name.setText(camp_mark.getCamp_mark_where_detail());
        phone.setText(camp_mark.getCamp_mark_phone());
        url.setText(camp_mark.getCamp_mark_link());

        ImageView camp_recommend_mark_view = findViewById(R.id.camp_recommend_mark_view);
        ImageView camp_recommend_place_mark_insert = findViewById(R.id.camp_recommend_place_mark_insert);
        camp_recommend_mark_view.setVisibility(View.VISIBLE);
        camp_recommend_place_mark_insert.setVisibility(View.INVISIBLE);

        camp_recommend_mark_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Camp_mark_bbs_view.class);
                intent.putExtra("Camp_mark", camp_mark);
                startActivity(intent);
            }
        });

        if(!camp_mark.getCamp_mark_link().equals("")){
            Uri uri = Uri.parse(camp_mark.getCamp_mark_link());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);


            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(camp_mark.getCamp_mark_link().contains("instagram")){        //인스타일시
                        intent.setPackage("com.instagram.android"); // 인스타그램 앱을 사용하여 열기
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        try {
                            startActivity(intent);
                        } catch (ActivityNotFoundException e) {
                            // 인스타그램 앱이 설치되어 있지 않은 경우, 웹 브라우저에서 열기
                            intent.setPackage(null); // 패키지 설정 제거
                            startActivity(intent);
                        }
                    }
                    else {  //아닐시
                        startActivity(intent);
                    }
                }
            });
        }

    }

    private void search_place_view(Camp_mark_Place camp_mark_place){        //선택시 밑에서 올라오기
        now_place = camp_mark_place;
        LinearLayout linearLayout = findViewById(R.id.camp_recommend_place_linearLayout);
        TextView place_title = findViewById(R.id.camp_recommend_place_title);
        TextView road_address_name = findViewById(R.id.camp_recommend_place_road_address_name);
        TextView phone = findViewById(R.id.camp_recommend_place_phone);
        TextView url = findViewById(R.id.camp_recommend_place_url);
        linearLayout.setVisibility(View.VISIBLE);

        place_title.setText(camp_mark_place.getPlace_name());
        road_address_name.setText(camp_mark_place.getRoad_address_name());
        phone.setText(camp_mark_place.getPhone());
        url.setText(camp_mark_place.getPlace_url());

        if(!camp_mark_place.getPlace_url().equals("")){
            Uri uri = Uri.parse(camp_mark_place.getPlace_url());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            url.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(intent);
                }
            });
        }

        ImageView camp_recommend_mark_view = findViewById(R.id.camp_recommend_mark_view);
        ImageView camp_recommend_place_mark_insert = findViewById(R.id.camp_recommend_place_mark_insert);
        camp_recommend_mark_view.setVisibility(View.INVISIBLE);
        camp_recommend_place_mark_insert.setVisibility(View.VISIBLE);

    }
    public void camp_recommend_place_mark_insert(View view){
        Intent intent = new Intent(getApplicationContext(), Camp_mark_insert.class);
        intent.putExtra("insert_update", 0);
        intent.putExtra("now_place", now_place);
        startActivity(intent);
    }

    public void camp_recommend_place_esc(View view){
        LinearLayout linearLayout = findViewById(R.id.camp_recommend_place_linearLayout);
        linearLayout.setVisibility(View.INVISIBLE);
    }



    private void search_place_label(ArrayList<Camp_mark_Place> camp_mark_places){       //Camp_mark_place로 label만들기

        LabelLayer layer = kakaoMap.getLabelManager().getLayer();

        if(place_labels != null){

            for(int i=0;i <place_labels.length;i++){
                place_labels[i].hide();
            }
            labelManager.update(place_labels);
        }

        place_labels = new Label[camp_mark_places.size()];

        for(int i=0;i<camp_mark_places.size();i++) {
            LabelStyles styles = LabelStyles.from("search_" + Integer.toString(i) ,
                    LabelStyle.from(R.drawable.map_point_icon_red_s).setZoomLevel(6),
                    LabelStyle.from(R.drawable.map_point_icon_red_s).setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15));

            Camp_mark_Place place = camp_mark_places.get(i);
            LabelOptions options = LabelOptions.from(LatLng.from(Double.parseDouble(place.getY()), Double.parseDouble(place.getX())))
                    .setClickable(true)
                    .setStyles(styles);
            Label label = layer.addLabel(options);


            place_labels[i] =label;
        }

        kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {  //라벨 클릭시
            @Override
            public void onLabelClicked(KakaoMap kakaoMap, LabelLayer layer, Label label) {

                for(int i=0; i<camp_mark_places.size();i++){
                    String str = "search_" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str))
                        click_place(camp_mark_places.get(i));

                }

                for(int i=0; i<food_size;i++){
                    String str1 = "camp_mark_food" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(food_list.get(i));
                    }
                }
                for(int i=0; i<view_size;i++){
                    String str1 = "camp_mark_view" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(view_list.get(i));
                    }
                }
                for(int i=0; i<house_size;i++){
                    String str1 = "camp_mark_house" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(house_list.get(i));
                    }
                }
                for(int i=0; i<cafe_size;i++){
                    String str1 = "camp_mark_cafe" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(cafe_list.get(i));
                    }
                }


            }
        });

        labelManager.update(place_labels);

    }


    final LocationListener gpsLocationListener = new LocationListener() {       //위치를 가져오기
        public void onLocationChanged(Location location) {
            my_location_x = location.getLongitude(); // 경도
            my_location_y = location.getLatitude(); // 위도


        } public void onStatusChanged(String provider, int status, Bundle extras) {

        } public void onProviderEnabled(String provider) {

        } public void onProviderDisabled(String provider) {

        }
    };

    public void my_location(){
        if ( Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission( getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION ) != PackageManager.PERMISSION_GRANTED ) {
            ActivityCompat.requestPermissions( this, new String[] {
                    android.Manifest.permission.ACCESS_FINE_LOCATION}, 0 );

        }
        else{
            // 가장최근 위치정보 가져오기
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location != null) {
                String provider = location.getProvider();
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                double altitude = location.getAltitude();

                //kakaoMap.moveCamera(CameraUpdateFactory.newCenterPosition(LatLng.from(latitude, longitude))); //내 위치로 이동
            }

            // 위치정보를 원하는 시간, 거리마다 갱신해준다.
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
            lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                    1000,
                    1,
                    gpsLocationListener);
        }
    }
    public void Camp_mark_Label(){      //Camp_mark 가져와서 label로 만들기

        StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_mark_all_url,
                new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                Camp_mark_listup(response);
            }

            private void Camp_mark_listup(String response) {
                Gson gson = new Gson();
                camp_marks =new ArrayList<>();
                Camp_mark[] camp_mark = gson.fromJson(response, Camp_mark[].class);

                if (camp_mark != null) {
                    camp_marks.addAll(Arrays.asList(camp_mark));
                }
                camp_mark_view();          //여기로 라벨만들기
            }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Camp_recommend_bbs", "Camp_recommend_bbs Volley오류");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

    private void camp_mark_view(){

        LabelLayer layer = kakaoMap.getLabelManager().getLayer();

        labels = new Label[camp_marks.size()];

        cafe_size =0; food_size =0; view_size=0; house_size=0;

        food_list = new ArrayList<>();
        view_list = new ArrayList<>();
        house_list = new ArrayList<>();
        cafe_list = new ArrayList<>();

        for(int i=0;i<camp_marks.size();i++) {//나중에 여기에 타입마다 바꿔줘야 함
            LabelStyles styles =null;
            if(camp_marks.get(i).getCamp_mark_type().equals("먹거리")) {
                styles = LabelStyles.from("camp_mark_food" + Integer.toString(food_size),
                        LabelStyle.from(R.drawable.map_point_icon_food_s).setZoomLevel(6),
                        LabelStyle.from(R.drawable.map_point_icon_food_s).setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15));
                food_size++;
                food_list.add(camp_marks.get(i));
            } else if(camp_marks.get(i).getCamp_mark_type().equals("볼거리")) {
                styles = LabelStyles.from("camp_mark_view" + Integer.toString(view_size),
                        LabelStyle.from(R.drawable.map_point_icon_view_s).setZoomLevel(6),
                        LabelStyle.from(R.drawable.map_point_icon_view_s).setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15));
                view_size++;
                view_list.add(camp_marks.get(i));
            } else if(camp_marks.get(i).getCamp_mark_type().equals("숙소")) {
                styles = LabelStyles.from("camp_mark_house" + Integer.toString(house_size),
                        LabelStyle.from(R.drawable.map_point_icon_house_s).setZoomLevel(6),
                        LabelStyle.from(R.drawable.map_point_icon_house_s).setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15));
                house_size++;
                house_list.add(camp_marks.get(i));
            } else if(camp_marks.get(i).getCamp_mark_type().equals("카페")){
                styles = LabelStyles.from("camp_mark_cafe" + Integer.toString(cafe_size),
                        LabelStyle.from(R.drawable.map_point_icon_cafe_s).setZoomLevel(6),
                        LabelStyle.from(R.drawable.map_point_icon_cafe_s).setTextStyles(32, Color.BLACK, 1, Color.GRAY).setZoomLevel(15));
                cafe_size++;

                cafe_list.add(camp_marks.get(i));
            }

            Camp_mark mark = camp_marks.get(i);

            LabelOptions options = LabelOptions.from(LatLng.from(
                            mark.getCamp_mark_y(),
                            mark.getCamp_mark_x()))
                    .setClickable(true)
                    .setStyles(styles);
            Label label = layer.addLabel(options);


            labels[i] =label;
        }

        kakaoMap.setOnLabelClickListener(new KakaoMap.OnLabelClickListener() {  //라벨 클릭시
            @Override
            public void onLabelClicked(KakaoMap kakaoMap, LabelLayer layer, Label label) {

                for(int i=0; i<food_size;i++){
                    String str1 = "camp_mark_food" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(food_list.get(i));
                    }
                }
                for(int i=0; i<view_size;i++){
                    String str1 = "camp_mark_view" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(view_list.get(i));
                    }
                }
                for(int i=0; i<house_size;i++){
                    String str1 = "camp_mark_house" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(house_list.get(i));
                    }
                }
                for(int i=0; i<cafe_size;i++){
                    String str1 = "camp_mark_cafe" + Integer.toString(i);
                    if(label.getStyles().getStyleId().equals(str1)){
                        click_place(cafe_list.get(i));
                    }
                }

            }
        });

        labelManager.update(labels);

        checkbox_setting();
    }

    public void searchKeyword(String keyword) {         //검색한 키워드 카카오 검색api로 받아오기

        String apiUrl = BASE_URL + "v2/local/search/keyword.json?" +
                "y="+ Double.toString(my_location_y) +
                "&x=" + Double.toString(my_location_x) +
                "&query=" + keyword;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 통신 성공
                        Log.d("Search", "Response: " + response.toString());

                        places = new ArrayList<>();

                        try {
                            JSONArray documents = response.getJSONArray("documents");
                            for (int i = 0; i < documents.length(); i++) {
                                JSONObject document = documents.getJSONObject(i);
                                String place_name = document.getString("place_name");
                                String phone = document.getString("phone");
                                String road_address_name = document.getString("road_address_name");
                                String x = document.getString("x");
                                String y = document.getString("y");
                                String place_url = document.getString("place_url");
                                places.add(new Camp_mark_Place(i + 1, place_name, phone, road_address_name, x, y, place_url));

                            }
                            LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
                            place_recyclerView.setLayoutManager(layoutManager);
                            Camp_mark_Place_Adapter camp_mark_place_adapter = new Camp_mark_Place_Adapter();

                            search_place_label(places);

                            for (int i = 0; i < places.size(); i++) {//처음 10개 불러오기
                                camp_mark_place_adapter.addItem(places.get(i));
                            }
                            place_recyclerView.setAdapter(camp_mark_place_adapter);
                            place_recyclerView.setVisibility(View.VISIBLE);
                            place_recyclerView.bringToFront();

                            camp_recommend_hide.setVisibility(View.VISIBLE);
                        } catch (JSONException e) {
                            Log.d("Camp_recommend", "place 에러");
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Search", "통신 실패: " + error.getMessage());
                    }

                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", API_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);

    }

    private void checkbox_setting(){
        CheckBox food_checkbox, view_checkbox, house_checkbox, cafe_checkbox;
        food_checkbox = findViewById(R.id.food_checkbox);
        view_checkbox = findViewById(R.id.view_checkbox);
        house_checkbox = findViewById(R.id.house_checkbox);
        cafe_checkbox = findViewById(R.id.cafe_checkbox);

        food_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {// 체크박스가 선택되었을 때의 동작
                    for(int i=0;i< labels.length;i++){
                        String str = "camp_mark_food" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].show();
                            }
                        }
                    }
                    labelManager.update(labels);
                } else {// 체크박스가 해제되었을 때의 동작

                    for(int i=0;i<labels.length;i++){
                        String str = "camp_mark_food" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].hide();
                            }
                        }
                    }
                    labelManager.update(labels);
                }
            }
        });

        view_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {// 체크박스가 선택되었을 때의 동작
                    for(int i=0;i<labels.length;i++){
                        String str = "camp_mark_view" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].show();
                            }
                        }
                    }
                    labelManager.update(labels);
                } else {// 체크박스가 해제되었을 때의 동작

                    for(int i=0;i<labels.length;i++){
                        String str = "camp_mark_view" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].hide();
                            }
                        }
                    }
                    labelManager.update(labels);
                }
            }
        });

        house_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {// 체크박스가 선택되었을 때의 동작
                    for(int i=0;i<labels.length;i++){
                        String str = "camp_mark_house" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].show();
                            }
                        }
                    }
                    labelManager.update(labels);
                } else {// 체크박스가 해제되었을 때의 동작

                    for(int i=0;i<labels.length;i++){
                        String str = "camp_mark_house" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].hide();
                            }
                        }
                    }
                    labelManager.update(labels);
                }
            }
        });

        cafe_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {// 체크박스가 선택되었을 때의 동작
                    for(int i=0;i<cafe_size;i++){
                        String str = "camp_mark_cafe" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].show();
                            }
                        }
                    }
                    labelManager.update(labels);
                } else {// 체크박스가 해제되었을 때의 동작

                    for(int i=0;i<cafe_size;i++){
                        String str = "camp_mark_cafe" + Integer.toString(i);
                        for(int j=0;j< labels.length;j++){
                            if(labels[j].getStyles().getStyleId().equals(str)){
                                labels[j].hide();
                            }
                        }
                    }
                    labelManager.update(labels);
                }
            }
        });
    }

}