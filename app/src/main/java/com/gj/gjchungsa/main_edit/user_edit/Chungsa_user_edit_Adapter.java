package com.gj.gjchungsa.main_edit.user_edit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
import com.gj.gjchungsa.login.Chungsa_user;
import com.gj.gjchungsa.main_edit.main_edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Chungsa_user_edit_Adapter extends RecyclerView.Adapter<Chungsa_user_edit_Adapter.ViewHolder>{

    ArrayList<Chungsa_user> items = new ArrayList<Chungsa_user>();

    public void addItem(Chungsa_user chungsa_user){
        items.add(chungsa_user);
    }
    public void setItems(ArrayList<Chungsa_user> items){
        this.items = items;
    }
    public Chungsa_user getItem(int position){
        return items.get(position);
    }

    public void setItem(int position, Chungsa_user chungsa_user){
        items.set(position, chungsa_user);
    }

    @NonNull
    @Override
    public Chungsa_user_edit_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.chungsa_user_edit_item, parent, false);
        return new Chungsa_user_edit_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Chungsa_user_edit_Adapter.ViewHolder holder, int position) {
        Chungsa_user item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        LinearLayout user_edit_linear;
        TextView user_edit_email;
        TextView user_edit_name;
        TextView user_edit_delete;
        private static final String user_delete_url ="http://gjchungsa.com/main_edit/chungsa_user/user_delete.php";

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void setItem(Chungsa_user user){
            user_edit_linear = itemView.findViewById(R.id.user_edit_linear);
            user_edit_email = itemView.findViewById(R.id.user_edit_email);
            user_edit_name = itemView.findViewById(R.id.user_edit_name);
            user_edit_delete = itemView.findViewById(R.id.user_edit_delete);

            user_edit_email.setText("이메일 : " + user.getChungsa_user_email());
            user_edit_name.setText("이름 : " + user.getChungsa_user_name());

            user_edit_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Chungsa_user_edit_view.class);
                    intent.putExtra("user", user);
                    itemView.getContext().startActivity(intent);
                }
            });

            user_edit_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    new AlertDialog.Builder(itemView.getContext())
                            .setMessage( user.getChungsa_user_name() + "님을 삭제하시겠습니까?")
                            .setIcon(android.R.drawable.ic_menu_save)
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 확인시 처리 로직


                                    chungsa_user_delete(user);
                                    Intent intent = new Intent(itemView.getContext(), main_edit.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    itemView.getContext().startActivity(intent);
                                }

                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    // 취소시 처리 로직
                                    Toast.makeText(itemView.getContext(), "취소하였습니다.", Toast.LENGTH_SHORT).show();
                                }})
                            .show();
                }
            });
        }
        private void chungsa_user_delete(Chungsa_user user) {       //삭제과정

            StringRequest stringRequest = new StringRequest(Request.Method.POST, user_delete_url,
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
                    params.put("email", user.getChungsa_user_email());
                    return params;
                }
            };

            Volley.newRequestQueue(itemView.getContext()).add(stringRequest);
        }
    }
}

