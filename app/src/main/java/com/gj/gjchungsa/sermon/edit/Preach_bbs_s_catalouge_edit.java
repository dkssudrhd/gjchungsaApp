package com.gj.gjchungsa.sermon.edit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Preach_bbs_s_catalouge_edit extends AppCompatActivity {

    private static final String preach_catalogue_s_url ="http://gjchungsa.com/preach_bbs/preach_catalogue_s_select.php";

    ArrayList<Preach_catalogue_s> preach_catalogue_s = new ArrayList<>();
    ArrayList<Preach_catalogue_s> preach_catalogue_s_select = new ArrayList<>();

    ImageButton left_button, right_button;
    int page_num;
    Button insert_btn;
    RecyclerView recyclerView;
    Spinner spinner;
    Preach_catalouge_s_Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preach_bbs_scatalouge_edit);

        s_catalogue_setting();

        IntentFilter filter = new IntentFilter("catalouge_s");      //브로드 캐스트 수신
        registerReceiver(receiver, filter);

        findView_setting();
    }

    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
    private void list_setting() {
        page_num =1;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Preach_catalouge_s_Adapter();

        for(int i=0;i<10 && i< preach_catalogue_s_select.size() ; i++){//처음 10개 불러오기
            adapter.addItem(preach_catalogue_s_select.get(i));
        }
        recyclerView.setAdapter(adapter);

    }

    private void findView_setting() {
        spinner = findViewById(R.id.preach_bbs_scatalouge_spinner);
        recyclerView = findViewById(R.id.scatalouge_edit_recyclerView);
        insert_btn = findViewById(R.id.scatalouge_edit_insert_button);
        left_button = findViewById(R.id.scatalouge_edit_left_arrow_button);
        right_button = findViewById(R.id.scatalouge_edit_right_arrow_button);

        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Preach_catalogue_s_edit.class);
                startActivity(intent);
            }
        });
    }

    private void spinner_setting() {
        ArrayList<String> folderList_b = new ArrayList<>();

        folderList_b.add(0, "세대통합예배");
        folderList_b.add(1, "주일찬양예배");
        folderList_b.add(2, "새벽예배");
        folderList_b.add(3, "수요예배");
        folderList_b.add(4, "샬롬마룻바닥기도회");
        folderList_b.add(5, "부교역자설교");
        folderList_b.add(6, "외부강사");
        folderList_b.add(7, "홍보영상");
        folderList_b.add(8, "특별설교");

        spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList_b));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                String str =  folderList_b.get(position); // 선택된 항목 가져오기
                // 선택된 항목에 대한 작업을 여기에 수행합니다.
                //Toast.makeText(getApplicationContext(),b_spinner_str,Toast.LENGTH_SHORT).show();
                preach_catalogue_s_select  =  new ArrayList<>();
                for (int i=0 ; i< preach_catalogue_s.size();i++){
                    if(str.equals(preach_catalogue_s.get(i).getBbs_b_catalogue()))
                        preach_catalogue_s_select.add(preach_catalogue_s.get(i));
                }

                list_setting();
                left_right_btn_setting();
                //여기에 리스트 셋팅
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }

    private void left_right_btn_setting() {

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    adapter = new Preach_catalouge_s_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< preach_catalogue_s_select.size() ; i++){
                        adapter.addItem(preach_catalogue_s_select.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < preach_catalogue_s_select.size()){
                    page_num++;
                    adapter = new Preach_catalouge_s_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< preach_catalogue_s_select.size() ; i++){
                        adapter.addItem(preach_catalogue_s_select.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void s_catalogue_setting() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, preach_catalogue_s_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        series_sermonn_listup(response);
                    }
                    private void series_sermonn_listup(String response) { //리스트 받기
                        Gson gson = new Gson();
                        preach_catalogue_s =new ArrayList<>();
                        Preach_catalogue_s[] catalogue_s = gson.fromJson(response, Preach_catalogue_s[].class);
                        if (catalogue_s != null) {
                            preach_catalogue_s.addAll(Arrays.asList(catalogue_s));
                        }
                        spinner_setting();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Schedule_Adapter", "Schedule_Adapter_delete Volley오류");
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

    private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("catalouge_s".equals(intent.getAction())) {
                s_catalogue_setting();
            }
        }
    };

}