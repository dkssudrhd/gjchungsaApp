package com.gj.gjchungsa.sermon;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.gj.gjchungsa.R;
import com.github.florent37.fiftyshadesof.FiftyShadesOf;

import java.util.ArrayList;

public class Preach_bbs_Adapter extends RecyclerView.Adapter<Preach_bbs_Adapter.ViewHolder>{

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
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.sermon_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Preach_bbs item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{
        //TextView textView;
        String url ="http://gjchungsa.com/preach_bbs/sermon_images/";
        //ImageView imageView;
        Button button;
        private FiftyShadesOf fiftyShadesOf;
        Drawable placeholderImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            button = itemView.findViewById(R.id.sermon_choice_button);
            fiftyShadesOf = FiftyShadesOf.with(itemView.getContext())
                    .on(button)
                    .start();

            //imageView = itemView.findViewById(R.id.sermon_image);
            //textView = itemView.findViewById(R.id.sermon_title);
        }
        public void setItem(Preach_bbs preach_bbs){
            button.setText(preach_bbs.getBbs_title());

            Glide.with(itemView)
                    .load(url +preach_bbs.getBbs_image_url())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.warning)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .override(250, 250)
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, Transition<? super Drawable> transition) {
                            button.setCompoundDrawablesWithIntrinsicBounds(null, resource, null, null);
                        }

                        @Override
                        public void onLoadCleared( Drawable placeholder) {
                            // 이미지가 로드되지 않았을 때의 처리를 여기에 추가할 수 있습니다.
                        }
                    });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent;
                    String type =preach_bbs.getBbs_video_type();

                    if("youtube".equals(type)){
                        //intent = new Intent(itemView.getContext(), Sermon_view_vimeo.class);
                        intent = new Intent(itemView.getContext(), Sermon_view_youtube.class);
                        intent.putExtra("preach_bbs", preach_bbs);
                        itemView.getContext().startActivity(intent);
                        //Toast.makeText(itemView.getContext(),"유튜브", Toast.LENGTH_SHORT).show();
                    }
                    else if("vimeo".equals(type)){
                        intent = new Intent(itemView.getContext(), Sermon_view_vimeo.class);
                        intent.putExtra("preach_bbs", preach_bbs);
                        itemView.getContext().startActivity(intent);
                    }
                    else{
                        Toast.makeText(itemView.getContext(),"Preach_bbs_Adapter오류", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            fiftyShadesOf.stop();
            //Toast.makeText(itemView.getContext(),"fify", Toast.LENGTH_SHORT).show();
        }
    }
}


//imageView = preach_bbs.getBbs_image_url();