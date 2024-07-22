package com.gj.gjchungsa.sermon.edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.gj.gjchungsa.sermon.Sermon_list;
import com.gj.gjchungsa.sermon.Sermon_view_vimeo;
import com.gj.gjchungsa.sermon.Preach_bbs;
import com.gj.gjchungsa.sermon.Sermon_view_youtube;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Preach_bbs_delete_Adapter extends RecyclerView.Adapter<Preach_bbs_delete_Adapter.ViewHolder>{
    ArrayList<Preach_bbs> items = new ArrayList<Preach_bbs>();

    public void addItem(Preach_bbs preach_bbs){
        items.add(preach_bbs);
    }
    public void setItems(ArrayList<Preach_bbs> items){
        this.items = items;
    }
    public Preach_bbs getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Preach_bbs preach_bbs){
        items.set(position, preach_bbs);
    }

    @NonNull
    @Override
    public Preach_bbs_delete_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.preach_bbs_delete_item, parent, false);
        return new Preach_bbs_delete_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Preach_bbs_delete_Adapter.ViewHolder holder, int position) {
        Preach_bbs item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView preach_bbs_delete_title;
        TextView preach_bbs_delete_when;
        Button preach_bbs_delete_check_btn;
        Button preach_bbs_delete_btn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void setItem(Preach_bbs preach_bbs){
            preach_bbs_delete_title = itemView.findViewById(R.id.preach_bbs_delete_title);
            preach_bbs_delete_when = itemView.findViewById(R.id.preach_bbs_delete_when);
            preach_bbs_delete_check_btn = itemView.findViewById(R.id.preach_bbs_delete_check_btn);
            preach_bbs_delete_btn = itemView.findViewById(R.id.preach_bbs_delete_btn);

            preach_bbs_delete_title.setText(preach_bbs.getBbs_title());
            preach_bbs_delete_when.setText(preach_bbs.getBbs_when());


            preach_bbs_delete_check_btn.setOnClickListener(new View.OnClickListener() { //확인 버튼
                @Override
                public void onClick(View view) {
                    if(preach_bbs.getBbs_video_type().equals("youtube")){
                        Intent intent;
                        intent = new Intent(itemView.getContext(), Sermon_view_youtube.class);
                        intent.putExtra("preach_bbs", preach_bbs);
                        itemView.getContext().startActivity(intent);


                    }
                    else if (preach_bbs.getBbs_video_type().equals("vimeo")){
                        Intent intent;
                        intent = new Intent(itemView.getContext(), Sermon_view_vimeo.class);
                        intent.putExtra("preach_bbs", preach_bbs);
                        itemView.getContext().startActivity(intent);
                    }
                    else{
                        Toast.makeText(itemView.getContext(),"videotype 오류 입니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            });

            preach_bbs_delete_btn.setOnClickListener(new View.OnClickListener() {   //삭제버튼
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    preach_bbs_delete(preach_bbs);

                                    Intent intent = new Intent(itemView.getContext(), Sermon_list.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    itemView.getContext().startActivity(intent);
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

        public void preach_bbs_delete(Preach_bbs preach_bbs){

            String delete_url = "http://gjchungsa.com/preach_bbs/preach_delete.php";
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
                    params.put("url", preach_bbs.getBbs_image_url());
                    params.put("no", Integer.toString(preach_bbs.getBbs_no()));
                    return params;
                }
            };
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }
    }

}
