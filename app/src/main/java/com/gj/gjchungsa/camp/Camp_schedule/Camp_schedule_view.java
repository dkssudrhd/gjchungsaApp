package com.gj.gjchungsa.camp.Camp_schedule;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.ImageSliderAdapter;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule_comment.Camp_schedule_comment;
import com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule_comment.Camp_schedule_comment_Adapter;
import com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_schedule_insert;
import com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time.Camp_time;
import com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time.Camp_time_day_view_Adapter;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_schedule_view extends AppCompatActivity {

    ArrayList<Camp_time> camp_time_list;
    private ViewPager2 sliderViewPager;
    Camp_schedule get_camp_schedule;
    Chungsa_user_InfoManager infoManager;
    Chungsa_user chungsa_user;
    private String[] images_url;    //슬라이드 이미지 용
    int images_size =0, comment_page_num=0;             //이미지 갯수
    String camp_schedule_image_url ="http://gjchungsa.com/camp_schedule/camp_schedule_images/";
    private static final String camp_schedule_comment_select_url = "http://gjchungsa.com/camp_schedule/comment/camp_schedule_comment_select.php";
    final static String camp_schedule_comment_insert_url ="http://gjchungsa.com/camp_schedule/comment/camp_schedule_comment_insert.php";
    String camp_time_select ="http://gjchungsa.com/camp_schedule/camp_time_select.php";
    String camp_schedule_delete_url ="http://gjchungsa.com/camp_schedule/camp_schedule_delete.php";
    TextView title_text, user_name_text, how_much_text, where_text, type_text, when_text, people_text_b, people_text_s,
            what_family_text, family_name_text, content_text;
    LinearLayout comment_plus_content_linear, photo_linear, layoutIndicator;
    EditText bbs_editTextText;
    RecyclerView view_recyclerView, comment_recyclerView;
    LinearLayoutManager layoutManager;
    Camp_time_day_view_Adapter adapter;
    Button update_btn, delete_btn,  comment_insert_btn;
    ArrayList<Camp_schedule_comment> comment_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_schedule_view);

        Intent get_intent = getIntent();
        get_camp_schedule = (Camp_schedule) get_intent.getSerializableExtra("camp_schedule");

        infoManager = new Chungsa_user_InfoManager();   //로그인 정보
        chungsa_user = infoManager.getUserInfo();

        findView_setting();

        IntentFilter filter = new IntentFilter("camp_schedule_comment");      //브로드 캐스트 수신
        registerReceiver(receiver, filter);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    private void image_setting() {      //image 셋팅
        if(images_size>0){
            photo_linear.setVisibility(View.VISIBLE);

            sliderViewPager.setOffscreenPageLimit(1);
            sliderViewPager.setAdapter(new ImageSliderAdapter(this, images_url));

            sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentIndicator(position);
                }
            });

            setupIndicators(images_size);
        }
    }

    private void image_url_setting(Camp_schedule camp_schedule) {   //image 옮겨주기
        String image[] = new String[4];

        if(!camp_schedule.getCamp_schedule_image_url1().equals("")){
            image[images_size] = camp_schedule_image_url + camp_schedule.getCamp_schedule_image_url1();
            images_size++;
        }
        if(!camp_schedule.getCamp_schedule_image_url2().equals("")){
            image[images_size] = camp_schedule_image_url + camp_schedule.getCamp_schedule_image_url2();
            images_size++;
        }
        if(!camp_schedule.getCamp_schedule_image_url3().equals("")){
            image[images_size] = camp_schedule_image_url + camp_schedule.getCamp_schedule_image_url3();
            images_size++;
        }
        if(!camp_schedule.getCamp_schedule_image_url4().equals("")){
            image[images_size] = camp_schedule_image_url + camp_schedule.getCamp_schedule_image_url4();
            images_size++;
        }
        images_url = new String[images_size];
        for(int i=0;i<images_size;i++)
            images_url[i] =image[i];

        image_setting();

    }


    private void findView_setting(){            //findView 셋팅 그리고 바로 처음 셋팅
        title_text = findViewById(R.id.camp_schedule_view_title);
        user_name_text = findViewById(R.id.camp_schedule_view_user_name);
        how_much_text = findViewById(R.id.camp_schedule_view_how_much);
        where_text = findViewById(R.id.camp_schedule_view_where);
        type_text = findViewById(R.id.camp_schedule_view_type);
        when_text = findViewById(R.id.camp_schedule_view_when);
        people_text_b = findViewById(R.id.camp_schedule_view_people_b);
        people_text_s = findViewById(R.id.camp_schedule_view_people_s);
        what_family_text = findViewById(R.id.camp_schedule_view_what_family);
        family_name_text = findViewById(R.id.camp_schedule_view_family_name);
        content_text = findViewById(R.id.camp_schedule_view_content);
        photo_linear = findViewById(R.id.camp_schedule_view_photo_linear);
        sliderViewPager = findViewById(R.id.camp_schedule_view_ViewPager);
        layoutIndicator = findViewById(R.id.camp_schedule_layoutIndicators);
        view_recyclerView = findViewById(R.id.camp_schedule_view_recyclerView);
        update_btn = findViewById(R.id.camp_schedule_view_update_btn);
        delete_btn = findViewById(R.id.camp_schedule_view_delete_btn);
        comment_recyclerView = findViewById(R.id.camp_schedule_view_comment_recyclerView);
        comment_plus_content_linear = findViewById(R.id.camp_schedule_comment_plus_content_linear);
        bbs_editTextText = findViewById(R.id.camp_schedule_bbs_editTextText);
        comment_insert_btn = findViewById(R.id.camp_schedule_comment_item_insert);

        if(chungsa_user != null) {
            if (chungsa_user.getChungsa_user_email() == get_camp_schedule.getChungsa_user_email() ||
                    chungsa_user.getChungsa_user_grade().equals("최고관리자")) {
                update_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.VISIBLE);
            }
        }
        textview_setting();
        image_url_setting(get_camp_schedule);
        camp_time_list_setting();
        comment_setting();
        comment_insert_setting();
    }

    private void comment_insert_setting() {
        comment_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = bbs_editTextText.getText().toString();
                if(chungsa_user == null){
                    Toast.makeText(getApplicationContext(), "로그인을 먼저 해주세요." , Toast.LENGTH_SHORT).show();
                }
                else if(content.equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요" , Toast.LENGTH_SHORT).show();
                } else {
                    comment_insert_ing(content);
                    bbs_editTextText.setText("");
                }
            }

            private void comment_insert_ing(String content) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_comment_insert_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                // 서버로부터의 응답 처리
                                Log.d("Camp_schedule_comment_Adapter", "Insert 응답 : " + response);
                                comment_setting();
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.i("Camp_schedule_comment_Adapter", "camp_schedule_comment_insert Volley오류");
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("camp_schedule_comment_content", content);
                        params.put("camp_schedule_no", Integer.toString(get_camp_schedule.getCamp_schedule_no()));
                        params.put("chungsa_user_email", chungsa_user.getChungsa_user_email());
                        params.put("parent_camp_schedule_comment_no", "0");
                        return params;
                    }
                };

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });
    }

    private void comment_setting() {        //댓글 셋팅
        StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_comment_select_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        commenet_list_setting(response);
                    }

                    private void commenet_list_setting(String response) {
                        Gson gson = new Gson();
                        comment_list =new ArrayList<>();
                        Camp_schedule_comment[] camp_schedule_comments = gson.fromJson(response, Camp_schedule_comment[].class);

                        if (camp_schedule_comments != null) {
                            comment_list.addAll(Arrays.asList(camp_schedule_comments));
                        }
                        comment_view();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Log.i("Camp_recommend_bbs", "Camp_recommend_bbs Volley오류");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("camp_schedule_no", Integer.toString(get_camp_schedule.getCamp_schedule_no()));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
    private void comment_view(){        //댓글 셋팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        comment_recyclerView.setLayoutManager(layoutManager);
        Camp_schedule_comment_Adapter comment_adapter = new Camp_schedule_comment_Adapter();

        comment_page_num=1;
        for(int i=0;i<5 && i< comment_list.size() ; i++){        //첫 화면 게시물 불러오기
            comment_adapter.addItem(comment_list.get(i));
        }

        comment_recyclerView.setAdapter(comment_adapter);

        ImageButton left_button, right_button;
        left_button = findViewById(R.id.camp_schedule_view_left_arrow_button);
        right_button = findViewById(R.id.camp_schedule_view_right_arrow_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment_page_num > 1){
                    comment_page_num--;
                    Camp_schedule_comment_Adapter comment_adapter = new Camp_schedule_comment_Adapter();

                    int page = (comment_page_num-1)*5;
                    for(int i=page;i<page+5 && i< comment_list.size() ; i++){
                        comment_adapter.addItem(comment_list.get(i));
                    }
                    comment_recyclerView.setAdapter(comment_adapter);
                }
            }
        });

        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment_page_num*5 < comment_list.size()){
                    comment_page_num++;
                    Camp_schedule_comment_Adapter comment_adapter = new Camp_schedule_comment_Adapter();

                    int page = (comment_page_num-1)*5;
                    for(int i=page;i<page+5 && i< comment_list.size() ; i++){
                        comment_adapter.addItem(comment_list.get(i));
                    }
                    comment_recyclerView.setAdapter(comment_adapter);
                }
            }
        });
    }

    private void camp_time_list_setting(){        //캠프일정 셋팅 -> 먼저 일정 불러오기
        StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_time_select, // Camp_mark 가져오기
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("camp_time_list_view", response);

                        camp_time_listup(response);
                    }

                    private void camp_time_listup(String response) {
                        Gson gson = new Gson();
                        camp_time_list =new ArrayList<>();
                        Camp_time[] camp_times = gson.fromJson(response, Camp_time[].class);

                        if (camp_times != null) {
                            camp_time_list.addAll(Arrays.asList(camp_times));
                        }

                        camp_time_setting();
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
                params.put("camp_schedule_no", Integer.toString(get_camp_schedule.getCamp_schedule_no()));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void camp_time_setting(){       //리사이클뷰에 이제 넣기

        ArrayList<ArrayList<Camp_time>> camp_time_list_list = new ArrayList<>();

        int day_last_size =0;
        for(int i=0; i<camp_time_list.size(); i++){
            if(day_last_size < camp_time_list.get(i).getCamp_time_day()){
                day_last_size = camp_time_list.get(i).getCamp_time_day();
            }
            Log.d("camp_time_list_view", camp_time_list.get(i).getCamp_time_content());
        }
        ViewGroup.LayoutParams layoutParams;
        for(int i=0; i<=day_last_size; i++){
            ArrayList<Camp_time> add_list =new ArrayList<>();
            for(int j=0; j<camp_time_list.size(); j++){
                if(i == camp_time_list.get(j).getCamp_time_day()) {
                    add_list.add(camp_time_list.get(j));

//                    layoutParams = view_recyclerView.getLayoutParams();
//
//                    int newHeight = layoutParams.height + (int) (70 * getResources().getDisplayMetrics().density);
//                    layoutParams.height = newHeight;
//                    view_recyclerView.setLayoutParams(layoutParams);


                }
            }
            if(add_list.size()>0) {
                layoutParams = view_recyclerView.getLayoutParams();
                int newHeight = layoutParams.height + (int) (95 * getResources().getDisplayMetrics().density);
                layoutParams.height = newHeight;
                view_recyclerView.setLayoutParams(layoutParams);
            }
            if(add_list.size()>2){
//                Toast.makeText(getApplicationContext(), "증가",Toast.LENGTH_SHORT).show();
                layoutParams = view_recyclerView.getLayoutParams();
                int newHeight = layoutParams.height + (int) (45 * getResources().getDisplayMetrics().density);
                layoutParams.height = newHeight;
                view_recyclerView.setLayoutParams(layoutParams);
            }
            camp_time_list_list.add(add_list);
        }

        adapter = new Camp_time_day_view_Adapter();
        adapter.setItems(camp_time_list_list);
        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        view_recyclerView.setLayoutManager(layoutManager);
        view_recyclerView.setAdapter(adapter);

    }

    @SuppressLint("SetTextI18n")
    private void textview_setting(){    //textView 셋팅
        title_text.setText(get_camp_schedule.getCamp_schedule_title());
        String name = "작성자 : " + get_camp_schedule.getChungsa_user_name();
        user_name_text.setText(name);
//        SpannableString spannableString = new SpannableString(name);

        how_much_text.setText("금액 : " +get_camp_schedule.getCamp_schedule_how_much());
        where_text.setText("지역 : " +get_camp_schedule.getCamp_schedule_where());
        type_text.setText("기간 : "+get_camp_schedule.getCamp_schedule_type());
        when_text.setText("시작날짜 : " +get_camp_schedule.getCamp_schedule_when());
        people_text_b.setText(" " + Integer.toString(get_camp_schedule.getCamp_schedule_adult_no()) +
                "명 ");
        people_text_s.setText(" " + Integer.toString(get_camp_schedule.getCamp_schedule_kid_no()) +"명 ");
        what_family_text.setText("가정교회 : " + get_camp_schedule.getCamp_schedule_what_family());
        family_name_text.setText("양육사 : " + get_camp_schedule.getCamp_schedule_family_name());
        content_text.setText(get_camp_schedule.getCamp_schedule_content());
    }

    private void setCurrentIndicator(int position) {
        int childCount = layoutIndicator.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
            if (i == position) {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_active
                ));
            } else {
                imageView.setImageDrawable(ContextCompat.getDrawable(
                        this,
                        R.drawable.bg_indicator_inactive
                ));
            }
        }
    }
    private void setupIndicators(int count) {
        ImageView[] indicators = new ImageView[count];
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        params.setMargins(16, 8, 16, 8);

        for (int i = 0; i < indicators.length; i++) {
            indicators[i] = new ImageView(this);
            indicators[i].setImageDrawable(ContextCompat.getDrawable(this,
                    R.drawable.bg_indicator_inactive));
            indicators[i].setLayoutParams(params);
//            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
    }

    public void camp_schedule_view_come_back(View view) {   //돌아가기 버튼
        finish();
    }

    public void camp_schedule_comment_view(View view){      //댓글 보기, 댓글 닫기 버튼
        Button comment_view_btn = findViewById(R.id.camp_schedule_view_comment_view_btn);
        LinearLayout comment_linaerLayout = findViewById(R.id.camp_schedule_view_comment_linear);

        if(comment_linaerLayout.getVisibility()==View.GONE){
            comment_view_btn.setText("댓글닫기");
            comment_linaerLayout.setVisibility(View.VISIBLE);
        }
        else {
            comment_view_btn.setText("댓글보기");
            comment_linaerLayout.setVisibility(View.GONE);
        }

    }

    private BroadcastReceiver receiver = new BroadcastReceiver() { //댓글 업데이트시 다시 댓글 셋팅
        @Override
        public void onReceive(Context context, Intent intent) {
            if( "camp_schedule_comment".equals(intent.getAction())){
                comment_setting();
            }
        }
    };

    private void camp_schedule_delete_ing(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("camp_schedule_delete", "camp_schedule_delete 실행");

                        Intent intent =new Intent("camp_schedule_delete");
                        sendBroadcast(intent);
                        finish();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("camp_schedule_delete", "camp_schedule_delete Volley오류");
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no", Integer.toString(get_camp_schedule.getCamp_schedule_no()));
                return params;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }
    public void camp_schedule_delete_btn(View view){

        new AlertDialog.Builder(this)
                .setMessage("캠프 일정을 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        camp_schedule_delete_ing();
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }})
                .show();

    }

    public void camp_schedule_update_btn(View view){

        new AlertDialog.Builder(this)
                .setMessage("캠프 일정을 수정하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        Intent intent = new Intent(getApplicationContext(), Camp_schedule_insert.class);
                        intent.putExtra("situation", 1);
                        intent.putExtra("camp_schedule", get_camp_schedule);
                        intent.putExtra("camp_time_list", camp_time_list);
                        startActivity(intent);
                    }

                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }})
                .show();

    }

}

