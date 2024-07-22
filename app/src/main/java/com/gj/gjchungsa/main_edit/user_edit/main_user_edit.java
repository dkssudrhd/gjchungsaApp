package com.gj.gjchungsa.main_edit.user_edit;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class main_user_edit extends AppCompatActivity {

    public ArrayList<Chungsa_user> chungsa_user_list_all = new ArrayList<>();
    public ArrayList<Chungsa_user> chungsa_user_list = new ArrayList<>();

    ImageButton left_button, right_button;
    int page_num;
    EditText search_editText;
    Button search_button;
    RecyclerView recyclerView;
    RequestQueue requestQueue;
    LinearLayoutManager layoutManager;
    private static final String user_list_url = "http://gjchungsa.com/login/user_list.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_user_edit);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        search_editText = findViewById(R.id.main_user_edit_search_editText);
        search_button = findViewById(R.id.main_user_edit_search_button);
        recyclerView = findViewById(R.id.main_user_edit_recyclerView);


        list_all_upload();
    }


    private void list_all_upload(){
        StringRequest request = new StringRequest(Request.Method.GET, user_list_url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();


                Gson gson = new Gson();
                Chungsa_user[] chungsa_users = gson.fromJson(response, Chungsa_user[].class);
                if (chungsa_users != null) {
                    chungsa_user_list_all.addAll(Arrays.asList(chungsa_users));
                }

                chungsa_user_adapter_setting(); //첫 셋팅
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Volley에러", Toast.LENGTH_SHORT).show();
            }
        }
        ){
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                return params;
            }

        };

        request.setShouldCache(false);
        requestQueue.add(request);
    }
    private void chungsa_user_adapter_setting(){

        //chungsa_user_list = chungsa_user_list_all;

        list_setting(chungsa_user_list_all);
        left_button = findViewById(R.id.main_user_edit_left_arrow_button);
        right_button = findViewById(R.id.main_user_edit_right_arrow_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    Chungsa_user_edit_Adapter adapter = new Chungsa_user_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< chungsa_user_list.size() ; i++){
                        adapter.addItem(chungsa_user_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < chungsa_user_list.size()){
                    page_num++;
                    Chungsa_user_edit_Adapter adapter = new Chungsa_user_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< chungsa_user_list.size() ; i++){
                        adapter.addItem(chungsa_user_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });
    }

    private void list_setting(ArrayList<Chungsa_user> user_list){ //리스트 셋팅하기

        page_num =1;
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Chungsa_user_edit_Adapter adapter = new Chungsa_user_edit_Adapter();

        for(int i=0;i<10 && i< user_list.size() ; i++){//처음 10개 불러오기
            adapter.addItem(user_list.get(i));
        }
        recyclerView.setAdapter(adapter);


    }
    public void user_search_btn(View view){  //검색
        Toast.makeText(getApplicationContext(),"검색",Toast.LENGTH_SHORT).show();

        String keyword = search_editText.getText().toString();
        chungsa_user_list = new ArrayList<>();

        Toast.makeText(getApplicationContext(), keyword, Toast.LENGTH_SHORT).show();
        for(int i=0;i<chungsa_user_list_all.size();i++){

            Chungsa_user user = chungsa_user_list_all.get(i);

            if(user.getChungsa_user_name().contains(keyword) ||
                    user.getChungsa_user_email().contains(keyword) ||
                    user.getChungsa_user_nickname().contains(keyword) ||
                    user.getChungsa_user_grade().contains(keyword) ||
                    user.getChungsa_user_position().contains(keyword)){

                chungsa_user_list.add(user);
            }
        }

        list_setting(chungsa_user_list);

    }

}