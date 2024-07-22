package com.gj.gjchungsa.camp.Camp_schedule;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.login.Chungsa_user_InfoManager;

import java.util.ArrayList;

public class Camp_schedule_Adapter extends RecyclerView.Adapter<Camp_schedule_Adapter.ViewHolder>{

    int page_num;
    ArrayList<Camp_schedule> items =new ArrayList<>();


    public Camp_schedule_Adapter() {
    }
    public Camp_schedule_Adapter(int page_num) {
        this.page_num = page_num-1;
    }
    public void addItem(Camp_schedule camp_schedule){
        items.add(camp_schedule);
    }
    public void setItems(ArrayList<Camp_schedule> items){
        this.items = items;
    }

    public void setItem(Camp_schedule camp_schedule, int day){
        items.set(day, camp_schedule);
    }
    public Camp_schedule getItem(int position){
        return items.get(position);
    }

    public ArrayList<Camp_schedule> getItems() {
        return items;
    }

    @NonNull
    @Override
    public Camp_schedule_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_schedule_item, parent, false);

        return new Camp_schedule_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_schedule_Adapter.ViewHolder holder, int position) {
        Camp_schedule item = items.get(position);

        holder.setItem(item, page_num*10 +position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        Chungsa_user_InfoManager infoManager; //로그인 확인
        Chungsa_user user;  //유저
        LinearLayout delete_linear, view_linear;
        TextView position_text, title_text, when_text, what_family_text; //, user_name_text;
        ImageView delete_btn;
        String camp_schedule_delete_url ="http://gjchungsa.com/camp_schedule/camp_schedule_delete.php";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(Camp_schedule camp_schedule, int position) {
            position_text = itemView.findViewById(R.id.camp_schedule_item_position);
            title_text = itemView.findViewById(R.id.camp_schedule_item_title);
            when_text = itemView.findViewById(R.id.camp_schedule_item_when);
            what_family_text = itemView.findViewById(R.id.camp_schedule_item_what_family);
//            user_name_text = itemView.findViewById(R.id.camp_schedule_item_user_name);
            view_linear = itemView.findViewById(R.id.camp_schedule_view_linear);

            infoManager = new Chungsa_user_InfoManager();
            user = infoManager.getUserInfo();       //로그인 정보

            view_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Camp_schedule_view.class);
                    intent.putExtra("camp_schedule", camp_schedule);
                    itemView.getContext().startActivity(intent);
                }
            });
            position_text.setText(Integer.toString(position+1));
            title_text.setText(camp_schedule.getCamp_schedule_title());
            when_text.setText(camp_schedule.getCamp_schedule_when());
            what_family_text.setText(camp_schedule.getCamp_schedule_what_family());
//            user_name_text.setText(camp_schedule.getChungsa_user_name());     //작성자

//            if(user.getChungsa_user_grade().equals("최고관리자") ||      //삭제버튼
//                user.getChungsa_user_email().equals(camp_schedule.getChungsa_user_email())){
//                delete_linear.setVisibility(View.VISIBLE);
//
//                delete_btn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        new AlertDialog.Builder(itemView.getContext())
//                                .setMessage("캠프 일정을 삭제하시겠습니까?")
//                                .setIcon(android.R.drawable.ic_menu_save)
//                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        // 확인시 처리 로직
//                                        delete_ing(camp_schedule);
//                                    }
//
//                                })
//                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int whichButton) {
//                                        // 취소시 처리 로직
//                                        Toast.makeText(itemView.getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
//                                    }})
//                                .show();
//                    }
//                });
//            }
        }
//        private void delete_ing(Camp_schedule camp_schedule) {
//            StringRequest stringRequest = new StringRequest(Request.Method.POST, camp_schedule_delete_url,
//                    new Response.Listener<String>() {
//                        @Override
//                        public void onResponse(String response) {
//                            Log.d("camp_schedule_delete", "camp_schedule_delete 실행");
//
//                            Intent intent =new Intent("camp_schedule_delete");
//                            itemView.getContext().sendBroadcast(intent);
//
//                        }
//
//                    },
//                    new Response.ErrorListener() {
//                        @Override
//                        public void onErrorResponse(VolleyError error) {
//
//                            Log.d("camp_schedule_delete", "camp_schedule_delete Volley오류");
//                        }
//
//                    }) {
//                @Override
//                protected Map<String, String> getParams() {
//                    Map<String, String> params = new HashMap<>();
//                    params.put("no", Integer.toString(camp_schedule.getCamp_schedule_no()));
//                    return params;
//                }
//            };
//
//            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
//        }

    }

}