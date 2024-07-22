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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Preach_catalouge_s_Adapter extends RecyclerView.Adapter<Preach_catalouge_s_Adapter.ViewHolder>{
    ArrayList<Preach_catalogue_s> items = new ArrayList<>();

    public void addItem(Preach_catalogue_s preach_catalogue_s){
        items.add(preach_catalogue_s);
    }
    public void setItems(ArrayList<Preach_catalogue_s> items){
        this.items = items;
    }
    public Preach_catalogue_s getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Preach_catalogue_s preach_catalogue_s){
        items.set(position, preach_catalogue_s);
    }

    @NonNull
    @Override
    public Preach_catalouge_s_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.preach_s_catalouge_item, parent, false);
        return new Preach_catalouge_s_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Preach_catalouge_s_Adapter.ViewHolder holder, int position) {
        Preach_catalogue_s item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        TextView preach_s_catalouge_title;
        Button preach_s_catalouge_delete_btn;
        String delete_url = "http://gjchungsa.com/series_sermon/catalogue_s_delete.php";
        Preach_catalogue_s now_preach_catalogue_s;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void setItem(Preach_catalogue_s preach_catalogue_s){
            now_preach_catalogue_s = preach_catalogue_s;
            preach_s_catalouge_title = itemView.findViewById(R.id.preach_s_catalouge_title);
            preach_s_catalouge_delete_btn = itemView.findViewById(R.id.preach_s_catalouge_delete_btn);

            preach_s_catalouge_title.setText(preach_catalogue_s.getBbs_s_catalogue());

            preach_s_catalouge_delete_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("삭제 하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    delete_ing();

                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소시 처리 로직
                                    Toast.makeText(itemView.getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .show();
                }

            });
        }

        private void delete_ing() {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, delete_url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // 서버로부터의 응답 처리
                            Log.d("Preach_catalouge_s_Adapter", response);
                            Intent intent = new Intent("catalouge_s");
                            itemView.getContext().sendBroadcast(intent);
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
                    params.put("catalogue_s", now_preach_catalogue_s.getBbs_s_catalogue());
                    return params;
                }
            };
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }


    }

}
