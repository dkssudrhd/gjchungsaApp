package com.gj.gjchungsa.camp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.edit.Camp_mark_insert;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_recommend_bbs extends AppCompatActivity {

    Intent get_intent;
    String where;
    ArrayList<Camp_mark> camp_mark_list;
    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;
    int page_num;
    ViewGroup.LayoutParams layoutParams;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_recommend_bbs);

        get_intent = getIntent();
        where = get_intent.getStringExtra("Camp_mark_where");
        String serverUrl;
        if(where.equals("전체"))
            serverUrl = "http://gjchungsa.com/camp_mark/campmark_all.php";
        else
            serverUrl = "http://gjchungsa.com/camp_mark/campmark.php";


        button_setting();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, serverUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Camp_mark_listup(response);
                    }

                    private void Camp_mark_listup(String response) {
                        Gson gson = new Gson();
                        camp_mark_list =new ArrayList<>();
                        Camp_mark[] camp_marks = gson.fromJson(response, Camp_mark[].class);
                        if (camp_marks != null) {
                            camp_mark_list.addAll(Arrays.asList(camp_marks));
                        }
                        Log.d("Camp_recommend_bbs", response);
                        page_num =1;
                        list_view_setting();
                        btn_page_number_setting();
                        list_setting();
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
                params.put("where", where);
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);


    }

    private void button_setting(){
        start_text = findViewById(R.id.camp_recommend_bbs_start_text);
        text_1 = findViewById(R.id.camp_recommend_bbs_1_text);
        text_2 = findViewById(R.id.camp_recommend_bbs_2_text);
        text_3 = findViewById(R.id.camp_recommend_bbs_3_text);
        text_4 = findViewById(R.id.camp_recommend_bbs_4_text);
        text_5 = findViewById(R.id.camp_recommend_bbs_5_text);
        last_text = findViewById(R.id.camp_recommend_bbs_last_text);

    }

    private void list_view_setting() {

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
                page_num = ((camp_mark_list.size()-1) / 10)+ 1;
                btn_page_number_setting();
                list_setting();
            }
        });

    }

    private void btn_page_number_setting() {
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
        if(page_num*10<camp_mark_list.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<camp_mark_list.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
    }

    private void list_setting(){        //list셋팅
        Camp_mark_bbs_Adapter adapter = new Camp_mark_bbs_Adapter();

        RecyclerView recyclerView = findViewById(R.id.camp_mark_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);

        layoutParams = recyclerView.getLayoutParams();
        layoutParams.height=0;

        for(int i=(page_num-1)*10 ; i<page_num*10 && i<camp_mark_list.size() ;i++){
            adapter.addItem(camp_mark_list.get(i));

            int newHeight = layoutParams.height + (int) (105 * getResources().getDisplayMetrics().density);
            layoutParams.height = newHeight;
            recyclerView.setLayoutParams(layoutParams);
        }

        recyclerView.setAdapter(adapter);
    }

    public void camp_recommend_bbs_insert_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Camp_mark_insert.class);
        intent.putExtra("insert_update", 2);
        startActivity(intent);
    }
}