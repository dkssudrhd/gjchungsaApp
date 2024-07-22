package com.gj.gjchungsa.sermon.edit;

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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.RequestManager;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.sermon.Sermon_list;
import com.gj.gjchungsa.sermon.Preach_bbs;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Sermon_edit_update extends AppCompatActivity {
    public Bitmap bitmap;
    public RequestQueue requestQueue;
    //public ArrayList<Series_sermon> series_sermon;
    public ArrayList<Preach_catalogue_s> preach_catalogue_s;

    private static final String preach_catalogue_s_url ="http://gjchungsa.com/preach_bbs/preach_catalogue_s.php";
    private static final String sermon_update_url ="http://gjchungsa.com/preach_bbs/preach_update.php";
    private static String url = "http://gjchungsa.com/select_preach_bbs.php";

    private static final String preach_bbs_image_url ="http://gjchungsa.com/preach_bbs/sermon_images/";
    public String update_b_spinner_str;    //대분류
    public String update_s_spinner_str;    //소분류
    public String update_bbs_video_type;   //비디오 타입
    ImageView imageView;
    EditText update_sermon_title_editText; //설교제목
    EditText update_sermon_preacher_editText;  //설교자
    EditText update_sermon_parse_editText; //설교본문
    EditText update_content_EditText;      //본문말씀
    EditText update_sermon_bbs_when_editText; //설교날짜
    EditText update_sermon_bbs_line_editText;  //비디오 id

    String title, preacher, content, parse, when, line;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private JsonObjectRequest jsonObjectRequest;

    Chungsa_user_InfoManager infoManager; //로그인 확인
    Chungsa_user user;  //유저
    int null_check;
    Preach_bbs update_preach_bbs;
    Spinner video_type_spinner;
    RequestBuilder<Drawable> requestBuilder;
    RequestManager requestManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_edit_update);

        bitmap=null;

        Intent get_intent = getIntent();
        update_preach_bbs = (Preach_bbs) get_intent.getSerializableExtra("preach_bbs");
        loading_preach_bbs();

    }
    private void loading_preach_bbs(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                Preach_bbs[] preaches = gson.fromJson(response, Preach_bbs[].class);
                if (preaches != null) {
                    update_preach_bbs = preaches[0];
                }
//                Log.d("stringRequest checking", response);
                setting();

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
                params.put("no", Integer.toString(update_preach_bbs.getBbs_no()));

                return params;
            }

        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void setting() {
        infoManager = new Chungsa_user_InfoManager();
        user = infoManager.getUserInfo();

        update_sermon_bbs_line_editText = findViewById(R.id.update_sermon_bbs_line_editText);
        update_sermon_preacher_editText = findViewById(R.id.update_sermon_preacher_editText);
        update_content_EditText = findViewById(R.id.update_content_EditText);
        update_sermon_parse_editText = findViewById(R.id.update_sermon_parse_editText);
        update_sermon_bbs_when_editText = findViewById(R.id.update_sermon_bbs_when_editText);
        update_sermon_title_editText = findViewById(R.id.update_sermon_title_editText);

        requestQueue = Volley.newRequestQueue(getApplicationContext());
        get_b_spinner();
        video_type_spinner();
        update_setting();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("설교를 수정 중입니다...");
        imageView = findViewById(R.id.update_sermon_insert_imageview);


        requestManager = Glide.with(this);
        requestBuilder = requestManager.load(preach_bbs_image_url + update_preach_bbs.getBbs_image_url())
                .error(R.drawable.warning)
                .placeholder(R.drawable.loading);
        requestBuilder.into(imageView);     //Gilde 적용

    }

    public void update_setting(){
        update_sermon_bbs_line_editText.setText(update_preach_bbs.getBbs_line());
        update_sermon_preacher_editText.setText(update_preach_bbs.getBbs_preacher());
        update_content_EditText.setText(update_preach_bbs.getBbs_content());
        update_sermon_parse_editText.setText(update_preach_bbs.getBbs_parse());
        update_sermon_bbs_when_editText.setText(update_preach_bbs.getBbs_when());
        update_sermon_title_editText.setText(update_preach_bbs.getBbs_title());

    }

    public void upload_check(){     //비어있는 항목 확인 및 받아오기

        title = update_sermon_title_editText.getText().toString();
        preacher = update_sermon_preacher_editText.getText().toString();
        content = update_content_EditText.getText().toString();
        parse = update_sermon_parse_editText.getText().toString();
        when = update_sermon_bbs_when_editText.getText().toString();
        line = update_sermon_bbs_line_editText.getText().toString();
        if(title.equals(""))
            Toast.makeText(getApplicationContext(),"설교제목 적어주세요",Toast.LENGTH_SHORT).show();
        else if(preacher.equals(""))
            Toast.makeText(getApplicationContext(),"설교자 적어주세요",Toast.LENGTH_SHORT).show();
        else if(content.equals(""))
            Toast.makeText(getApplicationContext(),"본문말씀 적어주세요",Toast.LENGTH_SHORT).show();
        else if(parse.equals(""))
            Toast.makeText(getApplicationContext(),"성경구절 적어주세요",Toast.LENGTH_SHORT).show();
        else if(when.equals(""))
            Toast.makeText(getApplicationContext(),"날짜 적어주세요",Toast.LENGTH_SHORT).show();
        else if(line.equals(""))
            Toast.makeText(getApplicationContext(),"비디오 ID 적어주세요", Toast.LENGTH_SHORT).show();
        else {
            null_check =1;
        }
        null_check =0;
    }



    public void sermon_update_btn(View view) {  //수정버튼


        new AlertDialog.Builder(this)
                .setMessage("이대로 수정하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        sermon_update();

                        Intent intent = new Intent(getApplicationContext(), Sermon_list.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                    }})
                .show();

    }
    public void sermon_update(){    //수정과정
        upload_check();
        if(null_check == 1){
            return;
        }
//        Toast.makeText(getApplicationContext(),preacher,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),when,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),line,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),bbs_video_type,Toast.LENGTH_SHORT).show();
//        Toast.makeText(getApplicationContext(),user.getChungsa_user_email(),Toast.LENGTH_SHORT).show();


        progressDialog.show();
        String image ="";
        String url_name="";
        if(bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }



        try {
            jsonObject = new JSONObject();
            jsonObject.put("bbs_no", Integer.toString(update_preach_bbs.getBbs_no()));
            jsonObject.put("bbs_title",title);
            jsonObject.put("bbs_parse", parse);
            jsonObject.put("bbs_content", content);
            jsonObject.put("bbs_b_catalogue", update_b_spinner_str);
            jsonObject.put("bbs_s_catalogue", update_s_spinner_str);
            jsonObject.put("bbs_preacher", preacher);
            jsonObject.put("bbs_when",when);
            jsonObject.put("bbs_line",line);
            jsonObject.put("url_name", url_name);
            jsonObject.put("image", image);
            jsonObject.put("bbs_video_type", update_bbs_video_type);
            jsonObject.put("chungsa_user_email", user.getChungsa_user_email());

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, sermon_update_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"업데이트 성공하셨습니다.", Toast.LENGTH_SHORT).show();

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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    try {
                        requestManager.clear(imageView);
                        Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageView.setImageBitmap(bitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }
    public void image_btn(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 1);
    }

    public void video_type_spinner(){       //비디오 타입
        video_type_spinner =findViewById(R.id.update_video_type_spinner);
        ArrayList<String> video_type_List = new ArrayList<>();
        video_type_List.add("유튜브");

        video_type_List.add("비메오");

        video_type_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, video_type_List));

        if(update_preach_bbs.getBbs_video_type().equals("youtube"))     //선택되어있는 거 찾기
            video_type_spinner.setSelection(0);
        else
            video_type_spinner.setSelection(1);

        video_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 사용자가 항목을 선택했을 때 실행되는 코드
                if(video_type_List.get(position).equals("유튜브"))
                    update_bbs_video_type = "youtube";
                else if(video_type_List.get(position).equals("비메오")) {
                    update_bbs_video_type = "vimeo";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });

    }

    public void get_b_spinner(){    //대분류
        Spinner b_spinner = findViewById(R.id.update_b_spinner);

        ArrayList<String> folderList_b = new ArrayList<>();

        folderList_b.add(0, "세대통합예배");
        folderList_b.add(1, "주일찬양예배");
        folderList_b.add(2, "새벽예배");
        folderList_b.add(3, "수요예배");
        folderList_b.add(4, "샬롬마룻바닥기도회");
        folderList_b.add(5, "부교역자설교");
        folderList_b.add(6, "외부강사");
        folderList_b.add(7, "홍보영상");

        b_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList_b));

        if(update_preach_bbs.getBbs_b_catalogue().equals("세대통합예배"))
            b_spinner.setSelection(0);
        else if(update_preach_bbs.getBbs_b_catalogue().equals("주일찬양예배"))
            b_spinner.setSelection(1);
        else if(update_preach_bbs.getBbs_b_catalogue().equals("새벽예배"))
            b_spinner.setSelection(2);
        else if(update_preach_bbs.getBbs_b_catalogue().equals("수요예배"))
            b_spinner.setSelection(3);
        else if(update_preach_bbs.getBbs_b_catalogue().equals("샬롬마룻바닥기도회"))
            b_spinner.setSelection(4);
        else if(update_preach_bbs.getBbs_b_catalogue().equals("부교역자설교"))
            b_spinner.setSelection(5);
        else if(update_preach_bbs.getBbs_b_catalogue().equals("외부강사"))
            b_spinner.setSelection(6);
        else
            b_spinner.setSelection(7);
        b_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // 사용자가 항목을 선택했을 때 실행되는 코드
                update_b_spinner_str = folderList_b.get(position); // 선택된 항목 가져오기
                // 선택된 항목에 대한 작업을 여기에 수행합니다.
                //Toast.makeText(getApplicationContext(),b_spinner_str,Toast.LENGTH_SHORT).show();
                get_s_spinner(update_b_spinner_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }

    private void get_s_spinner(String catalogue_b){ //시리즈 스피너 추가
        Toast.makeText(getApplicationContext(),catalogue_b,Toast.LENGTH_SHORT).show();

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
                        spinner_add();
                    }
                    private void spinner_add(){     //series 스피너 추가
                        Spinner s_spinner = findViewById(R.id.update_s_spinner);

                        int s_num=0;

                        ArrayList<String> folderList = new ArrayList<>();

                        if(preach_catalogue_s == null){
                            Toast.makeText(getApplicationContext(),"항목이 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(int i=0;i<preach_catalogue_s.size();i++){
                            folderList.add(i, preach_catalogue_s.get(i).getBbs_s_catalogue());
                            if(preach_catalogue_s.get(i).getBbs_s_catalogue().equals(update_preach_bbs.getBbs_s_catalogue())){
                                s_num=i;
                            }
                        }
                        s_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList));

                        s_spinner.setSelection(s_num);
                        s_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // 사용자가 항목을 선택했을 때 실행되는 코드
                                update_s_spinner_str = folderList.get(position); // 선택된 항목 가져오기
                                // 선택된 항목에 대한 작업을 여기에 수행합니다.
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parentView) {
                                // 아무 항목도 선택하지 않았을 때 실행되는 코드
                            }
                        });
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
                params.put("bbs_b_catalogue", catalogue_b);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }
}