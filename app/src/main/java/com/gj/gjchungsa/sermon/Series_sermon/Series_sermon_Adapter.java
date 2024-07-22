package com.gj.gjchungsa.sermon.Series_sermon;


import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gj.gjchungsa.R;

import java.util.ArrayList;

public class Series_sermon_Adapter extends RecyclerView.Adapter<Series_sermon_Adapter.ViewHolder>{

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
        View itemView = inflater.inflate(R.layout.series_sermon_item, parent, false);
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
        LinearLayout linearLayout;
        String url = "http://gjchungsa.com/series_sermon/series_sermon_images/";
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }
        public void setItem(Series_sermon series_sermon){
            textView = itemView.findViewById(R.id.series_sermon_textview);
            imageView = itemView.findViewById(R.id.series_sermon_imageView);
            linearLayout = itemView.findViewById(R.id.series_sermon_linear);

            textView.setText(series_sermon.getSeries_name());

            Glide.with(imageView.getContext())
                    .load(url + series_sermon.getSeries_image())
                    .placeholder(R.drawable.loading)
                    .into(imageView);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Series_sermon_list.class);
                    intent.putExtra("series_sermon", series_sermon);
                    itemView.getContext().startActivity(intent);
                }
            });

        }
    }
}