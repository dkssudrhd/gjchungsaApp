package com.gj.gjchungsa.sermon.Series_sermon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Preach_bbs;
import com.gj.gjchungsa.sermon.Preach_bbs_Adapter;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Series_sermon_list extends AppCompatActivity {

    Series_sermon get_series_sermon;
    ArrayList<Preach_bbs> series_preach_bbs = new ArrayList<>();
    String pick_select_url = "http://gjchungsa.com/series_sermon/series_sermon_pick_select.php";
    int page_num =0;
    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text, type;
    RecyclerView recyclerView;
    Preach_bbs_Adapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_sermon_list);

        Intent get_intent = getIntent();
        get_series_sermon = (Series_sermon) get_intent.getSerializableExtra("series_sermon");

        findview_setting();

        series_sermon_loading();
    }

    private void findview_setting() {
        type = findViewById(R.id.series_sermon_list_type);
        start_text = findViewById(R.id.series_sermon_list_start_text);
        text_1 = findViewById(R.id.series_sermon_list_1_text);
        text_2 = findViewById(R.id.series_sermon_list_2_text);
        text_3 = findViewById(R.id.series_sermon_list_3_text);
        text_4 = findViewById(R.id.series_sermon_list_4_text);
        text_5 = findViewById(R.id.series_sermon_list_5_text);
        last_text = findViewById(R.id.series_sermon_list_last_text);
        recyclerView = findViewById(R.id.series_sermon_list_recycler_view);

        type.setText(get_series_sermon.getSeries_name());

        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


    }


    private void list_setting(){        //list셋팅
        adapter = new Preach_bbs_Adapter();
        for(int i=(page_num-1)*10 ; i<page_num*10 && i<series_preach_bbs.size() ;i++){
            adapter.addItem(series_preach_bbs.get(i));
        }
        recyclerView.setAdapter(adapter);
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
        if(page_num*10<series_preach_bbs.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<series_preach_bbs.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
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
                page_num = ((series_preach_bbs.size()-1) / 10)+ 1;
                btn_page_number_setting();
                list_setting();
            }
        });
    }


    private void series_sermon_loading() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, pick_select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        listup(response);
                    }

                    private void listup(String response) {
                        Gson gson = new Gson();
                        series_preach_bbs =new ArrayList<>();
                        Preach_bbs[] preach_bbss = gson.fromJson(response, Preach_bbs[].class);
                        if (preach_bbss != null) {
                            series_preach_bbs.addAll(Arrays.asList(preach_bbss));
                        }
                        page_num =1;

                        list_view_setting();
                        list_setting();
                        btn_page_number_setting();
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
                params.put("bbs_s_catalogue", get_series_sermon.getSeries_name());
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}