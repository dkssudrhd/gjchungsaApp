
package com.gj.gjchungsa.phozone;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.phozone.edit.Photozone_insert;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Photozone_list extends AppCompatActivity {

    String photozone_select_url = "http://gjchungsa.com/photozone/photozone_select.php";
    RecyclerView list_recyclerView;
    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;
    ArrayList<Photozone> photozone_list = new ArrayList<>();
    Photozone_Adapter adapter;
    public int page_num;
    Button go_insert;
    Chungsa_user_InfoManager infoManager;
    Chungsa_user chungsa_user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photozone_list);

        photozone_list_setting();
        go_insert = findViewById(R.id.photozone_list_go_insert);
        infoManager = new Chungsa_user_InfoManager();   //로그인 정보
        chungsa_user = infoManager.getUserInfo();

        if(chungsa_user != null){
            if(chungsa_user.getChungsa_user_grade().equals("일반관리자") ||
                    chungsa_user.getChungsa_user_grade().equals("중간관리자") ||
                    chungsa_user.getChungsa_user_grade().equals("최고관리자")
            ) {
                go_insert.setVisibility(View.VISIBLE);
            }
        }
        go_insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Photozone_insert.class);
                startActivity(intent);
            }
        });
    }

    private void photozone_list_setting(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Gson gson = new Gson();
                        photozone_list =new ArrayList<>();
                        Photozone[] photozones = gson.fromJson(response, Photozone[].class);

                        if (photozones != null) {
                            photozone_list.addAll(Arrays.asList(photozones));
                        }

                        findView_setting();
                        page_num =1;

                        btn_page_number_setting();
                        list_setting();
                        list_view_setting();
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

    private void findView_setting() {
        list_recyclerView = findViewById(R.id.photozone_list_recyclerView);
        start_text = findViewById(R.id.photozone_list_start_text);
        text_1 = findViewById(R.id.photozone_list_1_text);
        text_2 = findViewById(R.id.photozone_list_2_text);
        text_3 = findViewById(R.id.photozone_list_3_text);
        text_4 = findViewById(R.id.photozone_list_4_text);
        text_5 = findViewById(R.id.photozone_list_5_text);
        last_text = findViewById(R.id.photozone_list_last_text);


        GridLayoutManager layoutManager = new GridLayoutManager(getApplicationContext(), 2);
        list_recyclerView.setLayoutManager(layoutManager);

        adapter = new Photozone_Adapter();

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
                page_num = ((photozone_list.size()-1) / 10)+ 1;
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
        if(page_num*10<photozone_list.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<photozone_list.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
    }

    private void list_setting(){        //list셋팅
        adapter = new Photozone_Adapter();
        for(int i=(page_num-1)*10 ; i<page_num*10 && i<photozone_list.size() ;i++){
            adapter.addItem(photozone_list.get(i));
        }
        list_recyclerView.setAdapter(adapter);
    }


}