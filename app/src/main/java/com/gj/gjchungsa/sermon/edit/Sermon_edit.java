package com.gj.gjchungsa.sermon.edit;

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
import com.gj.gjchungsa.sermon.Preach_bbs;

import java.util.ArrayList;

public class Sermon_edit extends AppCompatActivity {

    public ArrayList<Preach_bbs> preach_bbs_list_all = new ArrayList<>();
    public ArrayList<Preach_bbs> search_preach_bbs_list = new ArrayList<>();
    ImageButton left_button, right_button;
    int page_num;
    EditText sermon_edit_title_search_editText;
    Button sermon_edit_title_search_button;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sermon_edit);

        sermon_edit_title_search_editText = findViewById(R.id.sermon_edit_title_search_editText);
        sermon_edit_title_search_button = findViewById(R.id.sermon_edit_title_search_button);
        recyclerView = findViewById(R.id.sermon_edit_recyclerView);

        page_num =1;
        Intent intent = getIntent();
        preach_bbs_list_all = (ArrayList<Preach_bbs>) intent.getSerializableExtra("preach_bbs");
        for(int i=0; i<preach_bbs_list_all.size();i++){
            search_preach_bbs_list.add(preach_bbs_list_all.get(i));
        }


        list_setting(preach_bbs_list_all);
        left_button = findViewById(R.id.sermon_edit_left_arrow_button);
        right_button = findViewById(R.id.sermon_edit_right_arrow_button);

        left_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num > 1){
                    page_num--;
                    Preach_bbs_edit_Adapter adapter = new Preach_bbs_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< search_preach_bbs_list.size() ; i++){
                        adapter.addItem(search_preach_bbs_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        right_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(page_num*10 < search_preach_bbs_list.size()){
                    page_num++;
                    Preach_bbs_edit_Adapter adapter = new Preach_bbs_edit_Adapter();

                    int page = (page_num-1)*10;
                    for(int i=page;i<page+10 && i< search_preach_bbs_list.size() ; i++){
                        adapter.addItem(search_preach_bbs_list.get(i));
                    }
                    recyclerView.setAdapter(adapter);
                }
            }
        });



    }

    private void list_setting(ArrayList<Preach_bbs> preach_bbs_list){ //리스트 셋팅하기
        page_num =1;

        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        Preach_bbs_edit_Adapter preach_bbs_edit_adapter = new Preach_bbs_edit_Adapter();

        for(int i=0;i<10 && i< preach_bbs_list.size() ; i++){//처음 10개 불러오기
            preach_bbs_edit_adapter.addItem(preach_bbs_list.get(i));
        }
        recyclerView.setAdapter(preach_bbs_edit_adapter);


    }

    public void search_btn(View view){  //검색

        String keyword = sermon_edit_title_search_editText.getText().toString();
        search_preach_bbs_list = new ArrayList<>();

        for(int i=0;i<preach_bbs_list_all.size();i++){
            Preach_bbs preach_bbs =preach_bbs_list_all.get(i);
            if(preach_bbs.getBbs_title().contains(keyword) ||
                    preach_bbs.getBbs_b_catalogue().contains(keyword) ||
                    preach_bbs.getBbs_parse().contains(keyword) ||
                    preach_bbs.getBbs_preacher().contains(keyword) ||
                    preach_bbs.getBbs_s_catalogue().contains(keyword) ||
                    preach_bbs.getBbs_when().contains(keyword)){

                search_preach_bbs_list.add(preach_bbs);
            }
        }

        list_setting(search_preach_bbs_list);

    }

}