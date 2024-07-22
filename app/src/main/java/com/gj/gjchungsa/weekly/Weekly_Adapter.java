package com.gj.gjchungsa.weekly;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;

import java.util.ArrayList;

public class Weekly_Adapter extends RecyclerView.Adapter<Weekly_Adapter.ViewHolder>{

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.weekly_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Weekly_Adapter.ViewHolder holder, int position) {
        Weekly item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        String url ="http://gjchungsa.com/weekly_images/";
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.Weekly_title);
        }
        public void setItem(Weekly weekly){
            textView.setText(weekly.getWeekly_when() );
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Weekly_view.class);
                    intent.putExtra("weekly", weekly);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
