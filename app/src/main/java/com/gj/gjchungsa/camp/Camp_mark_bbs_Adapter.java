package com.gj.gjchungsa.camp;

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
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.gj.gjchungsa.R;

import java.util.ArrayList;

public class Camp_mark_bbs_Adapter extends RecyclerView.Adapter<Camp_mark_bbs_Adapter.ViewHolder>{


    ArrayList<Camp_mark> items =new ArrayList<>();

    public void addItem(Camp_mark camp_mark){
        items.add(camp_mark);
    }
    public void setItems(ArrayList<Camp_mark> items){
        this.items = items;
    }
    public Camp_mark getItem(int position){
        return items.get(position);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_bbs_item, parent, false);
        return new Camp_mark_bbs_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Camp_mark item = items.get(position);
        holder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }



    static class ViewHolder extends RecyclerView.ViewHolder {
        //TextView textView;
        public static final String url = "http://gjchungsa.com/camp_mark/camp_mark_images/";
        //ImageView imageView;
        public static int num=1;

        LinearLayout camp_mark_item;
        TextView camp_mark_number, camp_mark_title;
        ImageView camp_mark_image1, camp_mark_image2;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            //imageView = itemView.findViewById(R.id.sermon_image);
            //textView = itemView.findViewById(R.id.sermon_title);
        }
        public void setItem(Camp_mark camp_mark,int position){
            camp_mark_item = itemView.findViewById(R.id.camp_mark_item);
            camp_mark_number = itemView.findViewById(R.id.camp_mark_number);
            camp_mark_title = itemView.findViewById(R.id.camp_mark_title);
            camp_mark_image1 = itemView.findViewById(R.id.camp_mark_image1);
            camp_mark_image2 = itemView.findViewById(R.id.camp_mark_image2);

            camp_mark_number.setText(Integer.toString(position+1));
            camp_mark_title.setText(camp_mark.getCamp_mark_title());

            //if(!camp_mark.getCamp_mark_image_url1().equals("")){
                Glide.with(itemView)
                        .load(url + camp_mark.getCamp_mark_image_url1())
                        .transition(DrawableTransitionOptions.withCrossFade()) // 이미지 페이드 효과 적용
                        .into(camp_mark_image1);
            //}
            //if(!camp_mark.getCamp_mark_image_url2().equals("")){
                Glide.with(itemView)
                        .load(url + camp_mark.getCamp_mark_image_url2())
                        .transition(DrawableTransitionOptions.withCrossFade()) // 이미지 페이드 효과 적용
                        .into(camp_mark_image2);
            //}
            camp_mark_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(itemView.getContext(), Camp_mark_bbs_view.class);
                    intent.putExtra("Camp_mark",camp_mark);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }
}
