package com.gj.gjchungsa.camp.edit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_mark;
import com.gj.gjchungsa.camp.Camp_recommend_list;
import com.gj.gjchungsa.camp.ResultSerachKeyword.Camp_mark_Place;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Camp_mark_insert extends AppCompatActivity {

    private static final String BASE_URL = "https://dapi.kakao.com/";
    private static final String API_KEY = "KakaoAK f815506076c582415708f0c8c20cc244"; // REST API 키
    String camp_mark_images_url ="http://gjchungsa.com/camp_mark/camp_mark_images/";
    String camp_mark_update_url ="http://gjchungsa.com/camp_mark/camp_mark_update.php";
    String camp_mark_insert_url ="http://gjchungsa.com/camp_mark/camp_mark_insert.php";
    ArrayList<Camp_mark_Place> places = new ArrayList<>();
    int insert_update;
    Camp_mark_Place place;
    Camp_mark mark;
    TextView insert_update_TextView,where_detail;
    EditText title_editText, link_editText, phone_editText, content_EditText;

    ImageView imageview1, imageview2,imageview3, imageview4;
    Spinner type_spinner, where_spinner;
    String type_spinner_str, where_spinner_str, where_detail_str, title_str, content_str, link_str, phone_str;
    ArrayList<String> type = new ArrayList<>();
    ArrayList<String> where = new ArrayList<>();
    String x="", y="";
    RequestBuilder<Drawable> requestBuilder1, requestBuilder2, requestBuilder3, requestBuilder4;
    RequestManager requestManager1, requestManager2, requestManager3, requestManager4;
    Bitmap bitmap1, bitmap2, bitmap3, bitmap4;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private JsonObjectRequest jsonObjectRequest;
    public RequestQueue requestQueue;
    Chungsa_user_InfoManager infoManager; //로그인 확인
    Chungsa_user user;  //유저

    //검색
    EditText search_edit;
    Button search_btn;
    LinearLayout search_linear, selected_linear;
    PopupMenu popupMenu;
    MenuItem search[] = new MenuItem[10];
    MenuInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_mark_insert);

        Intent get_intent = getIntent();
        insert_update = get_intent.getIntExtra("insert_update", 2);

        infoManager = new Chungsa_user_InfoManager();
        user = infoManager.getUserInfo();       //로그인 정보
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        insert_update_TextView = findViewById(R.id.camp_mark_insert_update_TextView); //컨텐츠
        title_editText = findViewById(R.id.camp_insert_title_editText); //제목
        type_spinner = findViewById(R.id.camp_insert_type_spinner);     //타입 스피너
        where_spinner = findViewById(R.id.camp_insert_where_spinner);   //지역 스피너
        where_detail = findViewById(R.id.camp_insert_where_detail);     //주소
        link_editText = findViewById(R.id.camp_insert_link_editText);   //링크
        phone_editText = findViewById(R.id.camp_insert_phone_editText); //연락처
        content_EditText = findViewById(R.id.camp_insert_content_EditText);     //내용
        imageview1 = findViewById(R.id.sermon_insert_imageview1);               //이미지1
        imageview2 = findViewById(R.id.sermon_insert_imageview2);               //이미지2
        imageview3 = findViewById(R.id.sermon_insert_imageview3);               //이미지3
        imageview4 = findViewById(R.id.sermon_insert_imageview4);               //이미지4
        search_btn = findViewById(R.id.camp_mark_insert_search_btn);            //검색 버튼
        search_edit = findViewById(R.id.camp_mark_insert_search_editText);      //주소 변경
        search_linear = findViewById(R.id.camp_mark_insert_search_linear);      //검색시 linear
        selected_linear = findViewById(R.id.camp_mark_insert_selected_linear);  //선택시 linear

        camp_mark_insert_search_setting();
        setting_spinner();

        if(insert_update==0){   //place에서 넘어왔을경우 => 추가
            place = (Camp_mark_Place) get_intent.getSerializableExtra("now_place");
            where_detail.setText(place.getRoad_address_name());
            x = place.getX();
            y = place.getY();
            link_editText.setText(place.getPlace_url());
            phone_editText.setText(place.getPhone());
            search_linear.setVisibility(View.GONE);
            selected_linear.setVisibility(View.VISIBLE);
        }
        else if (insert_update==1) {      //mark에서 넘어왔을 경우 => 수정
            insert_update=1;
            mark = (Camp_mark) get_intent.getSerializableExtra("camp_mark");

            title_editText.setText(mark.getCamp_mark_title());
            search_linear.setVisibility(View.GONE);
            selected_linear.setVisibility(View.VISIBLE);

            for(int i=0; i<type.size();i++) {           //이전 타입 불러오기
                if (mark.getCamp_mark_type().equals(type.get(i))){
                    type_spinner.setSelection(i);
                }
            }
            for(int i=0; i<where.size();i++) {          //이전 지역 불러오기
                if (mark.getCamp_mark_where().equals(where.get(i))){
                    where_spinner.setSelection(i);
                }
            }

            where_detail.setText(mark.getCamp_mark_where_detail());
            x = Double.toString(mark.getCamp_mark_x());
            y = Double.toString(mark.getCamp_mark_y());
            link_editText.setText(mark.getCamp_mark_link());
            phone_editText.setText(mark.getCamp_mark_phone());
            content_EditText.setText(mark.getCamp_mark_content());

            if(!mark.getCamp_mark_image_url1().equals("")){
                requestManager1 = Glide.with(this);
                requestBuilder1 = requestManager1.load(camp_mark_images_url + mark.getCamp_mark_image_url1())
                        .error(R.drawable.warning)
                        .placeholder(R.drawable.loading);
                requestBuilder1.into(imageview1);
                imageview1.setVisibility(View.VISIBLE);
            }
            if(!mark.getCamp_mark_image_url2().equals("")){
                requestManager2 = Glide.with(this);
                requestBuilder2 = requestManager2.load(camp_mark_images_url + mark.getCamp_mark_image_url2())
                        .error(R.drawable.warning)
                        .placeholder(R.drawable.loading);
                requestBuilder2.into(imageview2);
                imageview2.setVisibility(View.VISIBLE);
            }
            if(!mark.getCamp_mark_image_url3().equals("")){
                requestManager3 = Glide.with(this);
                requestBuilder3 = requestManager3.load(camp_mark_images_url + mark.getCamp_mark_image_url3())
                        .error(R.drawable.warning)
                        .placeholder(R.drawable.loading);
                requestBuilder3.into(imageview3);
                imageview3.setVisibility(View.VISIBLE);
            }
            if(!mark.getCamp_mark_image_url4().equals("")){
                requestManager4 = Glide.with(this);
                requestBuilder4 = requestManager4.load(camp_mark_images_url + mark.getCamp_mark_image_url4())
                        .error(R.drawable.warning)
                        .placeholder(R.drawable.loading);
                requestBuilder4.into(imageview4);
                imageview4.setVisibility(View.VISIBLE);
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    try {
                        if(requestManager1 != null)
                            requestManager1.clear(imageview1);
                        Uri imageUri = data.getData();
                        bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageview1.setImageBitmap(bitmap1);
                        imageview1.setVisibility(View.VISIBLE);
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
                            requestManager2.clear(imageview2);
                        Uri imageUri = data.getData();
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageview2.setImageBitmap(bitmap2);
                        imageview2.setVisibility(View.VISIBLE);
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
                            requestManager3.clear(imageview3);
                        Uri imageUri = data.getData();
                        bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageview3.setImageBitmap(bitmap3);
                        imageview3.setVisibility(View.VISIBLE);
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
                            requestManager4.clear(imageview4);
                        Uri imageUri = data.getData();
                        bitmap4 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageview4.setImageBitmap(bitmap4);
                        imageview4.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    public void image_btn1(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 1);
    }
    public void image_btn2(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 2);
    }
    public void image_btn3(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 3);
    }
    public void image_btn4(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 4);
    }



    private void setting_spinner(){

        type.add(0, "먹거리"); type.add(1, "카페"); type.add(2, "볼거리");
        type.add(3, "숙소");

        type_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, type));

        type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 사용자가 항목을 선택했을 때 실행되는 코드
                type_spinner_str = type.get(position); // 선택된 항목 가져오기
                // 선택된 항목에 대한 작업을 여기에 수행합니다.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });


        where.add(0, "서울"); where.add(1, "인천"); where.add(2, "강원");
        where.add(3, "경기"); where.add(4, "충남"); where.add(5, "세종");
        where.add(6, "대전"); where.add(7, "전북"); where.add(8, "광주");
        where.add(9, "전남"); where.add(10, "제주"); where.add(11, "경북");
        where.add(12, "대구"); where.add(13, "부산"); where.add(14, "울산");
        where.add(15, "경남"); where.add(16, "충북");

        where_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, where));

        where_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 사용자가 항목을 선택했을 때 실행되는 코드
                where_spinner_str = where.get(position); // 선택된 항목 가져오기
                // 선택된 항목에 대한 작업을 여기에 수행합니다.
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }

    private void str_upload(){//editText에서 정보받아오기
        title_str = title_editText.getText().toString();
        where_detail_str = where_detail.getText().toString();
        content_str = content_EditText.getText().toString();
        link_str = link_editText.getText().toString();
        phone_str = phone_editText.getText().toString();
    }

    public void camp_mark_update_or_insert_btn(View view){
        if(insert_update==0){
            camp_mark_insert_btn();
        } else if (insert_update==1){
            camp_mark_update_btn();
        } else if(x==""){
            Toast.makeText(getApplicationContext(), "x값을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if(y==""){
            Toast.makeText(getApplicationContext(), "x값을 입력해주세요", Toast.LENGTH_SHORT).show();
        } else if(insert_update ==2){
            camp_mark_insert_btn();
        }
    }

    private void camp_mark_insert_btn(){    //insert버튼
        new AlertDialog.Builder(this)
                .setMessage("이대로 생성하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        camp_mark_insert();

                        Intent intent = new Intent(getApplicationContext(), Camp_recommend_list.class); // 남길 액티비티
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();

                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                    }})
                .show();
    }

    private void camp_mark_insert(){        //삽입 과정
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("컨텐츠를 생성 중입니다...");

        str_upload();

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
            jsonObject.put("type", type_spinner_str);
            jsonObject.put("where", where_spinner_str);
            jsonObject.put("where_detail", where_detail_str);
            jsonObject.put("title", title_str);
            jsonObject.put("content", content_str);
            jsonObject.put("link", link_str);
            jsonObject.put("y", y);
            jsonObject.put("x", x);
            jsonObject.put("phone", phone_str);
            jsonObject.put("email", user.getChungsa_user_email());
            jsonObject.put("image1", image1);
            jsonObject.put("image2", image2);
            jsonObject.put("image3", image3);
            jsonObject.put("image4", image4);
            jsonObject.put("url1", url_name1);
            jsonObject.put("url2", url_name2);
            jsonObject.put("url3", url_name3);
            jsonObject.put("url4", url_name4);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, camp_mark_insert_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("Camp_mark_insert_insert_volley", response.toString());

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "실패하였습니다.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }


            );
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);

    }

    private void camp_mark_update_btn(){
        new AlertDialog.Builder(this)
                .setMessage("이대로 수정하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        camp_mark_update();

                        Intent intent = new Intent(getApplicationContext(), Camp_recommend_list.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finishAffinity();

                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                    }})
                .show();
    }

    public void camp_mark_update(){    //수정과정
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("컨텐츠를 수정 중입니다...");

        str_upload();

        progressDialog.show();
        String image1 ="";
        String url_name1=mark.getCamp_mark_image_url1();
        if(url_name1 !="") {
            url_name1 = url_name1.substring(9);
            url_name1 = url_name1.substring(0, url_name1.length() - 4);  //.jpg삭제
        }

        String image2 ="";
        String url_name2=mark.getCamp_mark_image_url2();
        if(url_name2 != "") {
            url_name2 = url_name2.substring(9);
            url_name2 = url_name2.substring(0, url_name2.length() - 4);
        }
        String image3 ="";
        String url_name3=mark.getCamp_mark_image_url3();
        if(url_name3 != "") {
            url_name3 = url_name3.substring(9);
            url_name3 = url_name3.substring(0, url_name3.length() - 4);
        }
        String image4 ="";
        String url_name4=mark.getCamp_mark_image_url4();

        if(url_name4 != "") {
            url_name4 = url_name4.substring(9);
            url_name4 = url_name4.substring(0, url_name4.length() - 4);
        }
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
            jsonObject.put("no", Integer.toString(mark.getCamp_mark_no()));
            jsonObject.put("type", type_spinner_str);
            jsonObject.put("where", where_spinner_str);
            jsonObject.put("where_detail", where_detail_str);
            jsonObject.put("title", title_str);
            jsonObject.put("content", content_str);
            jsonObject.put("link", link_str);
            jsonObject.put("y", y);
            jsonObject.put("x", x);
            jsonObject.put("phone", phone_str);
            jsonObject.put("email", user.getChungsa_user_email());
            jsonObject.put("image1", image1);
            jsonObject.put("image2", image2);
            jsonObject.put("image3", image3);
            jsonObject.put("image4", image4);
            jsonObject.put("url1", url_name1);
            jsonObject.put("url2", url_name2);
            jsonObject.put("url3", url_name3);
            jsonObject.put("url4", url_name4);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, camp_mark_update_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"업데이트 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("Camp_mark_insert_update_volley", response.toString());

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "업데이트 실패하였습니다.", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }


            );
        } catch (JSONException e) {
            e.printStackTrace();
        }


        requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(jsonObjectRequest);
    }

    private void camp_mark_insert_search_setting(){
        popupMenu = new PopupMenu(getApplicationContext(), search_edit);
        inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.camp_mark_insert_menu, popupMenu.getMenu());

        search[0] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_1);  //menu 불러오기
        search[1] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_2);  //menu 불러오기
        search[2] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_3);  //menu 불러오기
        search[3] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_4);  //menu 불러오기
        search[4] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_5);  //menu 불러오기
        search[5] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_6);  //menu 불러오기
        search[6] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_7);  //menu 불러오기
        search[7] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_8);  //menu 불러오기
        search[8] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_9);  //menu 불러오기
        search[9] = popupMenu.getMenu().findItem(R.id.camp_mark_insert_search_10);  //menu 불러오기

        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String search_keyword = search_edit.getText().toString();
                location_search(search_keyword);
            }
        });
    }

    private void location_search(String keyword){
        String apiUrl = BASE_URL + "v2/local/search/keyword.json?" +
                "&query=" + keyword;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 통신 성공
                        Log.d("Search", "Response: " + response.toString());

                        places = new ArrayList<>();

                        try {
                            JSONArray documents = response.getJSONArray("documents");
                            for (int i = 0; i < documents.length(); i++) {
                                JSONObject document = documents.getJSONObject(i);
                                String place_name = document.getString("place_name");
                                String phone = document.getString("phone");
                                String road_address_name = document.getString("road_address_name");
                                String x = document.getString("x");
                                String y = document.getString("y");
                                String place_url = document.getString("place_url");
                                places.add(new Camp_mark_Place(i + 1, place_name, phone, road_address_name, x, y, place_url));
                            }
                            Toast.makeText(getApplicationContext(),Integer.toString(places.size()), Toast.LENGTH_SHORT).show();
                            for(int i=0; i<places.size() && i<10; i++){
                                search[i].setTitle(places.get(i).getPlace_name() +"/" + places.get(i).getRoad_address_name());
                                search[i].setVisible(true);
                            }

                            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  //메뉴 선택시 설정
                                @Override
                                public boolean onMenuItemClick(MenuItem menuItem) {
                                    int id = menuItem.getItemId();

                                    if(id == R.id.camp_mark_insert_search_1){
                                        menu_choice(places.get(0));
                                    } else if (id == R.id.camp_mark_insert_search_2) {
                                        menu_choice(places.get(1));
                                    } else if (id == R.id.camp_mark_insert_search_3) {
                                        menu_choice(places.get(2));
                                    } else if (id == R.id.camp_mark_insert_search_4) {
                                        menu_choice(places.get(3));
                                    } else if (id == R.id.camp_mark_insert_search_5) {
                                        menu_choice(places.get(4));
                                    } else if (id == R.id.camp_mark_insert_search_6) {
                                        menu_choice(places.get(5));
                                    } else if (id == R.id.camp_mark_insert_search_7) {
                                        menu_choice(places.get(6));
                                    } else if (id == R.id.camp_mark_insert_search_8) {
                                        menu_choice(places.get(7));
                                    } else if (id == R.id.camp_mark_insert_search_9) {
                                        menu_choice(places.get(8));
                                    } else if (id == R.id.camp_mark_insert_search_10) {
                                        menu_choice(places.get(9));
                                    }
                                    return false;
                                }
                            });
                            popupMenu.show();

                        } catch (JSONException e) {
                            Log.d("Camp_recommend", "place 에러");
                            e.printStackTrace();
                        }

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Search", "통신 실패: " + error.getMessage());
                    }

                }
        ) {
            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", API_KEY);
                return headers;
            }
        };

        Volley.newRequestQueue(this).add(request);
    }
    private void menu_choice(Camp_mark_Place camp_mark_place){  //메뉴선택시
        search_linear.setVisibility(View.GONE);
        selected_linear.setVisibility(View.VISIBLE);
        where_detail.setText(camp_mark_place.getRoad_address_name());
        where_detail_str = camp_mark_place.getRoad_address_name();
        x = camp_mark_place.getX();
        y = camp_mark_place.getY();
    }

    public void camp_insert_where_detail_btn(View view){    //주소변경
        selected_linear.setVisibility(View.GONE);
        search_linear.setVisibility(View.VISIBLE);
        x = "";
        y = "";
        where_detail.setText("");
        search_edit.setText(where_detail_str);
        where_detail_str ="";
    }

}