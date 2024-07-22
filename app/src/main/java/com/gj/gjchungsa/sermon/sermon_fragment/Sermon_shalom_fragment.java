package com.gj.gjchungsa.sermon.sermon_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Preach_bbs;
import com.gj.gjchungsa.sermon.Preach_bbs_Adapter;

import java.util.ArrayList;


public class Sermon_shalom_fragment extends Fragment {

    public ArrayList<Preach_bbs>  preach_bbs;
    public String catalogue_b;
    public int page_num;

    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;
    RecyclerView recyclerView;
    Preach_bbs_Adapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        page_num=1;
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sermon_shalom_fragment, container, false);

        TextView text_sermon_type = rootView.findViewById(R.id.text_sermon_shalom_type);
        text_sermon_type.setText(catalogue_b);
        recyclerView = rootView.findViewById(R.id.shalom_recycler_view);
        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new Preach_bbs_Adapter();

        findview_setting(rootView);

        page_num =1;

        btn_page_number_setting();
        list_setting();
        list_view_setting();

        return rootView;
    }

    private void list_view_setting(){       //recyclerView에 보여주는 버튼 셋팅
        start_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = 1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num-2;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num-1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num+1;
                btn_page_number_setting();
                list_setting();
            }
        });
        text_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = page_num+2;
                btn_page_number_setting();
                list_setting();
            }
        });
        last_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                page_num = ((preach_bbs.size()-1) / 10)+ 1;
                btn_page_number_setting();
                list_setting();
            }
        });
    }

    private void btn_page_number_setting() {
        if(page_num>=3){
            text_1.setVisibility(View.VISIBLE);
            text_1.setText(Integer.toString(page_num-2));
        } else {
            text_1.setVisibility(View.GONE);
        }
        if(page_num>=2){
            text_2.setVisibility(View.VISIBLE);
            text_2.setText(Integer.toString(page_num-1));
        } else {
            text_2.setVisibility(View.GONE);
        }
        text_3.setText(Integer.toString(page_num));
        if(page_num*10<preach_bbs.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<preach_bbs.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }

    }

    private void list_setting(){        //list셋팅
        adapter = new Preach_bbs_Adapter();
        for(int i=(page_num-1)*10 ; i<page_num*10 && i<preach_bbs.size() ;i++){
            adapter.addItem(preach_bbs.get(i));
        }
        recyclerView.setAdapter(adapter);
    }

    private void findview_setting(ViewGroup rootView) {
        start_text = rootView.findViewById(R.id.shalom_list_start_text);
        text_1 = rootView.findViewById(R.id.shalom_list_1_text);
        text_2 = rootView.findViewById(R.id.shalom_list_2_text);
        text_3 = rootView.findViewById(R.id.shalom_list_3_text);
        text_4 = rootView.findViewById(R.id.shalom_list_4_text);
        text_5 = rootView.findViewById(R.id.shalom_list_5_text);
        last_text = rootView.findViewById(R.id.shalom_list_last_text);
    }

    public Sermon_shalom_fragment(ArrayList<Preach_bbs> preach_bbs, String catalogue_b){
        this.preach_bbs =preach_bbs;
        this.catalogue_b = catalogue_b;
        page_num =1;
    }


}