package com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_mark;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_time_Adapter extends RecyclerView.Adapter<Camp_time_Adapter.ViewHolder>{

    ArrayList<Camp_time> items =new ArrayList<>();


    public void addItem(Camp_time camp_time){
        items.add(camp_time);
    }
    public void setItems(ArrayList<Camp_time> items){
        this.items = items;
    }
    public Camp_time getItem(int position){
        return items.get(position);
    }

    public void setItem(Camp_time item, int day){
        items.set(day, item);
    }

    public ArrayList<Camp_time> getItems() {
        return items;
    }

    @NonNull
    @Override
    public Camp_time_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_time_item, parent, false);

        return new Camp_time_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_time_Adapter.ViewHolder holder, int position) {
        Camp_time item = items.get(position);
        holder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView day_play_text, select_camp_mark_title, content_text;
        EditText search_edit, content_edit;
        Button search_btn, search_change_btn, checked_btn, cancel_btn;
        Camp_mark select_camp_mark = null;
        LinearLayout noselect_linear, select_linear;
        public ArrayList<Camp_mark> all_camp_mark;
        String camp_mark_all_url = "http://gjchungsa.com/camp_mark/campmark_all.php";
        PopupMenu popupMenu;
        MenuItem search[] = new MenuItem[5];
        int size;
        Camp_time now_camp_time;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(Camp_time camp_time, int position){
            now_camp_time = camp_time;
            size = position;

            search_setting();
            day_play_text = itemView.findViewById(R.id.camp_time_day_play_text);
            day_play_text.setText(Integer.toString(position+1) + "번째 일정");
            all_camp_mark_setting();

            checked_cancel_btn_setting(camp_time);

            if(camp_time.getCamp_time_no()!=0){     //update로 넘어올시 수정
                update_setting();
            }
        }

        private void update_setting() {
            Log.d("update_check" , "no : " + now_camp_time.getCamp_time_no() +" title : " + now_camp_time.getCamp_mark_title()
            + " content : " + now_camp_time.getCamp_time_content() +  " camp_mark_no : " + now_camp_time.camp_mark_no + " camp_schedule_no : "
            + now_camp_time.getCamp_schedule_no() + " day : " + now_camp_time.getCamp_time_day() +" day_play : " + now_camp_time.camp_time_day_play);

            content_edit.setVisibility(View.GONE);
            content_text.setVisibility(View.VISIBLE);
            content_text.setText(now_camp_time.getCamp_time_content());
            search_change_btn.setVisibility(View.GONE);

            checked_btn.setVisibility(View.GONE);
            cancel_btn.setVisibility(View.VISIBLE);

            select_linear.setVisibility(View.VISIBLE);
            noselect_linear.setVisibility(View.GONE);
            select_camp_mark_title.setText(now_camp_time.getCamp_mark_title());

        }

        private void checked_cancel_btn_setting(Camp_time camp_time){         //확인버튼셋팅
            checked_btn = itemView.findViewById(R.id.camp_time_checked_btn);
            cancel_btn = itemView.findViewById(R.id.camp_time_cancel_btn);
            content_edit = itemView.findViewById(R.id.camp_time_content_edit);
            content_text = itemView.findViewById(R.id.camp_time_content_text);

            checked_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = content_edit.getText().toString();
                    content_text.setText(text);
                    if( camp_time.getCamp_time_no()!=0 ){        //update
                        cancel_btn.setVisibility(View.VISIBLE);
                        checked_btn.setVisibility(View.GONE);
                        content_edit.setVisibility(View.GONE);
                        content_text.setVisibility(View.VISIBLE);
                        search_change_btn.setVisibility(View.GONE);

                        now_camp_time.setCamp_time_content(text);   //내용
                        if(select_camp_mark !=null)
                            now_camp_time.setCamp_mark_no(select_camp_mark.getCamp_mark_no());  //camp_mark 번호 넣기
                        now_camp_time.setCamp_time_situation("update");

                        Log.d("update_check" , "no : " + now_camp_time.getCamp_time_no() +" title : " + now_camp_time.getCamp_mark_title()
                                + " content : " + now_camp_time.getCamp_time_content() +  " camp_mark_no : " + now_camp_time.camp_mark_no + " camp_schedule_no : "
                                + now_camp_time.getCamp_schedule_no() + " day : " + now_camp_time.getCamp_time_day() +" day_play : " + now_camp_time.camp_time_day_play);



                        Intent intent =new Intent("camp_time_Adapter_checked_btn");
                        intent.putExtra("camp_time", now_camp_time);
                        intent.putExtra("day", now_camp_time.getCamp_time_day());
                        itemView.getContext().sendBroadcast(intent);
                        Log.d("broadcast_gogo" , "Camp_time_Adapter_checked_btn을 보냄, check_btn -> update");
                    }
                     else if(content_text == null){
                        Toast.makeText(itemView.getContext(), "설명을 적어주세요", Toast.LENGTH_SHORT).show();
                    } else if(select_camp_mark == null) {
                        Toast.makeText(itemView.getContext(), "컨텐츠를 선택하세요", Toast.LENGTH_SHORT).show();
                    } else {

                        cancel_btn.setVisibility(View.VISIBLE);
                        checked_btn.setVisibility(View.GONE);
                        content_edit.setVisibility(View.GONE);
                        content_text.setVisibility(View.VISIBLE);
                        search_change_btn.setVisibility(View.GONE);

                        camp_time.setCamp_time_content(text);   //내용
                        camp_time.setCamp_mark_no(select_camp_mark.getCamp_mark_no());  //camp_mark 번호 넣기
                        camp_time.setCamp_time_situation("insert");

                        Intent intent =new Intent("camp_time_Adapter_checked_btn");
                        intent.putExtra("camp_time", camp_time);
                        intent.putExtra("day",camp_time.getCamp_time_day());
                        itemView.getContext().sendBroadcast(intent);
                        Log.d("broadcast_gogo" , "Camp_time_Adapter_checked_btn을 보냄, check_btn -> insert");

                    }
                }
            });

            cancel_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    content_text.setVisibility(View.GONE);
                    content_edit.setVisibility(View.VISIBLE);
                    checked_btn.setVisibility(View.VISIBLE);
                    cancel_btn.setVisibility(View.GONE);
                    search_change_btn.setVisibility(View.VISIBLE);

                    //camp_time.setCamp_time_content(null);   //내용 지우기
                    //camp_time.setCamp_mark_no(0);  //camp_mark 번호 빼기

                    camp_time.setCamp_time_situation("not");

                    Intent intent =new Intent("camp_time_Adapter_checked_btn");
                    intent.putExtra("camp_time", camp_time);
                    intent.putExtra("day",camp_time.getCamp_time_day());
                    itemView.getContext().sendBroadcast(intent);
                    Log.d("broadcast_gogo" , "Camp_time_Adapter_checked_btn을 보냄 , cancel_btn => not으로");
                }
            });

        }

        private void all_camp_mark_setting(){       //camp_mark 셋팅
            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_mark_all_url, // Camp_mark 가져오기
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Camp_mark_listup(response);
                        }

                        private void Camp_mark_listup(String response) {
                            Gson gson = new Gson();
                            all_camp_mark =new ArrayList<>();
                            Camp_mark[] camp_mark = gson.fromJson(response, Camp_mark[].class);

                            if (camp_mark != null) {
                                all_camp_mark.addAll(Arrays.asList(camp_mark));
                            }
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
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }

        private void search_setting(){//검색 셋팅
            search_edit = itemView.findViewById(R.id.camp_time_search_edit);
            search_btn = itemView.findViewById(R.id.camp_time_search_btn);
            noselect_linear = itemView.findViewById(R.id.camp_time_camp_mark_noselect_linear);
            select_linear = itemView.findViewById(R.id.camp_time_camp_mark_select_linear);
            select_camp_mark_title = itemView.findViewById(R.id.camp_time_select_camp_mark_title);
            search_change_btn = itemView.findViewById(R.id.camp_time_search_change_btn);

            popupMenu = new PopupMenu(itemView.getContext(), search_edit);
            MenuInflater inflater = popupMenu.getMenuInflater();
            inflater.inflate(R.menu.camp_time_search_menu, popupMenu.getMenu());

            search[0] = popupMenu.getMenu().findItem(R.id.camp_time_search_1);  //menu 불러오기
            search[1] = popupMenu.getMenu().findItem(R.id.camp_time_search_2);
            search[2] = popupMenu.getMenu().findItem(R.id.camp_time_search_3);
            search[3] = popupMenu.getMenu().findItem(R.id.camp_time_search_4);
            search[4] = popupMenu.getMenu().findItem(R.id.camp_time_search_5);



            search_btn.setOnClickListener(new View.OnClickListener() {      //검색 버튼
                @Override
                public void onClick(View view) {

                    String search_keyword = search_edit.getText().toString();

                    ArrayList<Camp_mark> search_list = new ArrayList<>();
                    int num=0;

                    for(int i=0;i<5;i++){
                        search[i].setVisible(false);
                    }
                    for(int i=0 ; i<all_camp_mark.size() ; i++){
                        Camp_mark now_camp_mark = all_camp_mark.get(i);
                        if (now_camp_mark.getCamp_mark_title().contains(search_keyword) ||
                                    now_camp_mark.getChungsa_user_name().contains(search_keyword) ||
                                    now_camp_mark.getCamp_mark_type().contains(search_keyword)) {

                            search_list.add(now_camp_mark);
                            search[num].setTitle(Integer.toString(num+1) + "번\t" +now_camp_mark.getCamp_mark_title() +
                                "\t/\t" + now_camp_mark.getChungsa_user_name());
                            search[num].setVisible(true);
                            num++;
                        }
                        if(search_list.size()==5)
                            i = all_camp_mark.size();
                    }

                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {  //메뉴 선택시 설정
                        @Override
                        public boolean onMenuItemClick(MenuItem menuItem) {
                            int id = menuItem.getItemId();

                            if(id == R.id.camp_time_search_1){
                                menu_choice(search_list.get(0));
                            } else if (id == R.id.camp_time_search_2) {
                                menu_choice(search_list.get(1));
                            } else if (id == R.id.camp_time_search_3) {
                                menu_choice(search_list.get(2));
                            } else if (id == R.id.camp_time_search_4) {
                                menu_choice(search_list.get(3));
                            } else if (id == R.id.camp_time_search_5) {
                                menu_choice(search_list.get(4));
                            }
                            return false;
                        }
                    });
                    popupMenu.show();
                }
            });
            search_change_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    select_camp_mark = null;
                    select_linear.setVisibility(View.GONE);
                    noselect_linear.setVisibility(View.VISIBLE);
                    search_edit.setText("");
                }
            });
        }

        private void menu_choice(Camp_mark camp_mark){  //메뉴선택시
            select_camp_mark = camp_mark;
            select_linear.setVisibility(View.VISIBLE);
            noselect_linear.setVisibility(View.GONE);
            select_camp_mark_title.setText(camp_mark.getCamp_mark_title());
        }

    }
}
