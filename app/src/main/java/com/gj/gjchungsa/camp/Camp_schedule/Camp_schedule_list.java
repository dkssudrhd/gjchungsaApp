package com.gj.gjchungsa.camp.Camp_schedule;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_schedule_insert;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_schedule_list extends AppCompatActivity {

    ArrayList<Camp_schedule> camp_schedule_list_all = new ArrayList<>();
    ArrayList<Camp_schedule> camp_schedule_list = new ArrayList<>();
    RecyclerView recyclerView;
    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;
    Camp_schedule_Adapter adapter = new Camp_schedule_Adapter();
    int page_num;
    EditText search_edit;

    String camp_schedule_list_url = "http://gjchungsa.com/camp_schedule/camp_schedule_all.php";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_schedule_list);

        IntentFilter filter = new IntentFilter("camp_schedule_delete");
        registerReceiver(receiver, filter);

        findview_setting();
        camp_schedule_list_setting();
    }

    private void findview_setting(){            //findview셋팅
        recyclerView = findViewById(R.id.camp_schedule_list_recyclerview);
        start_text = findViewById(R.id.camp_schedule_list_start_text);
        text_1 = findViewById(R.id.camp_schedule_list_1_text);
        text_2 = findViewById(R.id.camp_schedule_list_2_text);
        text_3 = findViewById(R.id.camp_schedule_list_3_text);
        text_4 = findViewById(R.id.camp_schedule_list_4_text);
        text_5 = findViewById(R.id.camp_schedule_list_5_text);
        last_text = findViewById(R.id.camp_schedule_list_last_text);
        search_edit = findViewById(R.id.camp_schedule_search_edit);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);


    }

    public void camp_schedule_search_btn_setting(View view){
        String keyword = search_edit.getText().toString();
        camp_schedule_list = new ArrayList<>();

        for(int i=0;i<camp_schedule_list_all.size();i++){
            Camp_schedule now_camp_schedule = camp_schedule_list_all.get(i);
            if(now_camp_schedule.getCamp_schedule_title().contains(keyword)  ||
                    now_camp_schedule.getCamp_schedule_content().contains(keyword) ||
                    now_camp_schedule.getCamp_schedule_when().contains(keyword) ||
                    now_camp_schedule.getCamp_schedule_creation().contains(keyword)||
                    now_camp_schedule.getCamp_schedule_type().contains(keyword) ||
                    now_camp_schedule.getCamp_schedule_where().contains(keyword) ||
                    now_camp_schedule.getCamp_schedule_what_family().contains(keyword) ||
                    now_camp_schedule.getCamp_schedule_family_name().contains(keyword) ||
                    now_camp_schedule.getChungsa_user_name().contains(keyword)){
                camp_schedule_list.add(now_camp_schedule);
            }
        }
        page_num =1;
        btn_page_number_setting();
        list_setting();
        list_view_setting();
    }

    private void list_view_setting(){       //recyclerView에 보여주는 버튼 셋팅
        start_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = 1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num-2;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num-1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num+1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num+2;
                btn_page_number_setting();
                list_setting();
            }
        });
        last_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = ((camp_schedule_list.size()-1) / 10)+ 1;
                btn_page_number_setting();
                list_setting();
            }
        });
    }
    private void btn_page_number_setting(){     //버튼의 숫자와 위치를 셋팅
        if(page_num>=3){
            text_1.setVisibility(View.VISIBLE);
            text_1.setText(Integer.toString(page_num-2));
        } else {
            text_1.setVisibility(View.GONE);
        }
        if(page_num>=2){
            text_2.setVisibility(View.VISIBLE);
            text_2.setText(Integer.toString(page_num-1));
        } else {
            text_2.setVisibility(View.GONE);
        }
        text_3.setText(Integer.toString(page_num));
        if(page_num*10<camp_schedule_list.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<camp_schedule_list.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
    }

    private void list_setting(){        //list셋팅
        adapter = new Camp_schedule_Adapter(page_num);

        for(int i=(page_num-1)*10 ; i<page_num*10 && i<camp_schedule_list.size() ;i++){
            adapter.addItem(camp_schedule_list.get(i));
        }

        recyclerView.setAdapter(adapter);
    }

    private void camp_schedule_list_setting(){      //camp_schedule_list 불러오기

        StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_list_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        camp_schedule_listup(response);
                    }

                    private void camp_schedule_listup(String response) {
                        Gson gson = new Gson();
                        camp_schedule_list =new ArrayList<>();
                        Camp_schedule[] camp_schedules = gson.fromJson(response, Camp_schedule[].class);
                        if (camp_schedules != null) {
                            camp_schedule_list.addAll(Arrays.asList(camp_schedules));
                        }
                        camp_schedule_list_all = camp_schedule_list;

                        page_num =1;
                        btn_page_number_setting();
                        list_setting();
                        list_view_setting();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Camp_recommend_bbs", "Camp_recommend_bbs_ Volley오류");
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
    public void camp_schedule_insert_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Camp_schedule_insert.class);
        startActivity(intent);
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("camp_schedule_delete".equals(intent.getAction())) {

                Toast.makeText(getApplicationContext(),"삭제를 완료하였습니다.", Toast.LENGTH_SHORT).show();
                camp_schedule_list_setting();
            }

        }
    };

}