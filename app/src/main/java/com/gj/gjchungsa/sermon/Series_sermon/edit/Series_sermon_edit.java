package com.gj.gjchungsa.sermon.Series_sermon.edit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Series_sermon.Series_sermon;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Series_sermon_edit extends AppCompatActivity {

    RecyclerView recyclerView;
    Button insert_button;
    ImageButton left_arrow_button, right_arrow_button;
    String select_url = "http://gjchungsa.com/series_sermon/series_sermon_select.php";
    int page_num =0;
    Series_sermon_edit_Adapter adapter = new Series_sermon_edit_Adapter();

    ArrayList<Series_sermon> series_sermon_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_sermon_edit);

        findview_setting();

        IntentFilter filter = new IntentFilter("series_update");      //브로드 캐스트 수신
        registerReceiver(receiver, filter);

        series_sermon_listting();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void list_setting() {
        page_num =1;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Series_sermon_edit_Adapter();

        for(int i=0;i<10 && i< series_sermon_list.size() ; i++){//처음 10개 불러오기
            adapter.addItem(series_sermon_list.get(i));
        }
        recyclerView.setAdapter(adapter);

    }
    private void left_right_btn_setting() {

        left_arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    adapter = new Series_sermon_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< series_sermon_list.size() ; i++){
                        adapter.addItem(series_sermon_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < series_sermon_list.size()){
                    page_num++;
                    adapter = new Series_sermon_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< series_sermon_list.size() ; i++){
                        adapter.addItem(series_sermon_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void series_sermon_listting() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        listup(response);
                    }

                    private void listup(String response) {
                        Gson gson = new Gson();
                        series_sermon_list =new ArrayList<>();
                        Series_sermon[] series_sermons = gson.fromJson(response, Series_sermon[].class);
                        if (series_sermons != null) {
                            series_sermon_list.addAll(Arrays.asList(series_sermons));
                        }
                        page_num =1;

                        list_setting();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

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

    private void findview_setting() {
        recyclerView = findViewById(R.id.series_sermon_edit_recyclerView);
        insert_button = findViewById(R.id.series_sermon_edit_insert_button);
        left_arrow_button = findViewById(R.id.series_sermon_edit_left_arrow_button);
        right_arrow_button = findViewById(R.id.series_sermon_edit_right_arrow_button);

        insert_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Series_sermon_edit_insert.class);
                startActivity(intent);
            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("series_update".equals(intent.getAction())) {
                series_sermon_listting();
            }
        }
    };


}