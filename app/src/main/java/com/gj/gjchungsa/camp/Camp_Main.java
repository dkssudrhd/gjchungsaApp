package com.gj.gjchungsa.camp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.camp.Camp_schedule.Camp_schedule_list;

public class Camp_Main extends AppCompatActivity {

    ImageButton food_view_lodging_recommend,  camp_recommend;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camp_main);

        food_view_lodging_recommend = findViewById(R.id.food_view_lodging_recommend);

        camp_recommend = findViewById(R.id.camp_recommend);


        food_view_lodging_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Camp_recommend_list.class);
                startActivity(intent);
            }
        });

        camp_recommend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), Camp_schedule_list.class);
                startActivity(intent);
            }
        });

    }
}