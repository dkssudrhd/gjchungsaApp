package com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.gj.gjchungsa.ImageSliderAdapter;
import com.gj.gjchungsa.R;

import java.util.ArrayList;

public class Camp_time_day_view_Adapter extends RecyclerView.Adapter<Camp_time_day_view_Adapter.ViewHolder>{

    ArrayList<ArrayList<Camp_time>> items =new ArrayList<>();

    public void addItem(ArrayList<Camp_time> camp_time){
        items.add(camp_time);
    }
    public void setItems(ArrayList<ArrayList<Camp_time>> items){
        this.items = items;
    }

    public void setItem(ArrayList<Camp_time> camp_time_list, int day){
        items.set(day, camp_time_list);
    }
    public ArrayList<Camp_time> getItem(int position){
        return items.get(position);
    }

    public ArrayList<ArrayList<Camp_time>> getItems() {
        return items;
    }

    @NonNull
    @Override
    public Camp_time_day_view_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_time_day_view_item, parent, false);

        return new Camp_time_day_view_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_time_day_view_Adapter.ViewHolder holder, int position) {
        ArrayList<Camp_time> item = items.get(position);
        holder.setItem(item, position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textview;
        RecyclerView recyclerView;
        LinearLayoutManager layoutManager;
        int size =0;
        private ViewPager2 sliderViewPager;
        LinearLayout layoutIndicator;
        ConstraintLayout photo_linear;
        int position;
        Camp_time_view_Adapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(ArrayList<Camp_time> camp_time_list, int position) {
            textview = itemView.findViewById(R.id.camp_time_day_view_textview);
            textview.setText("Day " +Integer.toString(position+1));
            this.position = position;

            recyclerView = itemView.findViewById(R.id.camp_time_day_view_recycerview);
            photo_linear = itemView.findViewById(R.id.camp_time_day_view_constraintLayout);

            layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);
            adapter = new Camp_time_view_Adapter();

            for(int i=0;i<camp_time_list.size();i++){
                adapter.addItem(camp_time_list.get(i));

                Log.d("camp_time_list_view", "adapter"+camp_time_list.get(i).getCamp_time_content());
            }
            recyclerView.setAdapter(adapter);


            image_setting(camp_time_list);
        }

        private void image_setting(ArrayList<Camp_time> camp_time_list) {
            String images_url [] = new String[camp_time_list.size()];
            size =0;
            for(int i=0; i<camp_time_list.size();i++){
                if(!camp_time_list.get(i).getCamp_mark_image_url1().equals("")){
                    images_url[size] = "http://gjchungsa.com/camp_mark/camp_mark_images/" + camp_time_list.get(i).getCamp_mark_image_url1();
                    size++;
                }
            }
            if(size>0)
                photo_linear.setVisibility(View.VISIBLE);
            String new_images_url [] = new String[size];
            for(int i=0; i<size;i++){
                new_images_url[i] = images_url[i];
            }
            sliderViewPager = itemView.findViewById(R.id.camp_time_day_view_ViewPager);
            layoutIndicator = itemView.findViewById(R.id.camp_time_day_view_layoutIndicators);
            sliderViewPager.setOffscreenPageLimit(1);
            sliderViewPager.setAdapter(new ImageSliderAdapter(itemView.getContext(), new_images_url));

            sliderViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                @Override
                public void onPageSelected(int position) {
                    super.onPageSelected(position);
                    setCurrentIndicator(position);
                }
            });

            setupIndicators(size);
        }

        private void setCurrentIndicator(int position) {
            int childCount = layoutIndicator.getChildCount();
            for (int i = 0; i < childCount; i++) {
                ImageView imageView = (ImageView) layoutIndicator.getChildAt(i);
                if (i == position) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            imageView.getContext(),
                            R.drawable.bg_indicator_active
                    ));
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(
                            imageView.getContext(),
                            R.drawable.bg_indicator_inactive
                    ));
                }
            }
        }
        private void setupIndicators(int count) {
            ImageView[] indicators = new ImageView[count];
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            params.setMargins(16, 8, 16, 8);

            for (int i = 0; i < indicators.length; i++) {
                indicators[i] = new ImageView(itemView.getContext());
                indicators[i].setImageDrawable(ContextCompat.getDrawable(itemView.getContext(),
                        R.drawable.bg_indicator_inactive));
                indicators[i].setLayoutParams(params);
                layoutIndicator.addView(indicators[i]);
            }
            setCurrentIndicator(0);
        }
    }

}
