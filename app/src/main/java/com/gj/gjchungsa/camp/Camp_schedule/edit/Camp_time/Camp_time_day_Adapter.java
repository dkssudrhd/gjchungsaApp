package com.gj.gjchungsa.camp.Camp_schedule.edit.Camp_time;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;

import java.util.ArrayList;

public class Camp_time_day_Adapter extends RecyclerView.Adapter<Camp_time_day_Adapter.ViewHolder>{

    ArrayList<ArrayList<Camp_time>> items =new ArrayList<>();
    ArrayList<ViewHolder> holders = new ArrayList<>(); // 뷰 홀더를 저장하는 리스트


    public void addItem(ArrayList<Camp_time> camp_time){
        items.add(camp_time);
    }
    public void setItems(ArrayList<ArrayList<Camp_time>> items){
        this.items = items;
    }

    public void setItem(ArrayList<Camp_time> camp_time_list, int day){
        items.set(day, camp_time_list);
        Log.d("heeeeeeoo", Integer.toString(items.size()));
    }
    public ArrayList<Camp_time> getItem(int position){
        return items.get(position);
    }

    public ArrayList<ArrayList<Camp_time>> getItems() {
        return items;
    }

    @NonNull
    @Override
    public Camp_time_day_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.camp_time_day_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(itemView);
        holders.add(viewHolder);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Camp_time_day_Adapter.ViewHolder holder, int position) {
        ArrayList<Camp_time> item = items.get(position);
        holder.setItem(item, position);
    }

    @Override
    public void onViewRecycled(@NonNull ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.itemView.getContext().unregisterReceiver(holder.receiver);
    }

    public void unregisterReceivers() {
        try {
            for (ViewHolder holder : holders) {
                holder.unregisterReceiver();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ArrayList<Camp_time> camp_time_list_output = new ArrayList<>();
        TextView textview;
        RecyclerView recyclerView;
        Button insert_btn;
        LinearLayoutManager layoutManager;
        int size =0;
        int position;
        Camp_time_Adapter adapter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setItem(ArrayList<Camp_time> camp_time_list, int position) {
            textview = itemView.findViewById(R.id.camp_time_day_textview);
            textview.setText(Integer.toString(position+1) + "번째 날");
            this.position = position;

            recyclerView = itemView.findViewById(R.id.camp_time_day_recycerview);
            insert_btn = itemView.findViewById(R.id.camp_time_day_insert_btn);

            layoutManager = new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(layoutManager);

            adapter = new Camp_time_Adapter();

            if(camp_time_list.size()>=1){
                for(int i=0; i<camp_time_list.size();i++) {
                    adapter.addItem(camp_time_list.get(i));
                    ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();

                    int newHeight = layoutParams.height + (int) (208 * itemView.getContext().getResources().getDisplayMetrics().density);
                    layoutParams.height = newHeight;

                    recyclerView.setLayoutParams(layoutParams);
                }
            }

            IntentFilter filter = new IntentFilter("camp_time_Adapter_checked_btn");      //브로드 캐스트 수신
            itemView.getContext().registerReceiver(receiver, filter);

            recyclerView.setAdapter(adapter);
            insert_btn.setText(Integer.toString(position+1) + "번째 날 추가");

            insert_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();

                    int newHeight = layoutParams.height + (int) (208 * itemView.getContext().getResources().getDisplayMetrics().density);
                    layoutParams.height = newHeight;

                    recyclerView.setLayoutParams(layoutParams);
                    int size_i = adapter.getItemCount();
                    Camp_time new_camp_time = new Camp_time(position, size_i);
                    adapter.addItem(new_camp_time);
                    adapter.notifyItemInserted(adapter.getItemCount());

                    Intent intent =new Intent("camp_time_Adapter_insert_btn");
                    intent.putExtra("camp_time", new_camp_time);
                    itemView.getContext().sendBroadcast(intent);


                    Log.d("broadcast_gogo" , "camp_time_Adapter_insert_btn 보냄, Camp_time_day_Adapter에서 insert_btn");

                }
            });
        }

        private BroadcastReceiver receiver = new BroadcastReceiver() {//눌렀을때 그 좌표로 이동 Camp_mark_place_Adapter에서 받아온 정보를 가지고 표시

            @Override
            public void onReceive(Context context, Intent intent) {
                if( "camp_time_Adapter_checked_btn".equals(intent.getAction())){
                    int day = intent.getIntExtra("day",100);
                    if (day == position) {

                        Log.d("broadcast_gogo" , "camp_time_Adapter_checked_btn 받음, Camp_time_day_Adapter에서 받음");

                        Camp_time get_camp_time = (Camp_time) intent.getSerializableExtra("camp_time");

                        int day_play = get_camp_time.getCamp_time_day_play();
                        adapter.setItem(get_camp_time, day_play);



                        Intent intent_to =new Intent("camp_time_day_Adapter_checked_btn");
                        intent_to.putExtra("camp_time_list", adapter.getItems());
                        intent_to.putExtra("day", get_camp_time.camp_time_day);
                        itemView.getContext().sendBroadcast(intent_to);

                        Log.d("broadcast_gogo" , "camp_time_day_Adapter_checked_btn 보냄 , Camp_time_day_Adapter에서");
//                        Log.d("broadcast_gogo" , "camp_time_Adapter_checked_btn 받음, Camp_time_day_Adapter에서 받음");


//                    Camp_time get_camp_time = (Camp_time) intent.getSerializableExtra("camp_time");
//                    camp_time_list_output.set(get_camp_time.getCamp_time_day_play(), get_camp_time);    //받은걸로 바꾸기

                    }


                }
            }
        };

        public void unregisterReceiver() {
            try {
                itemView.getContext().unregisterReceiver(receiver);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }

}
