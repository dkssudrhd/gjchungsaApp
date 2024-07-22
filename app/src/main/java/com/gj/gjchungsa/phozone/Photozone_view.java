package com.gj.gjchungsa.phozone;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.phozone.Photozone_comment.Photozone_comment;
import com.gj.gjchungsa.phozone.Photozone_comment.Photozone_comment_Adapter;
import com.gj.gjchungsa.phozone.edit.Photozone_insert;
import com.gj.gjchungsa.weekly.Weekly_image;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Photozone_view extends AppCompatActivity {

    Photozone now_photozone;
    int photozone_num, comment_page_num;
    String photozone_pick_select_url = "http://gjchungsa.com/photozone/photozone_pick_select.php";
    String photozone_image_select_url ="http://gjchungsa.com/photozone/photozone_image_select.php";
    String photozone_image_url = "http://gjchungsa.com/photozone/photozone_image/";
    String photozone_delete_url = "http://gjchungsa.com/photozone/photozone_delete.php";
    String photozone_image_delete_url = "http://gjchungsa.com/photozone/photozone_image_delete.php";
    private static final String photozone_comment_select_url = "http://gjchungsa.com/photozone/comment/photozone_comment_select.php";
    final static String photozone_comment_insert_url ="http://gjchungsa.com/photozone/comment/photozone_comment_insert.php";

    ArrayList<Photozone_image> photozone_image_list = new ArrayList<>();
    ImageView imageView[] = new ImageView[10];
    TextView title, content;
    Button update_btn, delete_btn, comment_insert_btn;
    LinearLayout comment_plus_content_linear;
    EditText comment_bbs_editTextText;
    Chungsa_user_InfoManager infoManager;
    Chungsa_user chungsa_user = null;
    RecyclerView comment_recyclerView;
    ArrayList<Photozone_comment> comment_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photozone_view);

        infoManager = new Chungsa_user_InfoManager();   //로그인 정보

        if(infoManager.chungsa_user != null) {
            chungsa_user = infoManager.getUserInfo();  //로그인 정보가져오기

        }

        Intent get_intent = getIntent();
        photozone_num = get_intent.getIntExtra("no", 0);

        IntentFilter filter = new IntentFilter("photozone_comment");      //브로드 캐스트 수신
        registerReceiver(receiver, filter);


        findviewSetting();


        get_photozone(photozone_num);
        get_photozone_image(photozone_num);

    }

    private void get_photozone_image(int photozone_num) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_image_select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Gson gson = new Gson();
                        Photozone_image[] photozone_images = gson.fromJson(response, Photozone_image[].class);

                        if (photozone_images != null) {
                            photozone_image_list.addAll(Arrays.asList(photozone_images));
                        }
                        photozone_image_setting();

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
                params.put("no", Integer.toString(photozone_num));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void photozone_image_setting() {
        for(int i=0; i<photozone_image_list.size(); i++){
            Photozone_image now = photozone_image_list.get(i);

            Glide.with(this)
                    .load(photozone_image_url + now.getPhotozone_image_url())
                    .placeholder(R.drawable.loading)
                    .into(imageView[i]);

            imageView[i].setVisibility(View.VISIBLE);

            imageView[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), Weekly_image.class);
                    intent.putExtra("image_res_id", photozone_image_url + now.getPhotozone_image_url()); // 확대하려는 이미지 리소스 ID로 변경
                    startActivity(intent);
                }
            });
        }
    }

    private void findviewSetting() {
        title = findViewById(R.id.photozone_view_title);
        content = findViewById(R.id.photozone_view_content);
        imageView[0] = findViewById(R.id.photozone_view_image1);
        imageView[1] = findViewById(R.id.photozone_view_image2);
        imageView[2] = findViewById(R.id.photozone_view_image3);
        imageView[3] = findViewById(R.id.photozone_view_image4);
        imageView[4] = findViewById(R.id.photozone_view_image5);
        imageView[5] = findViewById(R.id.photozone_view_image6);
        imageView[6] = findViewById(R.id.photozone_view_image7);
        imageView[7] = findViewById(R.id.photozone_view_image8);
        imageView[8] = findViewById(R.id.photozone_view_image9);
        imageView[9] = findViewById(R.id.photozone_view_image10);
        update_btn = findViewById(R.id.photozone_view_update_btn);
        delete_btn = findViewById(R.id.photozone_view_delete_btn);
        //댓글
        
        comment_plus_content_linear = findViewById(R.id.photozone_comment_plus_content_linear);
        comment_bbs_editTextText = findViewById(R.id.photozone_view_bbs_editTextText);
        comment_insert_btn = findViewById(R.id.photozone_view_comment_item_insert);
        comment_recyclerView = findViewById(R.id.photozone_view_comment_recyclerView);

        if(chungsa_user != null) {
            if (chungsa_user.getChungsa_user_grade().equals("최고관리자") || chungsa_user.getChungsa_user_email().equals(now_photozone.getChungsa_user_email())) {
                update_btn.setVisibility(View.VISIBLE);
                delete_btn.setVisibility(View.VISIBLE);
            }
        }
    }

    private void comment_setting() {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_comment_select_url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        commenet_list_setting(response);
                    }

                    private void commenet_list_setting(String response) {
                        Gson gson = new Gson();
                        comment_list =new ArrayList<>();
                        Photozone_comment[] photozone_comments = gson.fromJson(response, Photozone_comment[].class);

                        if (photozone_comments != null) {
                            comment_list.addAll(Arrays.asList(photozone_comments));
                        }
                        comment_view();
                        comment_insert_setting();
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
                params.put("photozone_no", Integer.toString(now_photozone.getPhotozone_no()));
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void comment_insert_setting() {
        comment_insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String content = comment_bbs_editTextText.getText().toString();
                if(chungsa_user == null){
                    Toast.makeText(getApplicationContext(), "로그인을 먼저 해주세요" , Toast.LENGTH_SHORT).show();
                }
                else if(content.equals("")){
                    Toast.makeText(getApplicationContext(), "내용을 입력해주세요" , Toast.LENGTH_SHORT).show();
                } else {
                    comment_insert_ing(content);
                }
            }

            private void comment_insert_ing(String content) {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_comment_insert_url,
                        new Response.Listener<String>() {

                            @Override
                            public void onResponse(String response) {
                                // 서버로부터의 응답 처리
                                Log.d("Photozone_comment_Adapter", "Insert 응답 : " + response);
                                comment_setting();
                                comment_bbs_editTextText.setText("");
                            }

                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                                Log.i("Photozone_comment_Adapter", "photozone_comment_insert Volley오류");
                            }
                        }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("photozone_comment_content", content);
                        params.put("photozone_no", Integer.toString(now_photozone.getPhotozone_no()));
                        params.put("chungsa_user_email", chungsa_user.getChungsa_user_email());
                        params.put("parent_photozone_comment_no", "0");
                        return params;
                    }
                };

                Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
            }
        });
    }

    private void comment_view(){        //댓글 셋팅
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        comment_recyclerView.setLayoutManager(layoutManager);
        Photozone_comment_Adapter comment_adapter = new Photozone_comment_Adapter();

        comment_page_num=1;
        for(int i=0;i<5 && i< comment_list.size() ; i++){        //첫 화면 게시물 불러오기
            comment_adapter.addItem(comment_list.get(i));
        }

        comment_recyclerView.setAdapter(comment_adapter);

        ImageButton left_button, right_button;
        left_button = findViewById(R.id.photozone_view_left_arrow_button);
        right_button = findViewById(R.id.photozone_view_right_arrow_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(comment_page_num > 1){
                    comment_page_num--;
                    Photozone_comment_Adapter comment_adapter = new Photozone_comment_Adapter();

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
                    Photozone_comment_Adapter comment_adapter = new Photozone_comment_Adapter();

                    int page = (comment_page_num-1)*5;
                    for(int i=page;i<page+5 && i< comment_list.size() ; i++){
                        comment_adapter.addItem(comment_list.get(i));
                    }
                    comment_recyclerView.setAdapter(comment_adapter);
                }
            }
        });
    }


    private void get_photozone(int photozone_num) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_pick_select_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Gson gson = new Gson();
                        Photozone[] photozones = gson.fromJson(response, Photozone[].class);
                        if (photozones != null) {
                            now_photozone = photozones[0];
                        }

                        title.setText(now_photozone.getPhotozone_title());
                        content.setText(now_photozone.getPhotozone_content());

                        if(chungsa_user !=null) {
                            if (chungsa_user.getChungsa_user_email().equals(now_photozone.getChungsa_user_email())) {
                                update_btn.setVisibility(View.VISIBLE);
                                delete_btn.setVisibility(View.VISIBLE);
                            }
                        }
                        comment_setting(); //댓글셋팅

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
                params.put("no", Integer.toString(photozone_num));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    public void photozone_view_delete_btn(View view){
        new AlertDialog.Builder(this)
                .setMessage("수정 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        delete_btn_ing();

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

    private void delete_btn_ing() { //데이터베이스 삭제
        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Photozone_delete", "camp_mark_delete : " + response);
                        delete_photozone_image();

                        Intent intent = new Intent(getApplicationContext(), Photozone_list.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
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
                params.put("no", Integer.toString(now_photozone.getPhotozone_no()));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void delete_photozone_image() {     //이미지 삭제
        for (int i = 0; i < photozone_image_list.size(); i++) {
            int num = i;
            StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_image_delete_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("Photozone_delete", "photozone_delete : " +Integer.toString(num) +"의 " + response);
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
                    params.put("url", photozone_image_list.get(num).getPhotozone_image_url());
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        }
    }

    public void photozone_view_update_btn(View view){           //수정
        new AlertDialog.Builder(this)
                .setMessage("수정 하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        update_btn_ing();

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

    private void update_btn_ing() {
        Intent intent = new Intent(getApplicationContext(), Photozone_insert.class);
        intent.putExtra("photozone", now_photozone);
        intent.putExtra("photozone_image_list", photozone_image_list);
        startActivity(intent);
    }

    public void photozone_comment_view(View view){      //댓글 보기, 댓글 닫기 버튼
        Button comment_view_btn = findViewById(R.id.photozone_view_comment_view_btn);
        LinearLayout comment_linaerLayout = findViewById(R.id.photozone_view_comment_linear);

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
            if( "photozone_comment".equals(intent.getAction())){
                comment_setting();
            }
        }
    };


}