package com.gj.gjchungsa.weekly.edit;

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
import com.gj.gjchungsa.weekly.Weekly;
import com.gj.gjchungsa.weekly.Weekly_list;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Weekly_edit_Adapter extends RecyclerView.Adapter<Weekly_edit_Adapter.ViewHolder>{

    ArrayList<Weekly> items = new ArrayList<Weekly>();

    public void addItem(Weekly weekly){
        items.add(weekly);
    }
    public void setItems(ArrayList<Weekly> items){
        this.items = items;
    }
    public Weekly getItem(int position){
        return items.get(position);
    }


    @NonNull
    @Override
    public Weekly_edit_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weekly_edit_item, parent, false);
        return new Weekly_edit_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Weekly_edit_Adapter.ViewHolder holder, int position) {
        Weekly item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String url ="http://gjchungsa.com/weekly_images/";
        private static final String weekly_delete_url = "http://gjchungsa.com/weekly/weekly_delete.php";
        TextView textView, delete;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Weekly_edit_title);
        }
        public void setItem(Weekly weekly){
            textView.setText(weekly.getWeekly_when() + " 주보");
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Weekly_edit_view.class);
                    intent.putExtra("weekly", weekly);
                    itemView.getContext().startActivity(intent);
                }
            });

            delete = itemView.findViewById(R.id.Weekly_edit_delete);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직
                                    Weekly_delete(weekly);

                                    Intent intent = new Intent(itemView.getContext(), Weekly_list.class);
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
        private void Weekly_delete(Weekly weekly) {       //삭제과정

            StringRequest stringRequest = new StringRequest(Request.Method.POST, weekly_delete_url,
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
                            Toast.makeText(itemView.getContext(), "volley 오류", Toast.LENGTH_SHORT).show();

                            Log.i("Chungsa_user_edit_Adapter", "chungsa_user_delete Volley오류");
                        }

                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("when", weekly.getWeekly_when());
                    params.put("image1", weekly.getWeekly_image1());
                    params.put("image2", weekly.getWeekly_image2());
                    params.put("image3", weekly.getWeekly_image3());
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }
    }
}
