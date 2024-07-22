package com.gj.gjchungsa.camp.Camp_schedule.edit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule;
import com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule_list;
import com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time.Camp_time;
import com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time.Camp_time_day_Adapter;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Camp_schedule_insert extends AppCompatActivity {

    ArrayList<Camp_time> now_camp_time_list =null;
    RequestBuilder<Drawable> requestBuilder1, requestBuilder2, requestBuilder3, requestBuilder4;
    RequestManager requestManager1, requestManager2, requestManager3, requestManager4;
    Camp_schedule now_camp_schedule =null;
    LinearLayout recycler_linear, photo_linear, content_linear;
    RecyclerView recyclerview;
    Camp_time_day_Adapter adapter;
    TextView title_type;
    Button camp_time_day_btn, camp_time_check_btn, time_insert_btn, photo_btn, insert_image1_btn,
            insert_image2_btn, insert_image3_btn, insert_image4_btn, content_insert_btn;
    ImageView insert_image1, insert_image2, insert_image3, insert_image4;
    LinearLayoutManager layoutManager;
    EditText title_editText, type_editText, where_editText, what_family_editText,when_editText
            , how_much_editText, family_name_editText, adult_no_editText, kid_no_editText, content_editText;
    String title_str, type_str, where_str, what_family_str, when_str, how_much_str, family_name_str, adult_no_str, kid_no_str, content_str;
    Bitmap bitmap1 =null, bitmap2=null, bitmap3=null, bitmap4=null;
    private String camp_schedule_insert_url ="http://gjchungsa.com/camp_schedule/camp_schedule_insert.php";
    private String camp_schedule_update_url ="http://gjchungsa.com/camp_schedule/camp_schedule_update.php";
    private String camp_time_insert_url = "http://gjchungsa.com/camp_schedule/camp_time_insert.php";
    private String camp_time_update_url = "http://gjchungsa.com/camp_schedule/camp_time_update.php";
    private String camp_schedule_image_url ="http://gjchungsa.com/camp_schedule/camp_schedule_images/";
    private JSONObject jsonObject;
    private JsonObjectRequest jsonObjectRequest;
    private ProgressDialog progressDialog;
    Chungsa_user_InfoManager infoManager; //로그인 확인
    Chungsa_user user;  //유저
    ViewGroup.LayoutParams layoutParams;
    int size =0;
    int situation = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_schedule_insert);

        Intent get_intent = getIntent();
        situation = get_intent.getIntExtra("situation", 0);
        now_camp_schedule = (Camp_schedule) get_intent.getSerializableExtra("camp_schedule");
        now_camp_time_list = (ArrayList<Camp_time>) get_intent.getSerializableExtra("camp_time_list");

        find_setting();     //find 셋팅 모음


        infoManager = new Chungsa_user_InfoManager();
        user = infoManager.getUserInfo();       //로그인 정보

        IntentFilter filter = new IntentFilter("camp_time_Adapter_insert_btn");      //브로드 캐스트 수신
        IntentFilter filter1 = new IntentFilter("camp_time_day_Adapter_checked_btn");
        registerReceiver(receiver, filter);
        registerReceiver(receiver1, filter1);

        recyclerview_setting();
        camp_time_day_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                layoutParams = recyclerview.getLayoutParams();

                int newHeight = layoutParams.height + (int) (108 * getResources().getDisplayMetrics().density);
                layoutParams.height = newHeight;

                recyclerview.setLayoutParams(layoutParams);

                ArrayList<Camp_time> camp_time_list_list = new ArrayList<>();
                adapter.addItem(camp_time_list_list);
                adapter.notifyItemChanged(adapter.getItemCount());

            }
        });


    }


    private void update_setting() {                 //update일시 셋팅
        title_editText.setText(now_camp_schedule.getCamp_schedule_title());
        type_editText.setText(now_camp_schedule.getCamp_schedule_type());
        where_editText.setText(now_camp_schedule.getCamp_schedule_where());
        what_family_editText.setText(now_camp_schedule.getCamp_schedule_what_family());
        when_editText.setText(now_camp_schedule.getCamp_schedule_when());
        how_much_editText.setText(now_camp_schedule.getCamp_schedule_how_much());
        family_name_editText.setText(now_camp_schedule.getCamp_schedule_family_name());
        adult_no_editText.setText(Integer.toString(now_camp_schedule.getCamp_schedule_adult_no()));
        kid_no_editText.setText(Integer.toString(now_camp_schedule.getCamp_schedule_kid_no()));
        content_editText.setText(now_camp_schedule.getCamp_schedule_content());
        title_type.setText("캠프 수정");


        if(!now_camp_schedule.getCamp_schedule_image_url1().equals("")){
            requestManager1 = Glide.with(this);
            requestBuilder1 = requestManager1.load(camp_schedule_image_url + now_camp_schedule.getCamp_schedule_image_url1())
                    .error(R.drawable.warning)
                    .placeholder(R.drawable.loading);
            requestBuilder1.into(insert_image1);
            insert_image1.setVisibility(View.VISIBLE);
        }
        if(!now_camp_schedule.getCamp_schedule_image_url2().equals("")){
            requestManager2 = Glide.with(this);
            requestBuilder2 = requestManager2.load(camp_schedule_image_url + now_camp_schedule.getCamp_schedule_image_url2())
                    .error(R.drawable.warning)
                    .placeholder(R.drawable.loading);
            requestBuilder2.into(insert_image2);
            insert_image2.setVisibility(View.VISIBLE);
        }
        if(!now_camp_schedule.getCamp_schedule_image_url3().equals("")){
            requestManager3 = Glide.with(this);
            requestBuilder3 = requestManager3.load(camp_schedule_image_url + now_camp_schedule.getCamp_schedule_image_url3())
                    .error(R.drawable.warning)
                    .placeholder(R.drawable.loading);
            requestBuilder3.into(insert_image3);
            insert_image3.setVisibility(View.VISIBLE);
        }
        if(!now_camp_schedule.getCamp_schedule_image_url4().equals("")){
            requestManager4 = Glide.with(this);
            requestBuilder4 = requestManager4.load(camp_schedule_image_url + now_camp_schedule.getCamp_schedule_image_url4())
                    .error(R.drawable.warning)
                    .placeholder(R.drawable.loading);
            requestBuilder4.into(insert_image4);
            insert_image4.setVisibility(View.VISIBLE);
        }

    }

    public void setCamp_time_check_btn(View view){
        title_str = title_editText.getText().toString();
        type_str = type_editText.getText().toString();
        where_str = where_editText.getText().toString();
        what_family_str = what_family_editText.getText().toString();
        when_str = when_editText.getText().toString();
        how_much_str = how_much_editText.getText().toString();
        family_name_str = family_name_editText.getText().toString();
        adult_no_str = adult_no_editText.getText().toString();
        kid_no_str = kid_no_editText.getText().toString();
        content_str = content_editText.getText().toString();

        if(title_str.equals(""))
            Toast.makeText(getApplicationContext(), "제목을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(type_str.equals(""))
            Toast.makeText(getApplicationContext(), "기간을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(where_str.equals(""))
            Toast.makeText(getApplicationContext(), "지역을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(what_family_str.equals(""))
            Toast.makeText(getApplicationContext(), "가정교회를 적어주세요", Toast.LENGTH_SHORT).show();
        else if(when_str.equals(""))
            Toast.makeText(getApplicationContext(), "출발날짜를 적어주세요", Toast.LENGTH_SHORT).show();
        else if(how_much_str.equals(""))
            Toast.makeText(getApplicationContext(), "총 금액을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(family_name_str.equals(""))
            Toast.makeText(getApplicationContext(), "양육사님의 이름을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(adult_no_str.equals(""))
            Toast.makeText(getApplicationContext(), "어른 인원을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(kid_no_str.equals(""))
            Toast.makeText(getApplicationContext(), "아이 인원을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(content_str.equals(""))
            Toast.makeText(getApplicationContext(), "설명을 적어주세요", Toast.LENGTH_SHORT).show();
        else if(situation ==1){
            new AlertDialog.Builder(this)
                    .setMessage("캠프 일정을 수정하시겠습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            camp_schedule_update();

                        }})
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 취소시 처리 로직
                            Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }})
                    .show();
        }

        else{

            new AlertDialog.Builder(Camp_schedule_insert.this)
                    .setMessage("캠프 일정을 올리시겠습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            camp_schedule_insert();

                        }})
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 취소시 처리 로직
                            Toast.makeText(getApplicationContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                        }})
                    .show();

        }
    }
    private void camp_schedule_update(){
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("캠프를 수정 중입니다...");

        progressDialog.show();
        String image1 ="";
        String url_name1="";
        String image2 ="";
        String url_name2="";
        String image3 ="";
        String url_name3="";
        String image4 ="";
        String url_name4="";

        if(bitmap1 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name1 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap2 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image2 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap3 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap3.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image3 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name3 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap4 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap4.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image4 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name4 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }

        try {
            jsonObject = new JSONObject();
            jsonObject.put("no", now_camp_schedule.getCamp_schedule_no());
            jsonObject.put("title", title_str);
            jsonObject.put("when", when_str);
            jsonObject.put("type", type_str);
            jsonObject.put("where", where_str);
            jsonObject.put("adult_no", adult_no_str);
            jsonObject.put("kid_no", kid_no_str);
            jsonObject.put("email", user.getChungsa_user_email());
            jsonObject.put("what_family", what_family_str);
            jsonObject.put("family_name", family_name_str);
            jsonObject.put("how_much", how_much_str);
            jsonObject.put("image1", image1);
            jsonObject.put("image2", image2);
            jsonObject.put("image3", image3);
            jsonObject.put("image4", image4);
            jsonObject.put("url1", url_name1);
            jsonObject.put("url2", url_name2);
            jsonObject.put("url3", url_name3);
            jsonObject.put("url4", url_name4);
            jsonObject.put("content", content_str);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, camp_schedule_update_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("Camp_mark_insert_update_volley", response.toString());
                            Log.d("Camp_mark_insert_update_volley", Integer.toString(now_camp_schedule.getCamp_schedule_no()));

                            camp_time_insert_or_update(now_camp_schedule.getCamp_schedule_no());

                            Intent intent = new Intent(getApplicationContext(), Camp_schedule_list.class); // 남길 액티비티
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("Camp_mark_insert_insert_volley", error.toString());
                    progressDialog.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }
    private void camp_schedule_insert(){    //insert문은 여기로
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("캠프를 업로드 중입니다...");

        progressDialog.show();
        String image1 ="";
        String url_name1="";
        String image2 ="";
        String url_name2="";
        String image3 ="";
        String url_name3="";
        String image4 ="";
        String url_name4="";

        if(bitmap1 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image1 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name1 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap2 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image2 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name2 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap3 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap3.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image3 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name3 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }
        if(bitmap4 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap4.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image4 = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name4 = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }

        try {
            jsonObject = new JSONObject();
            jsonObject.put("title", title_str);
            jsonObject.put("when", when_str);
            jsonObject.put("type", type_str);
            jsonObject.put("where", where_str);
            jsonObject.put("adult_no", adult_no_str);
            jsonObject.put("kid_no", kid_no_str);
            jsonObject.put("email", user.getChungsa_user_email());
            jsonObject.put("what_family", what_family_str);
            jsonObject.put("family_name", family_name_str);
            jsonObject.put("how_much", how_much_str);
            jsonObject.put("image1", image1);
            jsonObject.put("image2", image2);
            jsonObject.put("image3", image3);
            jsonObject.put("image4", image4);
            jsonObject.put("url1", url_name1);
            jsonObject.put("url2", url_name2);
            jsonObject.put("url3", url_name3);
            jsonObject.put("url4", url_name4);
            jsonObject.put("content", content_str);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, camp_schedule_insert_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("Camp_mark_insert_insert_volley", response.toString());
                            try {       //여기서 camp_schedule_no까지 가져옴
                                camp_time_insert_or_update(Integer.parseInt(response.getJSONObject("arr").getString("camp_schedule_no")));


                                Intent intent = new Intent(getApplicationContext(), Camp_schedule_list.class); // 남길 액티비티
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);

                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "실패하였습니다.", Toast.LENGTH_SHORT).show();
                    Log.d("Camp_mark_insert_insert_volley", error.toString());
                    progressDialog.dismiss();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void camp_time_insert_or_update(int camp_schedule_no){
        ArrayList<ArrayList<Camp_time>> get_camp_time = adapter.getItems();

        ArrayList<Camp_time> insert_camp_time = new ArrayList<>();      //추가할 것
        ArrayList<Camp_time> update_camp_time = new ArrayList<>();      //수정할 것

        for(int i=0; i<get_camp_time.size();i++){
            ArrayList<Camp_time> camp_time_list = get_camp_time.get(i);
            for(int j=0; j<camp_time_list.size() ; j++){
                Camp_time now_camp_time = camp_time_list.get(j);

                if(now_camp_time.getCamp_time_situation().equals("insert"))
                    insert_camp_time.add(now_camp_time);
                else if(now_camp_time.getCamp_time_situation().equals("update"))
                    update_camp_time.add(now_camp_time);
            }
        }

        camp_time_insert_ing(insert_camp_time, camp_schedule_no);
        camp_time_update_ing(update_camp_time, camp_schedule_no);
    }

    private void camp_time_update_ing(ArrayList<Camp_time> update_camp_time, int camp_schedule_no) {
        for(int i=0; i<update_camp_time.size();i++){
            Camp_time camp_time = update_camp_time.get(i);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_time_update_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("camp_time_insert", "Insert 응답 : " + response);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("camp_time_insert", "camp_comment_insert Volley오류");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("no", Integer.toString(camp_time.getCamp_time_no()));
                    params.put("content", camp_time.getCamp_time_content() );
                    params.put("camp_schedule_no", Integer.toString(camp_schedule_no));
                    params.put("camp_mark_no", Integer.toString(camp_time.getCamp_mark_no()));
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        }

    }

    private void camp_time_insert_ing(ArrayList<Camp_time> insert_camp_time, int camp_schedule_no){ // insert 진행사항

        for(int i=0; i<insert_camp_time.size();i++){
            Camp_time camp_time = insert_camp_time.get(i);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_time_insert_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("camp_time_insert", "Insert 응답 : " + response);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("camp_time_insert", "camp_comment_insert Volley오류");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("content", camp_time.getCamp_time_content() );
                    params.put("camp_schedule_no", Integer.toString(camp_schedule_no));
                    params.put("camp_mark_no", Integer.toString(camp_time.getCamp_mark_no()));
                    params.put("day", Integer.toString(camp_time.getCamp_time_day()));
                    params.put("day_play", Integer.toString(camp_time.getCamp_time_day_play()));
                    return params;
                }
            };

            Volley.newRequestQueue(this).add(stringRequest);
        }
    }
    private void find_setting(){    //findViewById 셋팅
        camp_time_day_btn = findViewById(R.id.camp_time_day_btn);
        camp_time_check_btn = findViewById(R.id.camp_time_check_btn);
        title_editText = findViewById(R.id.camp_schedule_title_editText);
        type_editText = findViewById(R.id.camp_schedule_type_editText);
        where_editText = findViewById(R.id.camp_schedule_where_editText);
        what_family_editText = findViewById(R.id.camp_schedule_what_family_editText);
        when_editText = findViewById(R.id.camp_schedule_when_editText);
        how_much_editText = findViewById(R.id.camp_schedule_how_much_editText);
        family_name_editText = findViewById(R.id.camp_schedule_family_name_editText);
        adult_no_editText = findViewById(R.id.camp_schedule_adult_no_editText);
        kid_no_editText = findViewById(R.id.camp_schedule_kid_no_editText);
        recycler_linear = findViewById(R.id.camp_schedule_recycler_linear);
        time_insert_btn = findViewById(R.id.camp_schedule_time_insert_btn);
        photo_btn = findViewById(R.id.camp_schedule_photo_btn);
        photo_linear = findViewById(R.id.camp_schedule_photo_linear);
        insert_image1_btn = findViewById(R.id.camp_schedule_insert_image1_btn);
        insert_image2_btn = findViewById(R.id.camp_schedule_insert_image2_btn);
        insert_image3_btn = findViewById(R.id.camp_schedule_insert_image3_btn);
        insert_image4_btn = findViewById(R.id.camp_schedule_insert_image4_btn);
        insert_image1 = findViewById(R.id.camp_schedule_insert_image1);
        insert_image2 = findViewById(R.id.camp_schedule_insert_image2);
        insert_image3 = findViewById(R.id.camp_schedule_insert_image3);
        insert_image4 = findViewById(R.id.camp_schedule_insert_image4);
        content_linear = findViewById(R.id.camp_schedule_content_linear);
        content_editText = findViewById(R.id.camp_schedule_content_editText);
        content_insert_btn = findViewById(R.id.camp_schedule_content_insert_btn);
        title_type = findViewById(R.id.camp_schedule_insert_title_type);

        if(now_camp_schedule!=null) {
            update_setting();
        }

    }

    private void recyclerview_setting(){        //recylerview 셋팅
        recyclerview = findViewById(R.id.camp_time_recyclerview);

        layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerview.setLayoutManager(layoutManager);

        adapter = new Camp_time_day_Adapter();

        if(now_camp_time_list !=null){
            int best =0;
            for(int i =0 ; i<now_camp_time_list.size();i++){
                Log.d("now_camp_time_list_check", Integer.toString(now_camp_time_list.get(i).getCamp_time_day())
                        + "일 " + Integer.toString(now_camp_time_list.get(i).getCamp_time_day_play()) +"횟수");

                if(now_camp_time_list.get(i).getCamp_time_day() > best) {
                    best = now_camp_time_list.get(i).getCamp_time_day();
                }
            }

            layoutParams = recyclerview.getLayoutParams();

            int newHeight = layoutParams.height + (int) (108 * (best+1) * getResources().getDisplayMetrics().density);
            layoutParams.height = newHeight;

            recyclerview.setLayoutParams(layoutParams);


            ArrayList<Camp_time> update_set_list;

            int best_day[] = new int [best+1];
            for(int i=0; i<=best; i++)
                best_day[i] =0;

            for(int i=0; i <= best; i++){
                for ( int j=0; j<now_camp_time_list.size(); j++){
                    if(now_camp_time_list.get(j).getCamp_time_day() == i && best_day[i] < now_camp_time_list.get(j).getCamp_time_day_play()) {
                        best_day[i] = now_camp_time_list.get(j).getCamp_time_day_play();
                    }
                }
            }

            for(int i =0; i<=best ;i++) {
                update_set_list = new ArrayList<>();

                update_set_list.add(new Camp_time(i, 0));
                Log.d("now_camp_time_list_check", Integer.toString(i)
                        + "의 0번째 만들기");

                for(int j=1;j<=best_day[i];j++) {
                    update_set_list.add(new Camp_time(i, j));
                    Log.d("now_camp_time_list_check", Integer.toString(i)
                            + "의 " + Integer.toString(j) +"번째 만들기");
                }

                layoutParams = recyclerview.getLayoutParams();

                newHeight = layoutParams.height + (int) (208 * (best_day[i]+1) * getResources().getDisplayMetrics().density);
                layoutParams.height = newHeight;

                recyclerview.setLayoutParams(layoutParams);

                for(int j=0;j<now_camp_time_list.size();j++){
                    if(now_camp_time_list.get(j).getCamp_time_day() == i){
                        update_set_list.set(now_camp_time_list.get(j).getCamp_time_day_play(), now_camp_time_list.get(j));
                        Log.d("now_camp_time_list_check", Integer.toString(now_camp_time_list.get(j).getCamp_time_day())
                                + "일 " + Integer.toString(now_camp_time_list.get(j).getCamp_time_day_play()) +"횟수 업데이트");

                    }
                }
                adapter.addItem(update_set_list);
            }
        }
        recyclerview.setAdapter(adapter);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    try {
                        if(requestManager1 != null)
                            requestManager1.clear(insert_image1);
                        Uri imageUri = data.getData();
                        bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        insert_image1.setImageBitmap(bitmap1);
                        insert_image1.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case 2: {
                if (resultCode == RESULT_OK) {
                    try {
                        if(requestManager2 != null)
                            requestManager2.clear(insert_image2);
                        Uri imageUri = data.getData();
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        insert_image2.setImageBitmap(bitmap2);
                        insert_image2.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case 3: {
                if (resultCode == RESULT_OK) {
                    try {
                        if(requestManager3 != null)
                            requestManager3.clear(insert_image3);
                        Uri imageUri = data.getData();
                        bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        insert_image3.setImageBitmap(bitmap3);
                        insert_image3.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
            case 4: {
                if (resultCode == RESULT_OK) {
                    try {
                        if(requestManager4 != null)
                            requestManager4.clear(insert_image4);
                        Uri imageUri = data.getData();
                        bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        insert_image4.setImageBitmap(bitmap4);
                        insert_image4.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    public void Setinsert_image1_btn(View view){        //사진 삽입1
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 1);
    }
    public void Setinsert_image2_btn(View view){        //사진 삽입2
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 2);
    }
    public void Setinsert_image3_btn(View view){        //사진 삽입3
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 3);
    }
    public void Setinsert_image4_btn(View view){        //사진 삽입4
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 4);
    }
    public void photo_insert_btn(View view){
        if(photo_linear.getVisibility() == View.GONE){
            photo_btn.setText("사진 숨기기");
            photo_linear.setVisibility(View.VISIBLE);
        } else {
            photo_btn.setText("사진 넣기");
            photo_linear.setVisibility(View.GONE);
        }
    }

    public void time_insert_btn(View view){ //시간표 넣기
        if(recycler_linear.getVisibility() == View.GONE){
            time_insert_btn.setText("시간표 숨기기");
            recycler_linear.setVisibility(View.VISIBLE);
        } else {
            time_insert_btn.setText("시간표 넣기");
            recycler_linear.setVisibility(View.GONE);
        }

    }
    public void content_insert_btn(View view){
        if(content_linear.getVisibility() == View.GONE){
            content_insert_btn.setText("내용 숨기기");
            content_linear.setVisibility(View.VISIBLE);
        } else {
            content_insert_btn.setText("내용 넣기");
            content_linear.setVisibility(View.GONE);
        }

    }
    private BroadcastReceiver receiver1 = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("camp_time_day_Adapter_checked_btn".equals(intent.getAction())) {
                Log.d("broadcast_gogo" , "camp_time_day_Adapter_checked_btn 받음, Camp_schedule_insert 에서 받음");
                ArrayList<Camp_time> camp_time_list = new ArrayList<>();
                camp_time_list = (ArrayList<Camp_time>) intent.getSerializableExtra("camp_time_list");

                for(int i=0; i<camp_time_list.size();i++){
                    Log.d("broadcast_gogo" , "캠프 " + camp_time_list.get(i).getCamp_time_content());

                }

                int day = intent.getIntExtra("day", 0);
                adapter.setItem(camp_time_list, day);   //adapter 로 다 받음

                for(int i=0; i<adapter.getItemCount();i++){
                    ArrayList<Camp_time> list = adapter.getItem(i);
                }
//                unregisterReceiver(receiver1);

            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();

        unregisterReceiver(receiver);
        unregisterReceiver(receiver1);
        if (adapter != null) {
            adapter.unregisterReceivers(); // 어댑터에서 리시버 해제하는 메서드를 추가해야 합니다.
        }
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

        @Override
        public void onReceive(Context context, Intent intent) {
            if ("camp_time_Adapter_insert_btn".equals(intent.getAction())) {

                Camp_time new_camp_time = (Camp_time) intent.getSerializableExtra("camp_time");

                Log.d("broadcast_gogo" , "camp_time_Adapter_insert_btn 받음, Camp_schedule_insert 에서 받음");
                adapter.getItem(new_camp_time.getCamp_time_day()).add(new_camp_time);
//                ArrayList<Camp_time> list = adapter.getItem(new_camp_time.getCamp_time_day());
//                list.add(new_camp_time);
//                adapter.setItem(list, new_camp_time.getCamp_time_day());

                ViewGroup.LayoutParams layoutParams = recyclerview.getLayoutParams();

                int newHeight = layoutParams.height + (int) (208 * getResources().getDisplayMetrics().density);

                layoutParams.height = newHeight;
                recyclerview.setLayoutParams(layoutParams);
            }

        }
    };
}