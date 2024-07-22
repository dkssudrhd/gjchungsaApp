package com.gj.gjchungsa.sermon.Series_sermon.edit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.edit.Preach_catalogue_s;
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

public class Series_sermon_edit_insert extends AppCompatActivity {

    Spinner b_spinner, s_spinner;
    Button image_btn, finish_btn;
    ImageView imageView;
    String b_catalogue_str, s_catalogue_str;
    ArrayList<Preach_catalogue_s> preach_catalogue_s =new ArrayList<>();
    private static final String preach_catalogue_s_url ="http://gjchungsa.com/preach_bbs/preach_catalogue_s.php";
    private static final String series_insert_url = "http://gjchungsa.com/series_sermon/series_insert.php";
    Bitmap bitmap;
    public RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private JsonObjectRequest jsonObjectRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_series_sermon_edit_insert);

        findview_setting();
        requestQueue = Volley.newRequestQueue(getApplicationContext());

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("추가하는 중입니다.");

    }

    private void findview_setting() {
        b_spinner = findViewById(R.id.series_sermon_edit_insert_b_spinner);
        s_spinner = findViewById(R.id.series_sermon_edit_insert_s_spinner);
        imageView = findViewById(R.id.series_sermon_edit_insert_imageview);
        image_btn = findViewById(R.id.series_sermon_edit_insert_image_btn);
        finish_btn = findViewById(R.id.series_sermon_edit_insert_finish_btn);

        finish_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Series_sermon_edit_insert.this)
                        .setMessage("시리즈를 추가하시겠습니까?")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 확인시 처리 로직
                                finish_btn_ing();

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
        });

        b_spinner_setting();
        finish_btn_setting();
    }

    private void b_spinner_setting() {
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

        b_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList_b));

        b_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                b_catalogue_str =  folderList_b.get(position); // 선택된 항목 가져오기

                s_spinner_setting(b_catalogue_str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }

    private void s_spinner_setting(String b_str){

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

                        ArrayList<String> folderList = new ArrayList<>();

                        if(preach_catalogue_s == null){
                            Toast.makeText(getApplicationContext(),"항목이 없습니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        for(int i=0;i<preach_catalogue_s.size();i++){
                            folderList.add(i, preach_catalogue_s.get(i).getBbs_s_catalogue());
                        }
                        s_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList));

                        s_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                                // 사용자가 항목을 선택했을 때 실행되는 코드
                                s_catalogue_str = folderList.get(position); // 선택된 항목 가져오기
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
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("bbs_b_catalogue", b_str);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    public void Setinsert_image_btn(View view){
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {   //이미지 버튼 클릭시 이미지 불러오기
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 1: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        imageView.setImageBitmap(bitmap);
                        imageView.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }

    private void finish_btn_setting(){


    }

    private void finish_btn_ing() {
        String image ="";
        String url_name = "";


        progressDialog.show();

        if(bitmap != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            image = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name = "series_" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";
        }



        try {
            jsonObject = new JSONObject();
            jsonObject.put("url_name", url_name);
            jsonObject.put("image", image);
            jsonObject.put("series_name", s_catalogue_str);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, series_insert_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            progressDialog.dismiss();

                            Intent intent = new Intent(getApplicationContext(), Series_sermon_edit.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);


                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "업로드 실패하였습니다.", Toast.LENGTH_SHORT).show();
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
}