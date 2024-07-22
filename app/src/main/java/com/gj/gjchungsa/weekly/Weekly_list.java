package com.gj.gjchungsa.weekly;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.weekly.edit.Weekly_edit_main;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Weekly_list extends AppCompatActivity {
    ViewGroup.LayoutParams layoutParams;
    RecyclerView recyclerView;
    public static ArrayList<Weekly> weekly_list = new ArrayList<>();
    RequestQueue requestQueue;
    private static String weekly_url = "http://gjchungsa.com/weekly/weeklylist.php";
    public int page_num;
    Chungsa_user_InfoManager chungsa_user_infoManager;
    public Chungsa_user user;
    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_list);

        if(chungsa_user_infoManager.chungsa_user != null) {
            user = chungsa_user_infoManager.getUserInfo();  //로그인 정보가져오기
            Button edit_button = findViewById(R.id.weekly_list_edit_button);
            if(user.getChungsa_user_grade().equals("최고관리자") || user.getChungsa_user_grade().equals("중간관리자")  ){  //편집 아이콘 보여주기 설정
                edit_button.setVisibility(View.VISIBLE);
            }
        }

        page_num = 1;
        weekly_list_upload();

        button_setting();
    }

    private void button_setting() {
        start_text = findViewById(R.id.weekly_list_start_text);
        text_1 = findViewById(R.id.weekly_list_1_text);
        text_2 = findViewById(R.id.weekly_list_2_text);
        text_3 = findViewById(R.id.weekly_list_3_text);
        text_4 = findViewById(R.id.weekly_list_4_text);
        text_5 = findViewById(R.id.weekly_list_5_text);
        last_text = findViewById(R.id.weekly_list_last_text);

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
                page_num = ((weekly_list.size()-1) / 10)+ 1;
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
        if(page_num*10<weekly_list.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<weekly_list.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
    }
    private void list_setting(){        //list셋팅
        Weekly_Adapter adapter = new Weekly_Adapter();

        layoutParams.height =0;
        for(int i=(page_num-1)*10 ; i<page_num*10 && i<weekly_list.size() ;i++){
            adapter.addItem(weekly_list.get(i));
            layoutParams = recyclerView.getLayoutParams();

            int newHeight = layoutParams.height + (int) (75 * getResources().getDisplayMetrics().density);
            layoutParams.height = newHeight;
            recyclerView.setLayoutParams(layoutParams);


        }

        recyclerView.setAdapter(adapter);
    }

    public void weekly_list_setting(){
        recyclerView = findViewById(R.id.weekly_recyclerView);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Weekly_Adapter weekly_adapter = new Weekly_Adapter();

        page_num=1;
        for(int i=0;i<10 && i< weekly_list.size() ; i++){//처음 10개 불러오기
            weekly_adapter.addItem(weekly_list.get(i));

            layoutParams = recyclerView.getLayoutParams();

            int newHeight = layoutParams.height + (int) (75 * getResources().getDisplayMetrics().density);
            layoutParams.height = newHeight;
            recyclerView.setLayoutParams(layoutParams);

        }
        recyclerView.setAdapter(weekly_adapter);
        btn_page_number_setting();

    }

    public void weekly_list_upload(){   //weekly_list 불러오기
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        StringRequest request = new StringRequest(Request.Method.GET, weekly_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                weekly_listup(response);
            }

            private void weekly_listup(String response) {
                Gson gson = new Gson();
                weekly_list =new ArrayList<>();
                Weekly[] weekly = gson.fromJson(response, Weekly[].class);
                if (weekly != null) {
                    weekly_list.addAll(Arrays.asList(weekly));
                }
                weekly_list_setting();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(getApplicationContext(), "Volley에러", Toast.LENGTH_SHORT).show();
                Log.e("VolleyError", error.getMessage());
                //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }

        }
        ) {
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }

    public void weekly_edit_go(View view) {
        Intent intent = new Intent(getApplicationContext(), Weekly_edit_main.class);
        intent.putExtra("weekly",weekly_list);
        startActivity(intent);
    }
}