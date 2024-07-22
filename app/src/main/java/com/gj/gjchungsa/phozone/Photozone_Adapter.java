package com.gj.gjchungsa.phozone;

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

public class Photozone_Adapter extends RecyclerView.Adapter<Photozone_Adapter.ViewHolder> {


    ArrayList<Photozone> items = new ArrayList<>();

    public void addItem(Photozone photozone) {
        items.add(photozone);
    }

    public void setItems(ArrayList<Photozone> items) {
        this.items = items;
    }

    public Photozone getItem(int position) {
        return items.get(position);
    }


    @NonNull
    @Override
    public Photozone_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.photozone_item, parent, false);
        return new Photozone_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Photozone_Adapter.ViewHolder holder, int position) {
        Photozone item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView photozone_imageview;
        TextView photozone_textview;
        LinearLayout photozone_item_linear;
        String photozone_image_url = "http://gjchungsa.com/photozone/photozone_image/";


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void setItem(Photozone photozone) {
            photozone_imageview = itemView.findViewById(R.id.photozone_imageview);
            photozone_textview = itemView.findViewById(R.id.photozone_textview);
            photozone_item_linear = itemView.findViewById(R.id.photozone_item_linear);

            Glide.with(itemView.getContext())
                    .load(photozone_image_url + photozone.getPhotozone_image_url())
                    .placeholder(R.drawable.loading)
                    .into(photozone_imageview);


            photozone_textview.setText(photozone.getPhotozone_title());

            photozone_item_linear.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Photozone_view.class);
                    intent.putExtra("no", photozone.getPhotozone_no());
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
