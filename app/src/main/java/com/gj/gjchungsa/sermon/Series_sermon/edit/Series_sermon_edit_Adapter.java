package com.gj.gjchungsa.sermon.Series_sermon.edit;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Series_sermon.Series_sermon;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Series_sermon_edit_Adapter extends RecyclerView.Adapter<Series_sermon_edit_Adapter.ViewHolder>{

    ArrayList<Series_sermon> items = new ArrayList<>();

    public void addItem(Series_sermon series_sermon){
        items.add(series_sermon);
    }
    public void setItems(ArrayList<Series_sermon> items){
        this.items = items;
    }
    public Series_sermon getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Series_sermon series_sermon){
        items.set(position, series_sermon);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.series_sermon_edit_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Series_sermon item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;
        Button delete_btn;
        String url = "http://gjchungsa.com/series_sermon/series_sermon_images/";
        String delete_url ="http://gjchungsa.com/series_sermon/series_sermon_delete.php";
        Series_sermon now_series_sermon;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void setItem(Series_sermon series_sermon){
            now_series_sermon = series_sermon;
            textView = itemView.findViewById(R.id.series_sermon_edit_textview);
            imageView = itemView.findViewById(R.id.series_sermon_edit_imageView);
            delete_btn = itemView.findViewById(R.id.series_sermon_edit_delete_btn);


            textView.setText(series_sermon.getSeries_name());

            Glide.with(imageView.getContext())
                    .load(url + series_sermon.getSeries_image())
                    .placeholder(R.drawable.loading)
                    .into(imageView);

            delete_btn.setOnClickListener(new View.OnClickListener() {
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
                            Log.d("Series_sermon_edit", response);
                            Intent intent = new Intent("series_update");
                            itemView.getContext().sendBroadcast(intent);
                        }

                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("Series_sermon_edit", "delete_ing() 에러");
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("no", Integer.toString(now_series_sermon.getSeries_no()));
                    return params;
                }
            };
            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }
    }


}