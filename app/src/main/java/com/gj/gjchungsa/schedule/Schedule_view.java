package com.gj.gjchungsa.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.ImageSliderAdapter;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Schedule_view extends AppCompatActivity {
    Chungsa_user_InfoManager chungsa_user_infoManager;
    public Chungsa_user user;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    ImageView schedule_image;
    Schedule view = new Schedule();
    Button schedule_edit_button;

    ArrayList<Schedule> schedule_list = new ArrayList<>();

    private static final String schedule_url ="http://gjchungsa.com/schedule/schedule.php";
    private static final String image_url ="http://gjchungsa.com/schedule/img/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);


        schedule_edit_button = findViewById(R.id.schedule_edit_button);
        if(chungsa_user_infoManager.chungsa_user != null) {
            user = chungsa_user_infoManager.getUserInfo();  //로그인 정보가져오기
            if(user.getChungsa_user_grade().equals("최고관리자") || user.getChungsa_user_grade().equals("중간관리자") ){  //편집 아이콘 보여주기 설정
                schedule_edit_button.setVisibility(View.VISIBLE);
            }
        }


        StringRequest stringRequest = new StringRequest(Request.Method.POST, schedule_url,  //교회일정 불러오기
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        schdeule_view(response);
                        Log.d("schedule", response);

                    }
                    private void schdeule_view(String response){
                        Gson gson = new Gson();
                        Schedule[] schedules= gson.fromJson(response, Schedule[].class);

                        if (schedules != null) {
                            schedule_list.addAll(Arrays.asList(schedules));
                        }
                        list();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        // 에러 처리
                        Toast.makeText(getApplicationContext(), "교회 일정 오류", Toast.LENGTH_SHORT).show();

                        Log.i("Schedule", "Schedule Volley오류");
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

    public void list(){
        String url[] = new String[schedule_list.size()];

        for(int i =0; i<schedule_list.size();i++){
            url[i] = image_url + schedule_list.get(i).getSchedule_url();
        }
        sliderViewPager = findViewById(R.id.schedule_sliderViewPager);

        sliderViewPager.setOffscreenPageLimit(1);
        sliderViewPager.setAdapter(new ImageSliderAdapter(this, url));
        sliderViewPager.setCurrentItem(schedule_list.size()-1);

    }
    public void edit_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Schedule_edit.class);
        intent.putExtra("schedule_list", schedule_list);
        startActivity(intent);
    }
}

