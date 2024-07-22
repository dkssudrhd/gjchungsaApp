package com.gj.gjchungsa.sermon.edit;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Preach_bbs;

import java.util.ArrayList;

public class Preach_bbs_edit_Adapter extends RecyclerView.Adapter<Preach_bbs_edit_Adapter.ViewHolder>{
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
    public Preach_bbs_edit_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.preach_bbs_edit_item, parent, false);
        return new Preach_bbs_edit_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Preach_bbs_edit_Adapter.ViewHolder holder, int position) {
        Preach_bbs item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView preach_bbs_edit_title;
        TextView preach_bbs_edit_when;
        LinearLayout preach_bbs_layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void setItem(Preach_bbs preach_bbs){

            preach_bbs_edit_title = itemView.findViewById(R.id.preach_bbs_edit_title);
            preach_bbs_edit_when = itemView.findViewById(R.id.preach_bbs_edit_when);
            preach_bbs_layout = itemView.findViewById(R.id.preach_bbs_layout);

            preach_bbs_edit_title.setText(preach_bbs.getBbs_title());
            preach_bbs_edit_when.setText(preach_bbs.getBbs_when());

            preach_bbs_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Sermon_edit_update.class);
                    intent.putExtra("preach_bbs", preach_bbs);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
