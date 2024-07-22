package com.gj.gjchungsa.schedule;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gj.gjchungsa.weekly.Weekly_image;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Schedule_Adapter  extends RecyclerView.Adapter<Schedule_Adapter.ViewHolder>{

    ArrayList<Schedule> items = new ArrayList<Schedule>();
    private Activity activity;
    public Schedule_Adapter(Activity activity) {
        this.activity = activity;
    }
    public void addItem(Schedule sche){ items.add(sche); }
    public void setItems(ArrayList<Schedule> items){
        this.items = items;
    }
    public Schedule getItem(int position){
        return items.get(position);
    }

    @NonNull
    @Override
    public Schedule_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.schedule_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Schedule_Adapter.ViewHolder holder, int position) {
        Schedule item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String url ="http://gjchungsa.com/schedule/img/";

        TextView schedule_title, schedule_delete_text;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            schedule_title= itemView.findViewById(R.id.schedule_title);
            schedule_delete_text =itemView.findViewById(R.id.schedule_delete_text);
        }
        public void setItem(Schedule sche){
            schedule_title.setText(sche.getSchedule_name());

            schedule_title.setOnClickListener(new View.OnClickListener() {  //클릭시 사진확인
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Weekly_image.class);
                    intent.putExtra("image_res_id", url + sche.getSchedule_url()); // 확대하려는 이미지 리소스 ID로 변경
                    itemView.getContext().startActivity(intent);



                }
            });

            schedule_delete_text.setOnClickListener(new View.OnClickListener() {//클릭시 삭제
                @Override
                public void onClick(View view) {

                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    schedule_delete(sche);

                                    Intent intent = new Intent(itemView.getContext(), Schedule_view.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    activity.startActivity(intent);
                                }})
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소시 처리 로직
                                    Toast.makeText(itemView.getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }})
                            .show();
                }
            });
        }
        public void schedule_delete(Schedule sche){

            String delete_url = "http://gjchungsa.com/schedule/schedule_delete.php";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Toast.makeText(itemView.getContext(), response, Toast.LENGTH_SHORT).show();

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
                    params.put("url", sche.getSchedule_url());
                    params.put("no", Integer.toString(sche.getSchedule_no()));
                    return params;
                }
            };
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }
    }
}
