package com.gj.gjchungsa.phozone.edit;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.phozone.Photozone_image;

import java.util.ArrayList;

public class Photozone_image_insert_Adapter extends RecyclerView.Adapter<Photozone_image_insert_Adapter.ViewHolder> {


    ArrayList<Photozone_image> items = new ArrayList<>();

    public void addItem(Photozone_image photozone) {
        items.add(photozone);
    }

    public void setItems(ArrayList<Photozone_image> items) {
        this.items = items;
    }

    public Photozone_image getItem(int position) {
        return items.get(position);
    }


    @NonNull
    @Override
    public Photozone_image_insert_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.photozone_insert_item, parent, false);
        return new Photozone_image_insert_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Photozone_image_insert_Adapter.ViewHolder holder, int position) {
        Photozone_image item = items.get(position);
        holder.setItem(item, position);

    }


    @Override
    public int getItemCount() {
        return items.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {

        Button image_upload_btn;
        Bitmap bitmap;
        ImageView imageview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

        }

        public void setItem(Photozone_image photozone, int position) {
            image_upload_btn = itemView.findViewById(R.id.photozone_insert_item_image_upload_btn);
            imageview = itemView.findViewById(R.id.photozone_insert_item_imageview);


            image_upload_btn.setText(Integer.toString(position+1)+"번째 이미지 업로드");

        }
    }
}
