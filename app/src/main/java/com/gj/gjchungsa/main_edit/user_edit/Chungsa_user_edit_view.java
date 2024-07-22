package com.gj.gjchungsa.main_edit.user_edit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.MainActivity;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chungsa_user_edit_view extends AppCompatActivity {


    TextView chungsa_user_email;
    EditText chungsa_user_edit_view_name_editText;
    Spinner grade_spinner;
    Spinner position_spinner;

    Button chungsa_user_edit_view_button;
    private ProgressDialog progressDialog;

    private static final String user_update_url ="http://gjchungsa.com/main_edit/chungsa_user/user_update.php";
    String update_grade_str, update_position_str, update_name_str;

    Chungsa_user edit_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chungsa_user_edit_view);

        Intent get_intent = getIntent();
//        Chungsa_user u = (Chungsa_user) get_intent.getSerializableExtra("user");
        edit_user = (Chungsa_user) get_intent.getSerializableExtra("user");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("유저 수정 중입니다...");

        chungsa_user_email = findViewById(R.id.chungsa_user_edit_view_email1);
        chungsa_user_edit_view_name_editText = findViewById(R.id.chungsa_user_edit_view_name_editText);

        chungsa_user_email.setText(edit_user.getChungsa_user_email());
        chungsa_user_edit_view_name_editText.setText(edit_user.getChungsa_user_name());

        grade_spinner = findViewById(R.id.chungsa_user_edit_view_grade_spinner);
        position_spinner = findViewById(R.id.chungsa_user_edit_view_position_spinner);

        make_grade_spinner();
        make_position_spinner();
    }

    private void make_grade_spinner(){
        ArrayList<String> folderList_grade = new ArrayList<>();

        folderList_grade.add(0, "최고관리자");               //일반사용자
        folderList_grade.add(1, "중간관리자");                 //양육사
        folderList_grade.add(2, "일반관리자");               //중간관리자
        folderList_grade.add(2, "청사교인");                 // 청사교인   => 캠프 올리기 가능
        folderList_grade.add(3, "일반사용자");               //기본

        grade_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList_grade));

        if(edit_user.getChungsa_user_grade().equals("최고관리자"))
            grade_spinner.setSelection(0);
        else if(edit_user.getChungsa_user_grade().equals("중간관리자"))
            grade_spinner.setSelection(1);
        else if(edit_user.getChungsa_user_grade().equals("일반관리자"))
            grade_spinner.setSelection(2);
        else if(edit_user.getChungsa_user_grade().equals("청사교인"))
            grade_spinner.setSelection(3);
        else
            grade_spinner.setSelection(4);

        grade_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                update_grade_str = folderList_grade.get(position); // 선택된 항목 가져오기
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }

    private void make_position_spinner(){
        ArrayList<String> folderList_position = new ArrayList<>();

        folderList_position.add(0, "목사");
        folderList_position.add(1, "장로");
        folderList_position.add(2, "안수집사");
        folderList_position.add(3, "권사");
        folderList_position.add(4, "집사");
        folderList_position.add(5, "청년");
        folderList_position.add(6, "학생");
        folderList_position.add(7, "성도");

        position_spinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), R.layout.item_sermon_folder, folderList_position));

        if(edit_user.getChungsa_user_position().equals("목사"))
            position_spinner.setSelection(0);
        else if(edit_user.getChungsa_user_position().equals("장로"))
            position_spinner.setSelection(1);
        else if(edit_user.getChungsa_user_position().equals("안수집사"))
            position_spinner.setSelection(2);
        else if(edit_user.getChungsa_user_position().equals("권사"))
            position_spinner.setSelection(3);
        else if(edit_user.getChungsa_user_position().equals("집사"))
            position_spinner.setSelection(4);
        else if(edit_user.getChungsa_user_position().equals("청년"))
            position_spinner.setSelection(5);
        else if(edit_user.getChungsa_user_position().equals("학생"))
            position_spinner.setSelection(6);
        else
            position_spinner.setSelection(7);

        position_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                update_position_str = folderList_position.get(position); // 선택된 항목 가져오기
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // 아무 항목도 선택하지 않았을 때 실행되는 코드
            }
        });
    }

    public void chungsa_user_edit_view_update(View view){
        update_name_str = chungsa_user_edit_view_name_editText.getText().toString();
        if(update_name_str.equals("")) {
            Toast.makeText(getApplicationContext(), "이름 적어주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        new AlertDialog.Builder(this)
                .setMessage("이대로 수정하시겠습니까?")
                .setIcon(android.R.drawable.ic_menu_save)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 확인시 처리 로직
                        Chungsa_user_update();

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);

                    }})
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // 취소시 처리 로직
                    }})
                .show();

    }
    private void Chungsa_user_update(){
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, user_update_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // 서버로부터의 응답 처리
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();

                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "volley 오류", Toast.LENGTH_SHORT).show();

                        Log.i("Chungsa_user_edit_view", "chungsa_user_delete Volley오류");
                    }

                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", edit_user.getChungsa_user_email());
                params.put("name", update_name_str);
                params.put("grade", update_grade_str);
                params.put("position", update_position_str);
                return params;
            }
        };

        Volley.newRequestQueue(getApplicationContext()).add(stringRequest);

    }
}