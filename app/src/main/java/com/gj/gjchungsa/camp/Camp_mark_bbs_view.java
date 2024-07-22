package com.gj.gjchungsa.camp;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
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
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.gj.gjchungsa.camp.Camp_comment.Camp_comment;
import com.gj.gjchungsa.camp.Camp_comment.Camp_comment_Adapter;
import com.gj.gjchungsa.camp.edit.Camp_mark_insert;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_mark_bbs_view extends AppCompatActivity {

    Camp_mark camp_mark;
    ArrayList<Camp_comment> comment_list;
    private ViewPager2 sliderViewPager;
    private LinearLayout layoutIndicator;
    private static final String camp_image_url ="http://gjchungsa.com/camp_mark/camp_mark_images/";
    private static final String camp_comment_mark_select_url = "http://gjchungsa.com/camp_comment/camp_comment_mark_select.php";
    private String[] images_url;
    private int images_size=0;
    private int comment_view=0;

    final static String camp_comment_insert_url ="http://gjchungsa.com/camp_comment/camp_comment_insert.php";

    TextView camp_mark_title, camp_mark_user_name, camp_mark_where_detail, camp_mark_url,
            camp_mark_content;
    Button delete_btn;
    LinearLayout comment_plus_content_linear;
    Chungsa_user_InfoManager infoManager;
    RecyclerView recyclerView;

    EditText bbs_editTextText;
    Chungsa_user chungsa_user;
    int page_num=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_mark_bbs_view);

        Intent get_intent = getIntent();
        camp_mark = (Camp_mark) get_intent.getSerializableExtra("Camp_mark");

        infoManager = new Chungsa_user_InfoManager();   //로그인 정보
        chungsa_user = infoManager.getUserInfo();

        image_url_setting(camp_mark);   //Camp_mark의 url 옮기기
        textView_setting(camp_mark);

        delete_btn = findViewById(R.id.camp_mark_bbs_view_delete_btn);

        if(chungsa_user != null) {
            if (chungsa_user.getChungsa_user_email().equals(camp_mark.getChungsa_user_email()) ||    //삭제버튼
                    chungsa_user.getChungsa_user_grade().equals("최고관리자") ||
                    chungsa_user.getChungsa_user_grade().equals("중간관리자")) {
                delete_btn.setVisibility(View.VISIBLE);
            }
        }
        camp_mark_update_btn();     //수정버튼 셋팅

        if(images_size !=0){
            ConstraintLayout camp_mark_constraintLayout = findViewById(R.id.Camp_mark_constraintLayout);
            camp_mark_constraintLayout.setVisibility(View.VISIBLE);
        }

        sliderViewPager = findViewById(R.id.Camp_mark_ViewPager);
        layoutIndicator = findViewById(R.id.Camp_mark_layoutIndicators);
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

        comment_setting();

        IntentFilter filter = new IntentFilter("camp_comment");      //브로드 캐스트 수신
        registerReceiver(receiver, filter);

        comment_insert_setting();
    }

    private void comment_insert_setting() { //댓글 등록
        comment_plus_content_linear = findViewById(R.id.camp_mark_bbs_comment_plus_content_linear);
        bbs_editTextText = findViewById(R.id.camp_mark_bbs_editTextText);

        Button camp_mark_comment_insert = findViewById(R.id.camp_mark_comment_item_insert);

        camp_mark_comment_insert.setOnClickListener(new View.OnClickListener() {
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

            private void comment_insert_ing(String content) {   // 실행코드
                StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_comment_insert_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                // 서버로부터의 응답 처리
                                Log.d("Camp_comment_Adapter", "Insert 응답 : " + response);
                                comment_setting();
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.i("Camp_comment_Adapter", "camp_comment_insert Volley오류");
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("camp_comment_content", content);
                        params.put("Camp_mark_no", Integer.toString(camp_mark.getCamp_mark_no()));
                        params.put("chungsa_user_email", chungsa_user.getChungsa_user_email());
                        params.put("parent_camp_comment_no", "0");
                        return params;
                    }
                };

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });
    }


    private void comment_setting(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_comment_mark_select_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        commenet_list_setting(response);
                    }

                    private void commenet_list_setting(String response) {
                        Gson gson = new Gson();
                        comment_list =new ArrayList<>();
                        Camp_comment[] camp_comment = gson.fromJson(response, Camp_comment[].class);

                        if (camp_comment != null) {
                            comment_list.addAll(Arrays.asList(camp_comment));
                        }
                        comment_add();

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
                params.put("Camp_mark_no", Integer.toString(camp_mark.getCamp_mark_no()));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
    private void comment_add(){
        recyclerView = findViewById(R.id.camp_mark_bbs_view_recyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Camp_comment_Adapter adapter = new Camp_comment_Adapter();

        page_num =1;
        for(int i=0;i<5 && i< comment_list.size() ; i++){        //첫 화면 게시물 불러오기
            adapter.addItem(comment_list.get(i));
        }
        recyclerView.setAdapter(adapter);
        ImageButton left_button, right_button;
        left_button = findViewById(R.id.camp_mark_bbs_view_left_arrow_button);
        right_button = findViewById(R.id.camp_mark_bbs_view_right_arrow_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    Camp_comment_Adapter adapter = new Camp_comment_Adapter();

                    int page = (page_num-1)*5;
                    for(int i=page;i<page+5 && i< comment_list.size() ; i++){
                        adapter.addItem(comment_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*5 < comment_list.size()){
                    page_num++;
                    Camp_comment_Adapter adapter = new Camp_comment_Adapter();

                    int page = (page_num-1)*5;
                    for(int i=page;i<page+5 && i< comment_list.size() ; i++){
                        adapter.addItem(comment_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }

    private void textView_setting(Camp_mark camp_mark){ //TextView를 첫 셋팅
        camp_mark_title = findViewById(R.id.Camp_mark_bbs_view_title);
        camp_mark_user_name = findViewById(R.id.Camp_mark_bbs_view_user_name);
        camp_mark_where_detail = findViewById(R.id.Camp_mark_bbs_view_where_detail);
        camp_mark_url = findViewById(R.id.Camp_mark_bbs_view_link);
        camp_mark_content = findViewById(R.id.Camp_mark_bbs_view_content);

        camp_mark_title.setText(camp_mark.getCamp_mark_title());
        camp_mark_user_name.setText("작성자 : " + camp_mark.getChungsa_user_name());
        camp_mark_where_detail.setText("주소 : " + camp_mark.getCamp_mark_where_detail());
        camp_mark_url.setText(camp_mark.getCamp_mark_link());
        camp_mark_content.setText(camp_mark.getCamp_mark_content());


        camp_mark_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Uri uri = Uri.parse(camp_mark.getCamp_mark_link());
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                if(camp_mark.getCamp_mark_link().contains("instagram")){        //인스타일시
                    intent.setPackage("com.instagram.android"); // 인스타그램 앱을 사용하여 열기
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    try {
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        // 인스타그램 앱이 설치되어 있지 않은 경우, 웹 브라우저에서 열기
                        intent.setPackage(null); // 패키지 설정 제거
                        startActivity(intent);
                    }
                }
                else {  //아닐시
                    startActivity(intent);
                }

            }
        });
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
            layoutIndicator.addView(indicators[i]);
        }
        setCurrentIndicator(0);
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


    private void image_url_setting(Camp_mark camp_mark){        //url 가져오기

        //Toast.makeText(getApplicationContext(), camp_mark.getCamp_mark_image_url3(), Toast.LENGTH_SHORT).show();
        String image[] =new String[4];
        int size =0;

        if(!camp_mark.getCamp_mark_image_url1().equals("")){
            image[images_size] = camp_image_url + camp_mark.getCamp_mark_image_url1();
            images_size++;
        }
        if(!camp_mark.getCamp_mark_image_url2().equals("")){
            image[images_size] = camp_image_url + camp_mark.getCamp_mark_image_url2();
            images_size++;
        }
        if(!camp_mark.getCamp_mark_image_url3().equals("")){
            image[images_size] = camp_image_url + camp_mark.getCamp_mark_image_url3();
            images_size++;
        }
        if(!camp_mark.getCamp_mark_image_url4().equals("")){
            image[images_size] = camp_image_url + camp_mark.getCamp_mark_image_url4();
            images_size++;
        }
        images_url = new String[images_size];
        for(int i=0;i<images_size;i++)
            images_url[i] =image[i];
    }

    public void come_back(View view){   //돌아가기 버튼
        finish();
    }

    private void camp_mark_delete(Camp_mark camp_mark){    //삭제 코드
        String delete_url = "http://gjchungsa.com/camp_mark/camp_mark_delete.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Camp_mark_bbs_view_delete", "camp_mark_delete : " + response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Camp_mark_bbs_view_delete", "camp_mark_delete Volley오류");
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no", Integer.toString(camp_mark.getCamp_mark_no()));
                params.put("url1", camp_mark.getCamp_mark_image_url1());
                params.put("url2", camp_mark.getCamp_mark_image_url2());
                params.put("url3", camp_mark.getCamp_mark_image_url3());
                params.put("url4", camp_mark.getCamp_mark_image_url4());
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void camp_mark_update_btn(){
        if(chungsa_user !=null) {
            if (chungsa_user.getChungsa_user_email().equals(camp_mark.getChungsa_user_email()) ||
                    chungsa_user.getChungsa_user_grade().equals("최고관리자") ||
                    chungsa_user.getChungsa_user_grade().equals("중간관리자")) {
                Button button = findViewById(R.id.camp_mark_bbs_update_btn);
                button.setVisibility(View.VISIBLE);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), Camp_mark_insert.class);
                        intent.putExtra("insert_update", 1);
                        intent.putExtra("camp_mark", camp_mark);
                        startActivity(intent);
                    }
                });
            }
        }
    }

    public void camp_mark_comment_view(View view){      //댓글 보기, 댓글 닫기 버튼
        Button comment_view_btn = findViewById(R.id.camp_mark_bbs_view_comment_view_btn);
        LinearLayout comment_linaerLayout = findViewById(R.id.camp_mark_bbs_view_comment_linear);
        if(comment_view==0){
            comment_view=1;
            comment_view_btn.setText("댓글닫기");
            comment_linaerLayout.setVisibility(View.VISIBLE);
        }
        else if(comment_view==1){
            comment_view=0;
            comment_view_btn.setText("댓글보기");
            comment_linaerLayout.setVisibility(View.GONE);
        }

    }

    public void camp_mark_delete_btn1(View view) {  //삭제버튼
        new AlertDialog.Builder(this)
                .setMessage("정말로 삭제하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        camp_mark_delete(camp_mark);

                        Intent intent = new Intent(getApplicationContext(), Camp_recommend_list.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                        Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() { //댓글 업데이트시 다시 댓글 셋팅
        @Override
        public void onReceive(Context context, Intent intent) {
            if( "camp_comment".equals(intent.getAction())){
                comment_setting();
            }
        }
    };

}