package com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_mark;
import com.gj.gjchungsa.camp.Camp_mark_bbs_view;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Camp_time_view_Adapter extends RecyclerView.Adapter<Camp_time_view_Adapter.ViewHolder>{

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
    public Camp_time_view_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_time_view_item, parent, false);

        return new Camp_time_view_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_time_view_Adapter.ViewHolder holder, int position) {
        Camp_time item = items.get(position);
        holder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView day_play_text, title_text, mark_title_text;
        public Camp_mark now_camp_mark;
        String camp_mark_no_url = "http://gjchungsa.com/camp_mark/campmark_no_select.php";
        Camp_time now_camp_time;
        int size;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setItem(Camp_time camp_time, int position){
            size = position;

            now_camp_time = camp_time;

            findView_setting();
            camp_mark_setting();

//            day_play_text.setText(Integer.toString(position+1) + "번째 일정");
            title_text.setText(now_camp_time.getCamp_time_content());

        }

        private void findView_setting(){
//            day_play_text = itemView.findViewById(R.id.camp_time_view_day_play_text);
            title_text = itemView.findViewById(R.id.camp_time_view_title);
//            mark_title_text = itemView.findViewById(R.id.camp_time_view_camp_mark_title);
        }

        private void camp_mark_setting(){       //camp_mark 셋팅
            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_mark_no_url, // Camp_mark 가져오기
                    new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Camp_mark_listup(response);
                        }

                        private void Camp_mark_listup(String response) {
                            Gson gson = new Gson();
                            ArrayList<Camp_mark> camp_mark_list =new ArrayList<>();
                            Camp_mark[] camp_mark = gson.fromJson(response, Camp_mark[].class);

                            if (camp_mark != null) {
                                camp_mark_list.addAll(Arrays.asList(camp_mark));
                            }

                            now_camp_mark = camp_mark_list.get(0);

//                            mark_title_text.setText(now_camp_mark.getCamp_mark_title());// 위치셋팅
                            title_text.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent intent = new Intent(itemView.getContext(), Camp_mark_bbs_view.class);
                                    intent.putExtra("Camp_mark", now_camp_mark);
                                    itemView.getContext().startActivity(intent);
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
                    params.put("Camp_mark_no", Integer.toString(now_camp_time.getCamp_mark_no()));
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }


    }
}
