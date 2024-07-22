package com.gj.gjchungsa.weekly.edit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.gj.gjchungsa.weekly.Weekly_list;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class Weekly_edit_insert extends AppCompatActivity {

    private static final String weekly_insert_url ="http://gjchungsa.com/weekly/weekly_insert.php";
    EditText when_editText;
    Button weekly1_btn, weekly2_btn, weekly3_btn;
    ImageView weekly1, weekly2, weekly3;
    Chungsa_user_InfoManager infoManager; //로그인 확인
    Chungsa_user user;  //유저
    Bitmap bitmap1, bitmap2, bitmap3;
    public RequestQueue requestQueue;
    private ProgressDialog progressDialog;
    private JSONObject jsonObject;
    private JsonObjectRequest jsonObjectRequest;
    String when_str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_edit_insert);

        infoManager = new Chungsa_user_InfoManager();
        user = infoManager.getUserInfo();

        when_editText = findViewById(R.id.weekly_edit_insert_when_editText);
        weekly1 = findViewById(R.id.weekly_edit_insert_weekly1);
        weekly2 = findViewById(R.id.weekly_edit_insert_weekly2);
        weekly3 = findViewById(R.id.weekly_edit_insert_weekly3);

        requestQueue = Volley.newRequestQueue(getApplicationContext());


        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("주보를 올리는 중입니다...");
    }

    public void weekly_insert_btn(View view) {  //추가버튼


        new AlertDialog.Builder(this)
                .setMessage("이대로 추가하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        weekly_insert();

                        Intent intent = new Intent(getApplicationContext(), Weekly_list.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                    }})
                .show();

    }


    private void weekly_insert(){
        when_str = when_editText.getText().toString();
        if(when_str.equals("")){
            Toast.makeText(getApplicationContext(),"제목 적어주세요",Toast.LENGTH_SHORT).show();
            return;
        }
        //progressDialog.show();


        String image1_str ="";
        String url_name1_str = "";
        String image2_str ="";
        String url_name2_str = "";
        String image3_str ="";
        String url_name3_str = "";

        if(bitmap1 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            image1_str = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name1_str = "weekly" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";
        }
        if(bitmap2 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap2.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            image2_str = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name2_str = "weekly" +  String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";
        }
        if(bitmap3 != null){
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap3.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            image3_str = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
            url_name3_str = "weekly" + String.valueOf(Calendar.getInstance().getTimeInMillis()) + ".jpg";
        }

        try {
            jsonObject = new JSONObject();
            jsonObject.put("when", when_str);
            jsonObject.put("email", user.getChungsa_user_email());
            jsonObject.put("url_name1", url_name1_str);
            jsonObject.put("image1", image1_str);
            jsonObject.put("url_name2", url_name2_str);
            jsonObject.put("image2", image2_str);
            jsonObject.put("url_name3", url_name3_str);
            jsonObject.put("image3", image3_str);

            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, weekly_insert_url, jsonObject,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(),"업데이트 성공하셨습니다.", Toast.LENGTH_SHORT).show();
                            Log.d("Weekly", "성공");
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(getApplicationContext(), "업데이트 실패하였습니다. 오류: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    Log.d("Weekly","업데이트 실패하였습니다. 오류: " + error.getMessage() );
                    //progressDialog.dismiss();
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
                        Uri imageUri = data.getData();
                        bitmap1 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        weekly1.setImageBitmap(bitmap1);
                        weekly1.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;

            case 2: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        bitmap2 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        weekly2.setImageBitmap(bitmap2);
                        weekly2.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;

            case 3: {
                if (resultCode == RESULT_OK) {
                    try {
                        Uri imageUri = data.getData();
                        bitmap3 = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                        weekly3.setImageBitmap(bitmap3);
                        weekly3.setVisibility(View.VISIBLE);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            break;
        }
    }


    public void weekly_insert_image1_btn(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 1);
    }
    public void weekly_insert_image2_btn(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 2);
    }
    public void weekly_insert_image3_btn(View view){   //이미지 추가 버튼
        Intent select = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(select, 3);
    }

}