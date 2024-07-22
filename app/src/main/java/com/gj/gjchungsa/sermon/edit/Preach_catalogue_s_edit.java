package com.gj.gjchungsa.sermon.edit;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Preach_catalogue_s_edit extends AppCompatActivity {

    Spinner spinner;
    String b_catalogue_str;
    EditText editText;
    Button insert_btn;
    String insert_url = "http://gjchungsa.com/series_sermon/catalogue_s_insert.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preach_catalogue_sedit);


        spinner = findViewById(R.id.preach_catalogue_s_edit_spinner);
        editText = findViewById(R.id.preach_catalogue_s_editText);
        insert_btn = findViewById(R.id.preach_catalogue_s_edit_insert_btn1);
        spinner_setting();


        insert_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(Preach_catalogue_s_edit.this)
                        .setMessage("추가 하시겠습니까?")
                        .setIcon(android.R.drawable.ic_menu_save)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // 확인시 처리 로직
                                insert_ing();

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
    }

    private void insert_ing() {
        String s_catalogue_str = editText.getText().toString();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, insert_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Log.d("Preach_catalouge_s_edit", response);
                        Intent intent = new Intent("catalouge_s");
                        sendBroadcast(intent);

                        finish();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("Preach_catalouge_s_Adapter", "delete_ing() 에러");
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("catalogue_s", s_catalogue_str);
                params.put("catalogue_b", b_catalogue_str);
                return params;
            }
        };
        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);
    }

    private void spinner_setting() {
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

        spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList_b));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                b_catalogue_str =  folderList_b.get(position); // 선택된 항목 가져오기

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }
}