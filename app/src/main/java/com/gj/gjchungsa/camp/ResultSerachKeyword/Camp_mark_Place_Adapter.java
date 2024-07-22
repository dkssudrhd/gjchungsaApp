package com.gj.gjchungsa.camp.ResultSerachKeyword;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;

import java.util.ArrayList;


public class Camp_mark_Place_Adapter extends  RecyclerView.Adapter<Camp_mark_Place_Adapter.ViewHolder>{

    ArrayList<Camp_mark_Place> items = new ArrayList<>();

    public void addItem(Camp_mark_Place camp_mark_place){
        items.add(camp_mark_place);
    }
    public void setItems(ArrayList<Camp_mark_Place> items){
        this.items = items;
    }
    public Camp_mark_Place getItem(int position){
        return items.get(position);
    }


    @NonNull
    @Override
    public Camp_mark_Place_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_mark_place_item, parent, false);
        return  new Camp_mark_Place_Adapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_mark_Place_Adapter.ViewHolder holder, int position) {
        Camp_mark_Place item = items.get(position);
        holder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout linearLayout;
        TextView title, address;
        String x, y;
        String place_name;
        String road_address_name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
        public void setItem(Camp_mark_Place camp_mark_place){
            linearLayout = itemView.findViewById(R.id.camp_mark_place_item_layout);
            title = itemView.findViewById(R.id.camp_mark_place_item_title);
            address = itemView.findViewById(R.id.camp_mark_place_item_address);

            place_name =camp_mark_place.getPlace_name();
            road_address_name = camp_mark_place.getRoad_address_name();
            x = camp_mark_place.getX();
            y = camp_mark_place.getY();

            title.setText(place_name);
            address.setText(road_address_name);

            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent =new Intent("camp_mark_place_checked");
                    intent.putExtra("camp_mark_place", camp_mark_place);

                    itemView.getContext().sendBroadcast(intent);
                }
            });
        }

        public String getX() {
            return x;
        }

        public String getY() {
            return y;
        }
    }
}
