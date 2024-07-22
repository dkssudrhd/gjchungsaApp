package com.gj.gjchungsa.sermon.sermon_fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gj.gjchungsa.R;
import com.gj.gjchungsa.sermon.Preach_bbs;
import com.gj.gjchungsa.sermon.Preach_bbs_Adapter;

import java.util.ArrayList;

public class Sermon_search_fragment extends Fragment {

    TextView start_text, text_1, text_2, text_3, text_4, text_5, last_text;
    RecyclerView recyclerView;
    int page_num =0;
    ArrayList<Preach_bbs>  preach_bbs_all;
    ArrayList<Preach_bbs>  preach_bbs_search;
    Preach_bbs_Adapter adapter;

    Button search_btn;
    EditText editText;

    public Sermon_search_fragment(ArrayList<Preach_bbs> preach_bbs_all) {
        this.preach_bbs_all = preach_bbs_all;
        this.preach_bbs_search = preach_bbs_all;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_sermon_search_fragment, container, false);

        Toast.makeText(getContext(), Integer.toString(preach_bbs_all.size()), Toast.LENGTH_SHORT);

        findview_setting(rootView);

        page_num = 1;
        list_setting();
        list_view_setting();
        btn_page_number_setting();
        Log.d("checkong_ina" , Integer.toString(preach_bbs_all.size()));

        return rootView;
    }

    private void list_setting(){        //list셋팅
        adapter = new Preach_bbs_Adapter();
        for(int i=(page_num-1)*10 ; i<page_num*10 && i<preach_bbs_search.size() ;i++){
            adapter.addItem(preach_bbs_search.get(i));
        }
        recyclerView.setAdapter(adapter);
    }


    private void btn_page_number_setting(){     //버튼의 숫자와 위치를 셋팅
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
        if(page_num*10<preach_bbs_search.size()){
            text_4.setVisibility(View.VISIBLE);
            text_4.setText(Integer.toString(page_num+1));
        }  else {
            text_4.setVisibility(View.GONE);
        }
        if((page_num+1)*10<preach_bbs_search.size()){
            text_5.setVisibility(View.VISIBLE);
            text_5.setText(Integer.toString(page_num+2));
        }  else {
            text_5.setVisibility(View.GONE);
        }
    }

    private void findview_setting(ViewGroup rootView) {
        start_text = rootView.findViewById(R.id.search_list_start_text);
        text_1 = rootView.findViewById(R.id.search_list_1_text);
        text_2 = rootView.findViewById(R.id.search_list_2_text);
        text_3 = rootView.findViewById(R.id.search_list_3_text);
        text_4 = rootView.findViewById(R.id.search_list_4_text);
        text_5 = rootView.findViewById(R.id.search_list_5_text);
        last_text = rootView.findViewById(R.id.search_list_last_text);
        search_btn = rootView.findViewById(R.id.sermon_search_button);
        editText = rootView.findViewById(R.id.sermon_search_editTextText);
        recyclerView = rootView.findViewById(R.id.search_recycler_view);

        GridLayoutManager layoutManager = new GridLayoutManager(rootView.getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);


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
                page_num = ((preach_bbs_search.size()-1) / 10)+ 1;
                btn_page_number_setting();
                list_setting();
            }
        });
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String keyword  = editText.getText().toString();
                Toast.makeText(getContext(), Integer.toString(preach_bbs_all.size()), Toast.LENGTH_SHORT);

                Log.d("checkong_ina" , Integer.toString(preach_bbs_all.size()));

                page_num=1;

                preach_bbs_search = new ArrayList<>();

                for(int i=0; i<preach_bbs_all.size() ;i++){
                    Preach_bbs now_preach_bbs = preach_bbs_all.get(i);


                    if(now_preach_bbs.getBbs_b_catalogue().contains(keyword) ||
                            now_preach_bbs.getBbs_s_catalogue().contains(keyword) ||
                            now_preach_bbs.getBbs_when().contains(keyword) ||
                            now_preach_bbs.getBbs_title().contains(keyword) ||
                            now_preach_bbs.getBbs_parse().contains(keyword) ||
                            now_preach_bbs.getBbs_preacher().contains(keyword)){
                        Log.d("checkong_ina" , now_preach_bbs.getBbs_title());

                        preach_bbs_search.add(now_preach_bbs);
                    }
                }
                list_setting();
                list_view_setting();
                btn_page_number_setting();
            }
        });
    }


}