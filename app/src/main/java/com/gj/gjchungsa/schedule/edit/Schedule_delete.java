package com.gj.gjchungsa.schedule.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.schedule.Schedule;
import com.gj.gjchungsa.schedule.Schedule_Adapter;

import java.util.ArrayList;

public class Schedule_delete extends AppCompatActivity {
    public Intent get_intent;
    ImageButton left_button, right_button;
    public int page_num = 1;
    ArrayList<Schedule> schedule_list = new ArrayList<Schedule>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_delete);

        get_intent = getIntent();
        schedule_list =(ArrayList<Schedule>) get_intent.getSerializableExtra("schedule_list");

        RecyclerView recyclerView = findViewById(R.id.schedule_delete_recyclerview);
        left_button = findViewById(R.id.schedule_delete_left_arrow_button);
        right_button = findViewById(R.id.schedule_delete_right_arrow_button);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Schedule_Adapter schedule_adapter = new Schedule_Adapter(this);
        
        for(int i=0;i<10 && i< schedule_list.size() ; i++){//처음 10개 불러오기
            schedule_adapter.addItem(schedule_list.get(i));
        }
        recyclerView.setAdapter(schedule_adapter);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    Schedule_Adapter adapter = new Schedule_Adapter(Schedule_delete.this);

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< schedule_list.size() ; i++){
                        adapter.addItem(schedule_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < schedule_list.size()){
                    page_num++;
                    Schedule_Adapter adapter = new Schedule_Adapter(Schedule_delete.this);

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< schedule_list.size() ; i++){
                        adapter.addItem(schedule_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }
}