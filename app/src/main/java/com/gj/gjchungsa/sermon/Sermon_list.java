package com.gj.gjchungsa.sermon;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.sermon.edit.Sermon_main;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_dawn_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_intro_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_other_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_search_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_series_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_shalom_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_special_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_sub_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_sunam_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_sunpm_fragment;
import com.gj.gjchungsa.sermon.sermon_fragment.Sermon_wed_fragment;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Sermon_list extends AppCompatActivity {

    ImageButton sermon_sunam_logo_button;
    TextView text_sermon_type;
    Button plus_sermon, sermon_edit_button;
    Chungsa_user_InfoManager chungsa_user_infoManager;
    public Chungsa_user user;
    int page_num;
    private static String url = "http://gjchungsa.com/preach_bbs.php";
    ArrayList<Preach_bbs> preach_bbs = new ArrayList<>();
    RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_list);

        requestQueue = Volley.newRequestQueue(getApplicationContext());

        sermon_edit_button= findViewById(R.id.sermon_edit_button);
        if(chungsa_user_infoManager.chungsa_user != null) {
            user = chungsa_user_infoManager.getUserInfo();  //로그인 정보가져오기
            if(user.getChungsa_user_grade().equals("최고관리자") || user.getChungsa_user_grade().equals("중간관리자")  ){  //편집 아이콘 보여주기 설정
                sermon_edit_button.setVisibility(View.VISIBLE);
            }
        }

        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                sermon_listup(response);
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
    private void sermon_listup(String response) {//json데이터 class로 바로 만들어 list로 넣기
        Gson gson = new Gson();
        preach_bbs =new ArrayList<>();
        Preach_bbs[] preaches = gson.fromJson(response, Preach_bbs[].class);
        if (preaches != null) {
            preach_bbs.addAll(Arrays.asList(preaches));
        }
        setting();

    }

    public void edit_go(View view){ //설정이동
        Intent intent = new Intent(getApplicationContext(), Sermon_main.class);
        intent.putExtra("preach_bbs",preach_bbs);
        startActivity(intent);
    }
    public void setting(){      //첫 셋팅
        ArrayList<Preach_bbs> sunam_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> sunpm_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> dawn_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> wed_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> shalom_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> sub_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> other_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> intro_preach =new ArrayList<Preach_bbs>();
        ArrayList<Preach_bbs> special_preach= new ArrayList<>();

        for(int i=0; i<preach_bbs.size();i++){
            if("세대통합예배".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                sunam_preach.add(preach_bbs.get(i));
            }
            else if("주일찬양예배".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                sunpm_preach.add(preach_bbs.get(i));
            }
            else if("새벽예배".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                dawn_preach.add(preach_bbs.get(i));
            }
            else if("수요예배".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                wed_preach.add(preach_bbs.get(i));
            } else if("샬롬마룻바닥기도회".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                shalom_preach.add(preach_bbs.get(i));
            } else if("부교역자설교".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                sub_preach.add(preach_bbs.get(i));
            } else if("외부강사".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                other_preach.add(preach_bbs.get(i));
            } else if("홍보영상".equals(preach_bbs.get(i).getBbs_b_catalogue())){
                intro_preach.add(preach_bbs.get(i));
            } else if("특별설교".equals(preach_bbs.get(i).getBbs_b_catalogue())) {
                special_preach.add(preach_bbs.get(i));
            }
        }

        Sermon_search_fragment fragment = new Sermon_search_fragment(preach_bbs);
        Sermon_series_fragment fragment0 = new Sermon_series_fragment();
        Sermon_sunam_fragment fragment1 = new Sermon_sunam_fragment(sunam_preach, "세대통합예배");
        Sermon_sunpm_fragment fragment2 = new Sermon_sunpm_fragment(sunpm_preach, "주일찬양예배");
        Sermon_dawn_fragment fragment3 = new Sermon_dawn_fragment(dawn_preach, "새벽예배");
        Sermon_wed_fragment fragment4 = new Sermon_wed_fragment(wed_preach, "수요예배");
        Sermon_shalom_fragment fragment5 = new Sermon_shalom_fragment(shalom_preach, "샬롬마룻바닥기도회");
        Sermon_sub_fragment fragment6 = new Sermon_sub_fragment(sub_preach, "부교역자설교");
        Sermon_other_fragment fragment7 = new Sermon_other_fragment(other_preach, "외부강사");
        Sermon_intro_fragment fragment8 = new Sermon_intro_fragment(intro_preach, "홍보영상");
        Sermon_special_fragment fragment9 = new Sermon_special_fragment(special_preach, "특별설교");

        getSupportFragmentManager().beginTransaction().replace(R.id.preach_container, fragment).commit();

        TabLayout tabs = findViewById(R.id.preach_tab);
        tabs.addTab(tabs.newTab().setText("검색하기"));
        tabs.addTab(tabs.newTab().setText("시리즈보기"));
        tabs.addTab(tabs.newTab().setText("세대통합예배"));
        tabs.addTab(tabs.newTab().setText("주일찬양예배"));
        tabs.addTab(tabs.newTab().setText("새벽예배"));
        tabs.addTab(tabs.newTab().setText("수요예배"));
        tabs.addTab(tabs.newTab().setText("샬롬마룻바닥기도회"));
        tabs.addTab(tabs.newTab().setText("부교역자설교"));
        tabs.addTab(tabs.newTab().setText("외부강사"));
        tabs.addTab(tabs.newTab().setText("홍보영상"));
        tabs.addTab(tabs.newTab().setText("특별설교"));


        tabs.selectTab(tabs.getTabAt(0));
        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Log.d("Sermon_list", "선택된 탭 : " + position);

                Fragment selected =null;
                if(position==0){
                    selected = fragment;
                }else if( position == 1){
                    selected = fragment0;
                }else if(position == 2){
                    selected = fragment1;
                }else if(position == 3){
                    selected = fragment2;
                }else if(position == 4){
                    selected = fragment3;
                }else if(position == 5){
                    selected = fragment4;
                }else if(position == 6){
                    selected = fragment5;
                }else if(position == 7){
                    selected = fragment6;
                }else if(position == 8){
                    selected = fragment7;
                }else if(position == 9){
                    selected = fragment8;
                }else if(position ==10){
                    selected = fragment9;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.preach_container, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
