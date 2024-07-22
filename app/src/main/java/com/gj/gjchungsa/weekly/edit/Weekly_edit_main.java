package com.gj.gjchungsa.weekly.edit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.weekly.Weekly;

import java.util.ArrayList;

public class Weekly_edit_main extends AppCompatActivity {

    Button insert_button;
    Button search_button;
    EditText search_editText;
    RecyclerView recyclerView;
    ImageButton left_arrow_button;
    ImageButton right_arrow_button;
    ArrayList<Weekly> weekly_list_all = new ArrayList<>();
    ArrayList<Weekly> weekly_list = new ArrayList<>();
    int page_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weekly_edit_main);

        insert_button = findViewById(R.id.weekly_edit_main_insert_button);
        search_button = findViewById(R.id.weekly_edit_main_search_button);
        search_editText = findViewById(R.id.weekly_edit_main_search_editText);
        recyclerView = findViewById(R.id.weekly_edit_main_recyclerView);
        left_arrow_button = findViewById(R.id.weekly_edit_main_left_arrow_button);
        right_arrow_button = findViewById(R.id.weekly_edit_main_right_arrow_button);

        Intent get_intent = getIntent();
        weekly_list_all = (ArrayList<Weekly>) get_intent.getSerializableExtra("weekly");
        page_num =1;

        weekly_list = weekly_list_all;

        list_setting(weekly_list_all);


        left_arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    Weekly_edit_Adapter adapter = new Weekly_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< weekly_list.size() ; i++){
                        adapter.addItem(weekly_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_arrow_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < weekly_list.size()){
                    page_num++;
                    Weekly_edit_Adapter adapter = new Weekly_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< weekly_list.size() ; i++){
                        adapter.addItem(weekly_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

    }

    private void list_setting(ArrayList<Weekly> weeklies){
        page_num =1;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Weekly_edit_Adapter adapter = new Weekly_edit_Adapter();

        for(int i=0;i<10 && i< weeklies.size() ; i++){//처음 10개 불러오기
            adapter.addItem(weeklies.get(i));
        }
        recyclerView.setAdapter(adapter);
    }

    public void weekly_edit_search_btn(View view){  //검색

        String keyword = search_editText.getText().toString();
        weekly_list = new ArrayList<>();

        for(int i=0;i<weekly_list_all.size();i++){
            Weekly weekly =weekly_list_all.get(i);
            if(weekly.getWeekly_when().contains(keyword)){

                weekly_list.add(weekly);
            }
        }

        list_setting(weekly_list);

    }

    public void weekly_edit_insert_btn(View view){
        Intent intent = new Intent(getApplicationContext(), Weekly_edit_insert.class);
        startActivity(intent);
    }

}