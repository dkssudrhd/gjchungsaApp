package com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule_comment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_mark;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_schedule_comment_Adapter extends  RecyclerView.Adapter<Camp_schedule_comment_Adapter.ViewHolder>{


    ArrayList<Camp_schedule_comment> items =new ArrayList<>();

    public void addItem(Camp_schedule_comment camp_schedule_comment){
        items.add(camp_schedule_comment);
    }
    public void setItems(ArrayList<Camp_schedule_comment> items){
        this.items = items;
    }
    public Camp_schedule_comment getItem(int position){
        return items.get(position);
    }


    @NonNull
    @Override
    public Camp_schedule_comment_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_comment_item, parent, false);
        return new Camp_schedule_comment_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_schedule_comment_Adapter.ViewHolder holder, int position) {
        Camp_schedule_comment item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Camp_mark now_camp_mark;
        TextView name_textview, content, creation;
        Button go_insert_btn, update_btn, delete_btn, insert_btn;
        int insert_update;
        Chungsa_user_InfoManager infoManager;
        LinearLayout comment_plus_content_linear , camp_comment_item_recylerView_linear;
        RecyclerView comment_RecyclerView;
        Chungsa_user chungsa_user;
        EditText comment_editText;
        ArrayList<Camp_schedule_comment> comment_schedule_list = new ArrayList<>();
        final static String camp_schedule_comment_delete_url ="http://gjchungsa.com/camp_schedule/comment/camp_schedule_comment_delete.php";
        final static String camp_schedule_comment_insert_url ="http://gjchungsa.com/camp_schedule/comment/camp_schedule_comment_insert.php";
        final static String camp_schedule_comment_update_url ="http://gjchungsa.com/camp_schedule/comment/camp_schedule_comment_update.php";
        final static String camp_schedule_comment_comment_url = "http://gjchungsa.com/camp_schedule/comment/camp_schedule_comment_comment.php";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


        }
        public void setItem(Camp_schedule_comment camp_schedule_comment){
            infoManager = new Chungsa_user_InfoManager();   //로그인 정보
            chungsa_user = infoManager.getUserInfo();

            name_textview = itemView.findViewById(R.id.comment_item_user_name);
            content = itemView.findViewById(R.id.comment_item_content);
            creation = itemView.findViewById(R.id.comment_item_creation);
            go_insert_btn = itemView.findViewById(R.id.comment_item_go_insert);
            comment_plus_content_linear = itemView.findViewById(R.id.camp_comment_item_comment_plus_content_linear);
            update_btn = itemView.findViewById(R.id.comment_item_update);   //수정 버튼
            delete_btn = itemView.findViewById(R.id.comment_item_delete);   //삭제 버튼
            comment_editText = itemView.findViewById(R.id.camp_comment_item_editTextText);  //답글 내용
            insert_btn = itemView.findViewById(R.id.comment_item_insert);       //댓글 등록 버튼
            camp_comment_item_recylerView_linear = itemView.findViewById(R.id.camp_comment_item_recylerView_linear);    //대댓글
            comment_RecyclerView = itemView.findViewById(R.id.camp_comment_item_comment_RecyclerView);      //대댓글 리사이클뷰

            name_textview.setText(camp_schedule_comment.getChungsa_user_name()); //작성자
            String time = camp_schedule_comment.getCamp_schedule_comment_creation();
            time = time.substring(2);
            time = time.substring(0, time.length() - 3);
            creation.setText(time); //시간 넣기
            content.setText(camp_schedule_comment.getCamp_schedule_comment_content());        //내용

            if(chungsa_user != null) {
                if (chungsa_user.getChungsa_user_email().equals(camp_schedule_comment.getChungsa_user_email()) ||
                        chungsa_user.getChungsa_user_grade().equals("최고관리자") ||
                        chungsa_user.getChungsa_user_grade().equals("중간관리자")) {
                    update_btn.setVisibility(View.VISIBLE);
                    delete_btn.setVisibility(View.VISIBLE);
                }
            }
            go_insert_btn.setOnClickListener(new View.OnClickListener() {       //답변 버튼
                @Override
                public void onClick(View view) {
                    comment_plus_content_linear.setVisibility(View.VISIBLE);
                    insert_update = 1;
                }
            });

            update_btn.setOnClickListener(new View.OnClickListener() {          //수정 버튼
                @Override
                public void onClick(View view) {
                    comment_plus_content_linear.setVisibility(View.VISIBLE);
                    insert_update = 2;
                    comment_editText.setText(camp_schedule_comment.getCamp_schedule_comment_content());
                }
            });

            insert_btn.setOnClickListener(new View.OnClickListener() {      // 댓글 등록 버튼
                @Override
                public void onClick(View view) {
                    if(chungsa_user == null){
                        Toast.makeText(itemView.getContext(), "로그인을 먼저 해주세요." , Toast.LENGTH_SHORT).show();
                    }
                    else if(insert_update ==1) {
                        camp_comment_insert(camp_schedule_comment);
                    }
                    else if(insert_update == 2){
                        camp_comment_update(camp_schedule_comment);
                    }
                }
            });

            delete_btn.setOnClickListener(new View.OnClickListener() {          //삭제 버튼
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    camp_comment_delete(camp_schedule_comment);
                                    //camp_mark_select(camp_comment);

                                }})
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소시 처리 로직
                                    Toast.makeText(itemView.getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }})
                            .show();
                }
            });

                camp_comment_comment(camp_schedule_comment);
        }

        private void comment_recyclerView_setting(){

            LinearLayoutManager layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            comment_RecyclerView.setLayoutManager(layoutManager);
            Camp_schedule_comment_Adapter adapter = new Camp_schedule_comment_Adapter();

            if(comment_schedule_list.size() != 0)
                camp_comment_item_recylerView_linear.setVisibility(View.VISIBLE);
            for(int i=0; i<comment_schedule_list.size();i++){
                adapter.addItem(comment_schedule_list.get(i));
            }

            comment_RecyclerView.setAdapter(adapter);

        }

        private void camp_comment_comment(Camp_schedule_comment camp_schedule_comment){        //대댓글
            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_comment_comment_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("Camp_recommend_bbs", response);
                            commenet_list_setting(response);
                        }

                        private void commenet_list_setting(String response) {
                            Gson gson = new Gson();
                            comment_schedule_list =new ArrayList<>();
                            Camp_schedule_comment[] camp_schedule_comments = gson.fromJson(response, Camp_schedule_comment[].class);

                            if (camp_schedule_comments != null) {
                                comment_schedule_list.addAll(Arrays.asList(camp_schedule_comments));
                            }
                            comment_recyclerView_setting();

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
                    params.put("parent_camp_schedule_comment_no", Integer.toString(camp_schedule_comment.getCamp_schedule_comment_no()));
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }

//        private void camp_mark_select(Camp_comment camp_comment){        //camp_mark 가져와서 인텐드 다시실행
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_mark_no_select_url,
//                    new Response.Listener<String>() {
//
//                        @Override
//                        public void onResponse(String response) {
//                            // 서버로부터의 응답 처리
//                            Camp_mark_listup(response);
//                        }
//
//                        private void Camp_mark_listup(String response) {
//                            Gson gson = new Gson();
//                            ArrayList<Camp_mark> camp_marks =new ArrayList<>();
//                            Camp_mark[] camp_mark = gson.fromJson(response, Camp_mark[].class);
//
//                            if (camp_mark != null) {
//                                camp_marks.addAll(Arrays.asList(camp_mark));
//                            }
//                            now_camp_mark = camp_marks.get(0);
//
//
//                            Intent intent = new Intent(itemView.getContext(), Camp_mark_bbs_view.class);
//                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
////                            intent.putExtra("Camp_mark",now_camp_mark);
//                            itemView.getContext().startActivity(intent);
//
//                            ((Activity)itemView.getContext()).finish();
//                        }
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Log.i("Camp_recommend_bbs", "Camp_recommend_bbs Volley오류");
//                        }
//                    }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("Camp_mark_no", Integer.toString(camp_comment.getCamp_mark_no()));
//                    return params;
//                }
//            };
//
//            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
//        }
        private void camp_comment_update(Camp_schedule_comment camp_schedule_comment){    //update 코드
            String content_str = comment_editText.getText().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_comment_update_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("Camp_schedule_comment_Adapter", "Insert 응답 : " + response);
                            broadcast_output();
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("Camp_schedule_comment_Adapter", "camp_comment_insert Volley오류");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("camp_schedule_comment_content", content_str);
                    params.put("camp_schedule_comment_no", Integer.toString(camp_schedule_comment.getCamp_schedule_comment_no()));
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);

        }
        private void camp_comment_insert(Camp_schedule_comment camp_schedule_comment){        //삽입시 코드
            String content_str = comment_editText.getText().toString();


            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_comment_insert_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("Camp_schedule_comment_Adapter", "Insert 응답 : " + response);
                            broadcast_output();
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("Camp_schedule_comment_Adapter", "camp_schedule_comment_insert Volley오류");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("camp_schedule_comment_content", content_str);
                    params.put("camp_schedule_no", Integer.toString(camp_schedule_comment.getCamp_schedule_no()));
                    params.put("chungsa_user_email", chungsa_user.getChungsa_user_email());
                    params.put("parent_camp_schedule_comment_no", Integer.toString(camp_schedule_comment.getCamp_schedule_comment_no()));
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }

        private void camp_comment_delete(Camp_schedule_comment camp_schedule_comment){        //삭제버튼 누를시 웹서버 연동
            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_comment_delete_url,
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("Camp_comment_Adapter", "Delete 응답 : " + response);
                            broadcast_output();
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Log.i("Camp_comment_Adapter", "camp_comment_delete Volley오류");
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("camp_schedule_comment_no", Integer.toString(camp_schedule_comment.getCamp_schedule_comment_no()));
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }

        private void broadcast_output(){    //댓글 작성후 업뎃을 시킬 브로드케스트
            Intent intent_to =new Intent("camp_schedule_comment");
            itemView.getContext().sendBroadcast(intent_to);
        }

    }



}
