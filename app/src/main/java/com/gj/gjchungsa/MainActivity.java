package com.gj.gjchungsa;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.advertisement.Advertisement;
import com.gj.gjchungsa.advertisement.Advertisement_view;
import com.gj.gjchungsa.camp.Camp_Main;
import com.gj.gjchungsa.introduction.Introduction_main;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.login.KakaoLogin;
import com.gj.gjchungsa.main_edit.main_edit;
import com.gj.gjchungsa.offering.offering_view;
import com.gj.gjchungsa.phozone.Photozone_list;
import com.gj.gjchungsa.schedule.Schedule_view;
import com.gj.gjchungsa.sermon.RequestHttpURLConnection;
import com.gj.gjchungsa.sermon.Sermon_list;
import com.gj.gjchungsa.weekly.Weekly_list;
import com.gj.gjchungsa.worshipguide.Worshipguide_main;
import com.google.gson.Gson;
import com.kakao.sdk.user.UserApiClient;
import com.kakao.sdk.user.model.User;
import com.smarteist.autoimageslider.SliderView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;

public class MainActivity extends AppCompatActivity {

    SliderView sliderView;
    Bitmap[] bitmap;
    String[] s_url;
    ImageButton button1, button2, button3, button4, button5, button6, button7, button8;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;

    ArrayList<Advertisement> advertisement_list =new ArrayList<>();
    int first_setting =0;
    private static final String login_url = "http://gjchungsa.com/login/login.php";
    private static final String advertisement_select = "http://gjchungsa.com/advertisement/advertisement_select.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        IntentFilter filter = new IntentFilter("advertisement");      //브로드 캐스트 수신
        registerReceiver(receiver, filter);

        // 현재 네트워크 정보 가져오기
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnected()) {
            String url = "http://gjchungsa.com/mainscreen.php";
            selectDatabase selectDatabase = new selectDatabase(url, null);
            selectDatabase.execute();
            advertisement_list_setting();


        } else {
            // 인터넷 연결이 없을 때 실행할 코드
            // 예: 네트워크 연결이 없을 때 동작
            Toast.makeText(getApplicationContext(),"데이터를 켜주세요", Toast.LENGTH_SHORT).show();
        }



        button1 = findViewById(R.id.sermon_main);
        button2 = findViewById(R.id.weekly);
        button3 = findViewById(R.id.introduction);
        button4 = findViewById(R.id.schedule);
        button5 = findViewById(R.id.worshipguide);
        button6 = findViewById(R.id.offering);
        button7 = findViewById(R.id.photozone);
        button8 = findViewById(R.id.camp_icon);

        auto_login(); //자동로그인

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Introduction_main.class);
                startActivity(intent);
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Worshipguide_main.class);
                startActivity(intent);
            }
        });
        button6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), offering_view.class);
                startActivity(intent);
            }
        });

        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Photozone_list.class);
                startActivity(intent);
            }
        });

        button8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Camp_Main.class);
                startActivity(intent);
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Sermon_list.class);
                startActivity(intent);
            }
        });

    }

    private void advertisement_list_setting() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, advertisement_select,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        listup(response);
                        Log.d("main_let's go",response);
                    }

                    private void listup(String response) {
                        Gson gson = new Gson();
                        advertisement_list =new ArrayList<>();
                        Advertisement[] advertisements = gson.fromJson(response, Advertisement[].class);
                        if (advertisements != null) {
                            advertisement_list.addAll(Arrays.asList(advertisements));
                        }

                        if(first_setting ==0 & advertisement_list.size()>0){
                            first_setting++;
                            Intent intent = new Intent(getApplicationContext(), Advertisement_view.class);
                            intent.putExtra("advertisement", advertisement_list.get(0));
                            startActivity(intent);
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.d("main_let's go", "에러");
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }


    public void auto_login(){
        UserApiClient.getInstance().me(new Function2<User, Throwable, Unit>() {
            @Override
            public Unit invoke(User user, Throwable throwable) {

                if (user != null) {

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, login_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                        user_login(response);
                                }
                                private void user_login(String response){
                                    Gson gson = new Gson();
                                    Chungsa_user chungsa_user;
                                    chungsa_user = gson.fromJson(response, Chungsa_user.class);
                                    Chungsa_user_InfoManager infoManager = new Chungsa_user_InfoManager();

                                    infoManager.setUserInfo(chungsa_user);

                                    Button main_edit_button = findViewById(R.id.main_edit_button);

                                    if(chungsa_user.getChungsa_user_grade().equals("최고관리자")){
                                        main_edit_button.setVisibility(View.VISIBLE);
                                    }
                                }

                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                    // 에러 처리
                                    Toast.makeText(getApplicationContext(), "오류", Toast.LENGTH_SHORT).show();

                                    Log.i("Login", "자동 Login Volley오류");
                                }

                            }) {
                        @Override
                        protected Map<String, String> getParams() {
                            Map<String, String> params = new HashMap<>();
                            params.put("chungsa_user_email",user.getKakaoAccount().getEmail() );
                            return params;
                        }
                    };

                    Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

                }
                return null;
            }
        });
    }

    public void schedule_btn(View view){    //교회일정 버튼
        Intent intent = new Intent(getApplicationContext(), Schedule_view.class);
        startActivity(intent);
    }

    public void weekly_push(View view){

        Intent intent = new Intent(getApplicationContext(), Weekly_list.class);
        startActivity(intent);

    }
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }

    public void main_edit_go(View view) {   // 편집으로 가기
        Intent intent = new Intent(getApplicationContext(), main_edit.class);
        startActivity(intent);
    }

    class selectDatabase extends AsyncTask<Void, Void, String> {

        private String url1;
        private ContentValues values1;
        String result1; // 요청 결과를 저장할 변수.


        public selectDatabase(String url, ContentValues contentValues) {
            this.url1 = url;
            this.values1 = contentValues;
        }

        @Override
        protected String doInBackground(Void... params) {
            RequestHttpURLConnection requestHttpURLConnection = new RequestHttpURLConnection();
            result1 = requestHttpURLConnection.request(url1, values1); // 해당 URL로 부터 결과물을 얻어온다.
            return result1; // 여기서 당장 실행 X, onPostExcute에서 실행
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //txtView.setText(s); // 파서 없이 전체 출력
            doJSONParser(s); // 파서로 전체 출력
        }
    }

    // 받아온 json 데이터를 파싱합니다..
    public void doJSONParser(String string) {   //메인 이미지 슬라이드
        try {
            String result = "";
            JSONArray jsonArray = new JSONArray(string);

            bitmap = new Bitmap[jsonArray.length()];
            s_url = new String[jsonArray.length()];

            for (int i=0; i < jsonArray.length(); i++) {
                JSONObject output = jsonArray.getJSONObject(i);
                s_url[i]= "http://gjchungsa.com/uploads/"+output.getString("ms_url");
            }

            sliderViewPager = findViewById(R.id.sliderViewPager);
            layoutIndicator = findViewById(R.id.layoutIndicators);

            sliderViewPager.setOffscreenPageLimit(1);
            sliderViewPager.setAdapter(new ImageSliderAdapter(this, s_url));

            sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentIndicator(position);
                }
            });
            setupIndicators(s_url.length);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    public void login_btn(View view){   //로그인 버튼
        Intent intent = new Intent(getApplicationContext(), KakaoLogin.class);
        startActivity(intent);
    }

    public void go_terms_of_use(View view){
        Uri uri = Uri.parse("http://chungsa.or.kr/new/sub_0705.html");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }
    public void go_privacy(View view){
        Uri uri = Uri.parse("http://chungsa.or.kr/new/sub_0706.html");
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("advertisement".equals(intent.getAction())) {

                if(advertisement_list.size() > first_setting){

                    Log.d("main_let's go",Integer.toString(first_setting));
                    Intent adverintent = new Intent(getApplicationContext(), Advertisement_view.class);
                    adverintent.putExtra("advertisement", advertisement_list.get(first_setting));
                    startActivity(adverintent);
                    first_setting++;
                }
            }
        }
    };
}