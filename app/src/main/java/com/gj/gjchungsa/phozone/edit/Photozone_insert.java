package com.gj.gjchungsa.phozone.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.phozone.Photozone;
import com.gj.gjchungsa.phozone.Photozone_image;
import com.gj.gjchungsa.phozone.Photozone_list;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Photozone_insert extends AppCompatActivity {

    EditText title_editText, content_editText;

    Button image_btn [] = new Button[10];
    Button image_cancal_btn [] = new Button[10];
    ImageView image_view [] = new ImageView[10];

    Bitmap bitmap[] = new Bitmap[10];
    String photozone_image_url = "http://gjchungsa.com/photozone/photozone_image/";
    String photozone_image_insert = "http://gjchungsa.com/photozone/photozone_image_insert.php";
    String photozone_image_update = "http://gjchungsa.com/photozone/photozone_image_update.php";
    String photozone_insert ="http://gjchungsa.com/photozone/photozone_insert.php";
    String photozone_update ="http://gjchungsa.com/photozone/photozone_update.php";
    String photozone_image_select_delete_url = "http://gjchungsa.com/photozone/photozone_image_select_delete.php";
    String photozone_image_order_url = "http://gjchungsa.com/photozone/photozone_image_order.php";
    RequestManager requestManager [] = new RequestManager[10];
    RequestBuilder requestBuilder [] = new RequestBuilder[10];

    Photozone get_photozone;
    ArrayList<Photozone_image> get_photozone_image_list;
    String situation[] = new String[10];
    int insert_update_photo_no=0;
    int photozone_image_order [] = new int[10];
    String title_str, content_str;

    Chungsa_user_InfoManager infoManager;
    Chungsa_user chungsa_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photozone_insert);

        Intent get_intent = getIntent();

        get_photozone = (Photozone) get_intent.getSerializableExtra("photozone");
        get_photozone_image_list = (ArrayList<Photozone_image>) get_intent.getSerializableExtra("photozone_image_list");

        infoManager = new Chungsa_user_InfoManager();   //로그인 정보
        chungsa_user = infoManager.getUserInfo();

        title_editText = findViewById(R.id.photozone_insert_title_editText);
        content_editText = findViewById(R.id.photozone_insert_content_editText);


        for(int i=0;i < 10;i++) {
            situation[i] = "not";
            photozone_image_order[i] =0;
            requestManager[i] = null;
            requestBuilder[i] = null;
        }
        image_insert_setting();
    }

    private void image_insert_setting() {
        image_btn[0] = findViewById(R.id.photozone_image1_upload_btn);
        image_btn[1] = findViewById(R.id.photozone_image2_upload_btn);
        image_btn[2] = findViewById(R.id.photozone_image3_upload_btn);
        image_btn[3] = findViewById(R.id.photozone_image4_upload_btn);
        image_btn[4] = findViewById(R.id.photozone_image5_upload_btn);
        image_btn[5] = findViewById(R.id.photozone_image6_upload_btn);
        image_btn[6] = findViewById(R.id.photozone_image7_upload_btn);
        image_btn[7] = findViewById(R.id.photozone_image8_upload_btn);
        image_btn[8] = findViewById(R.id.photozone_image9_upload_btn);
        image_btn[9] = findViewById(R.id.photozone_image10_upload_btn);

        image_cancal_btn[0] = findViewById(R.id.photozone_image1_cancel_btn);
        image_cancal_btn[1] = findViewById(R.id.photozone_image2_cancel_btn);
        image_cancal_btn[2] = findViewById(R.id.photozone_image3_cancel_btn);
        image_cancal_btn[3] = findViewById(R.id.photozone_image4_cancel_btn);
        image_cancal_btn[4] = findViewById(R.id.photozone_image5_cancel_btn);
        image_cancal_btn[5] = findViewById(R.id.photozone_image6_cancel_btn);
        image_cancal_btn[6] = findViewById(R.id.photozone_image7_cancel_btn);
        image_cancal_btn[7] = findViewById(R.id.photozone_image8_cancel_btn);
        image_cancal_btn[8] = findViewById(R.id.photozone_image9_cancel_btn);
        image_cancal_btn[9] = findViewById(R.id.photozone_image10_cancel_btn);

        image_view[0] = findViewById(R.id.photozone_imageview1);
        image_view[1] = findViewById(R.id.photozone_imageview2);
        image_view[2] = findViewById(R.id.photozone_imageview3);
        image_view[3] = findViewById(R.id.photozone_imageview4);
        image_view[4] = findViewById(R.id.photozone_imageview5);
        image_view[5] = findViewById(R.id.photozone_imageview6);
        image_view[6] = findViewById(R.id.photozone_imageview7);
        image_view[7] = findViewById(R.id.photozone_imageview8);
        image_view[8] = findViewById(R.id.photozone_imageview9);
        image_view[9] = findViewById(R.id.photozone_imageview10);


        if(get_photozone != null){ //update시
            content_editText.setText(get_photozone.getPhotozone_content());
            title_editText.setText(get_photozone.getPhotozone_title());

            for(int i=0;i<get_photozone_image_list.size();i++){
                int order = get_photozone_image_list.get(i).getPhotozone_image_order()-1;
                requestManager[order] = Glide.with(this);
                requestBuilder[order] = requestManager[order].load(photozone_image_url + get_photozone_image_list.get(order).getPhotozone_image_url())
                        .error(R.drawable.warning)
                        .placeholder(R.drawable.loading);
                requestBuilder[order].into(image_view[order]);
                image_view[order].setVisibility(View.VISIBLE);
                image_cancal_btn[order].setVisibility(View.VISIBLE);
                if(order <9)
                    image_btn[order+1].setVisibility(View.VISIBLE);
                situation[order] = "select";
            }
        }

        for(int i=0; i< 10;i ++){
            int finalI = i;
            image_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(select, finalI);
                }
            });

            image_cancal_btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(situation[finalI].equals("select")){
                        if(requestManager[finalI] != null)
                            requestManager[finalI].clear(image_view[finalI]);
                        situation[finalI] = "delete";
                    } else {
                        bitmap[finalI] = null;
                        image_view[finalI].setVisibility(View.GONE);
                        situation[finalI] = "not";
                    }
                }
            });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            try{
                if(requestManager[requestCode] != null)
                    requestManager[requestCode].clear(image_view[requestCode]);
                Uri imageUri = data.getData();
                bitmap[requestCode] = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                image_view[requestCode].setImageBitmap(bitmap[requestCode]);
                image_view[requestCode].setVisibility(View.VISIBLE);

                if(situation[requestCode].equals("select") || situation[requestCode].equals("delete") ){
                    situation[requestCode] = "update";
                } else{
                    situation[requestCode] = "insert";
                    Log.d("Photozone_insert_log", "situation 이 insert 되었습니다,");
                }

                image_cancal_btn[requestCode].setVisibility(View.VISIBLE);
                
                if(requestCode<9) 
                    image_btn[requestCode + 1].setVisibility(View.VISIBLE);
                
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    
    public void insert_update_btn(View view){
        title_str = title_editText.getText().toString();
        content_str = content_editText.getText().toString();

        if(title_str.equals("")){
            Toast.makeText(getApplicationContext(),"제목을 입력하세요", Toast.LENGTH_SHORT).show();
        } else if(content_str.equals(""))
            Toast.makeText(getApplicationContext(),"내용을 입력하세요", Toast.LENGTH_SHORT).show();
        else{
            new AlertDialog.Builder(this)
                    .setMessage("완료 하시겠습니까?")
                    .setIcon(android.R.drawable.ic_menu_save)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            // 확인시 처리 로직
                            first_insert_update_ing();

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
    }

    private void first_insert_update_ing(){
        if(get_photozone != null){  //update시
            StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_update,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            insert_update_ing(get_photozone.getPhotozone_no());

                            Log.d("Photozone_insert_log", "first_insert_update : update " + response);

                            Intent intent = new Intent(getApplicationContext(), Photozone_list.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Photozone_insert_log", " first_insert_update : update 오류 " +Integer.toString(get_photozone.getPhotozone_no())
                                    + " title : " + title_str+ " content : " +content_str);
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("no", Integer.toString(get_photozone.getPhotozone_no()));
                    params.put("title", title_str);
                    params.put("content", content_str);
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

        } else{     //insert시

            StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_insert,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리

                            Gson gson = new Gson();
                            Photozone_image[] photozone_images = gson.fromJson(response, Photozone_image[].class);

                            Log.d("Photozone_insert_log", "first_insert_update : insert "+ Integer.toString(photozone_images[0].getPhotozone_no()) + "  내용: " + response);

                            insert_update_ing(photozone_images[0].getPhotozone_no());


                            Intent intent = new Intent(getApplicationContext(), Photozone_list.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Photozone_insert_log", " first_insert_update : insert 오류");
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("title", title_str);
                    params.put("content", content_str);
                    params.put("chungsa_user_email", chungsa_user.getChungsa_user_email());
                    return params;
                }
            };
            Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
        }
    }

    private void insert_update_ing(int photozone_no) {
        insert_update_photo_no = 0;

        for(int i=0; i<10;i++){
            
            if(situation[i].equals("insert")){                          //photozone_image_insert
                photozone_image_insert_ing(i, photozone_no);
                insert_update_photo_no++;
            } else if(situation[i].equals("update")){                   //photozone_image_update
                Photozone_image update_image = null;
                Log.d("Photozone_insert_log", "update 확인" );

                for(int j=0; j<get_photozone_image_list.size(); j++){
                    if(get_photozone_image_list.get(j).getPhotozone_image_order() == i+1){
                        update_image = get_photozone_image_list.get(j);
                    }
                }

                Log.d("Photozone_insert_log", "update 넘버는?" + Integer.toString(update_image.getPhotozone_image_no()) );
                photozone_image_update_ing(update_image, i);
                insert_update_photo_no++;

            } else if(situation[i].equals("delete")){                   //photozone_image_delete
                Photozone_image delete_image = null;
                for(int j=0; j<get_photozone_image_list.size(); j++){
                    if(get_photozone_image_list.get(j).getPhotozone_image_order() == i+1)
                        delete_image = get_photozone_image_list.get(j);
                }
                photozone_image_delete_ing(delete_image);

            } else if(situation[i].equals("select")){
                Photozone_image order_image = null;
                Log.d("Photozone_insert_log", "select 확인" );

                for(int j=0; j<get_photozone_image_list.size(); j++){
                    if(get_photozone_image_list.get(j).getPhotozone_image_order() == i+1){
                        order_image = get_photozone_image_list.get(j);
                    }
                }
                photozone_image_order_ing(order_image, i);
                insert_update_photo_no++;
            }
        }
    }

    private void photozone_image_order_ing(Photozone_image order_image, int i) {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_image_order_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Photozone_insert_log", "not " +response);
                        Log.d("Photozone_insert_log", "not " + Integer.toString(insert_update_photo_no+1));
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Photozone_insert_log", "not 오류" );
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no", Integer.toString(order_image.getPhotozone_image_no()));
                params.put("order", Integer.toString(insert_update_photo_no+1));
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }

    private void photozone_image_delete_ing(Photozone_image delete_image) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, photozone_image_select_delete_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Photozone_insert_log", "delete " +response);
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Photozone_insert_log", "delete 오류" );
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("no", Integer.toString(delete_image.getPhotozone_image_no()));
                params.put("url", delete_image.getPhotozone_image_url());
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void photozone_image_update_ing(Photozone_image update_image, int number) {
        String image ="";
        String url_name="";

        if(bitmap[number] != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap[number].compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }

        JsonObjectRequest jsonObjectRequest = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("no", Integer.toString(update_image.getPhotozone_image_no()));
            jsonObject.put("image", image);
            jsonObject.put("url_name", url_name);
            jsonObject.put("before_url", update_image.getPhotozone_image_url());
            jsonObject.put("order", insert_update_photo_no+1);

            String finalUrl_name = url_name;
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, photozone_image_update, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Photozone_insert_log", "update" +response.toString());
                            Log.d("Photozone_insert_log", "update 내용" +Integer.toString(update_image.getPhotozone_image_no())
                                    +  " " + finalUrl_name + " " + update_image.getPhotozone_image_url());

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Photozone_insert_log", "update" + error.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void photozone_image_insert_ing(int number, int photozone_no) {
        String image ="";
        String url_name="";

        if(bitmap[number] != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap[number].compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name = String.valueOf(Calendar.getInstance().getTimeInMillis());
        }

        JsonObjectRequest jsonObjectRequest = null;
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("image", image);
            jsonObject.put("url_name", url_name);
            jsonObject.put("photozone_image_order", Integer.toString(insert_update_photo_no+1));
            jsonObject.put("photozone_no", Integer.toString(photozone_no));

            //포토존

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, photozone_image_insert, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            Log.d("Photozone_insert_log"," insert" + response.toString());
                            
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.d("Photozone_insert_log", " insert" + error.toString());
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
}