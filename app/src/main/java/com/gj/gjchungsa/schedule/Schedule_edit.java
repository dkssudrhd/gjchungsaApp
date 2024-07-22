package com.gj.gjchungsa.schedule;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.schedule.edit.Schedule_delete;
import com.gj.gjchungsa.schedule.edit.Schedule_insert;

import java.util.ArrayList;

public class Schedule_edit extends AppCompatActivity {

    public Intent get_intent;
    ArrayList<Schedule> schedule_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_edit);
        get_intent = getIntent();
        schedule_list =(ArrayList<Schedule>) get_intent.getSerializableExtra("schedule_list");
    }

    public void insert_go(View view){
        Intent intent = new Intent(getApplicationContext(), Schedule_insert.class);
        startActivity(intent);
    }

    public void delete_go(View view){
        Intent intent = new Intent(getApplicationContext(), Schedule_delete.class);
        intent.putExtra("schedule_list", schedule_list);
        startActivity(intent);
    }
}